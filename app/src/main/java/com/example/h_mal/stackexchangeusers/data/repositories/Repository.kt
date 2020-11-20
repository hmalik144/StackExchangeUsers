package com.example.h_mal.stackexchangeusers.data.repositories

import androidx.lifecycle.LiveData
import com.example.h_mal.stackexchangeusers.data.network.response.ApiResponse
import com.example.h_mal.stackexchangeusers.data.network.response.User
import com.example.h_mal.stackexchangeusers.data.room.entities.UserEntity

interface Repository {

    fun getUsersFromDatabase(): LiveData<List<UserEntity>>
    fun getSingleUserFromDatabase(id: Int): LiveData<UserEntity>
    suspend fun saveUsersToDatabase(users: List<User>)
    suspend fun getUsersFromApi(username: String): ApiResponse?
    fun saveCurrentSearchToPrefs(username: String)

}