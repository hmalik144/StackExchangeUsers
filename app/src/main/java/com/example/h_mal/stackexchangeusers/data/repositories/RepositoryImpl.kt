package com.example.h_mal.stackexchangeusers.data.repositories

import com.example.h_mal.stackexchangeusers.data.network.api.ApiClass
import com.example.h_mal.stackexchangeusers.data.network.model.ApiResponse
import com.example.h_mal.stackexchangeusers.data.network.model.User
import com.example.h_mal.stackexchangeusers.data.network.networkUtils.ResponseUnwrap
import com.example.h_mal.stackexchangeusers.data.preferences.PreferenceProvider
import com.example.h_mal.stackexchangeusers.data.room.AppDatabase
import com.example.h_mal.stackexchangeusers.data.room.entities.UserItem

const val MILLISECONDS_ONE_MIN = 60000
class RepositoryImpl(
    private val api: ApiClass,
    private val database: AppDatabase,
    private val preference: PreferenceProvider
) : ResponseUnwrap(), Repository {

    // Current list of users in the database
    override fun getUsersFromDatabase() = database.getUsersDao().getAllUsers()

    // retrieving a single user from an ID
    override fun getSingleUserFromDatabase(id: Int) = database.getUsersDao().getUser(id)

    // save a list of users to the room database
    override suspend fun saveUsersToDatabase(users: List<User>){
        val userList= getUserList(users)
        database.getUsersDao().upsertNewUsers(userList)
    }

    // fetch users from an api call
    override suspend fun getUsersFromApi(username: String): ApiResponse? {
        return when (isSearchValid(username)) {
            true -> responseUnwrap { api.getUsersFromApi(username) }
            else -> { null }
        }
    }

    // save current time and current search input into shared prefs
    override fun saveCurrentSearchToPrefs(username: String){
        val time = System.currentTimeMillis()
        preference.saveLastSavedAt(username, time)
    }

    // boolean response of validity of search
    // if the same search is taking place again with a minute return false
    private fun isSearchValid(username: String): Boolean {
        val time = preference.getLastSavedAt(username) ?: return true
        val currentTime = System.currentTimeMillis()
        val difference = currentTime - time

        return difference > MILLISECONDS_ONE_MIN
    }

    // map user from api response to userItem entity for database
    private fun getUserList(users: List<User>): List<UserItem> {
        return users.map {
            it.mapToUserItem()
        }
    }

    private fun User.mapToUserItem(): UserItem {
        return UserItem(
            userId,
            displayName,
            badgeCounts?.bronze,
            badgeCounts?.silver,
            badgeCounts?.gold,
            reputation,
            creationDate,
            profileImage
        )
    }
}