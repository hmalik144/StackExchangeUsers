package com.example.h_mal.stackexchangeusers.data.repositories

import com.example.h_mal.stackexchangeusers.data.network.api.ApiClass
import com.example.h_mal.stackexchangeusers.data.network.response.ApiResponse
import com.example.h_mal.stackexchangeusers.data.network.response.User
import com.example.h_mal.stackexchangeusers.data.network.networkUtils.SafeApiCall
import com.example.h_mal.stackexchangeusers.data.preferences.PreferenceProvider
import com.example.h_mal.stackexchangeusers.data.room.AppDatabase
import com.example.h_mal.stackexchangeusers.data.room.entities.UserEntity

const val MILLISECONDS_ONE_MIN = 60000
class RepositoryImpl(
    private val api: ApiClass,
    private val database: AppDatabase,
    private val preference: PreferenceProvider
) : SafeApiCall(), Repository {

    override fun getUsersFromDatabase() = database.getUsersDao().getAllUsers()

    override fun getSingleUserFromDatabase(id: Int) = database.getUsersDao().getUser(id)

    override suspend fun saveUsersToDatabase(users: List<User>){
        val userList= users.map { UserEntity(it) }
        database.getUsersDao().upsertNewUsers(userList)
    }

    override suspend fun getUsersFromApi(username: String): ApiResponse? {
        return when (isSearchValid(username)) {
            true -> responseUnwrap { api.getUsersFromApi(username) }
            else -> { null }
        }
    }

    override fun saveCurrentSearchToPrefs(username: String){
        val time = System.currentTimeMillis()
        preference.saveLastSavedAt(username, time)
    }

    // Check if search is valid based on lasted saved
    private fun isSearchValid(username: String): Boolean {
        val time = preference.getLastSavedAt(username) ?: return true
        val currentTime = System.currentTimeMillis()
        val difference = currentTime - time

        return difference > MILLISECONDS_ONE_MIN
    }
}