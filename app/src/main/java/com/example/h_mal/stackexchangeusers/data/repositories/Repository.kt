package com.example.h_mal.stackexchangeusers.data.repositories

import androidx.lifecycle.LiveData
import com.example.h_mal.stackexchangeusers.data.network.model.ApiResponse
import com.example.h_mal.stackexchangeusers.data.network.model.User
import com.example.h_mal.stackexchangeusers.data.room.entities.UserItem

interface Repository {

    fun getUsersFromDatabase(): LiveData<List<UserItem>>
    fun getSingleUserFromDatabase(id: Int): LiveData<UserItem>
    suspend fun saveUsersToDatabase(users: List<User>)
    suspend fun getUsersFromApi(username: String): ApiResponse?
    fun saveCurrentSearchToPrefs(username: String)

}