package com.example.h_mal.stackexchangeusers.data.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.h_mal.stackexchangeusers.data.room.entities.UserEntity

@Dao
interface UsersDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveAllUsers(users : List<UserEntity>)

    @Query("SELECT * FROM UserEntity")
    fun getAllUsers() : LiveData<List<UserEntity>>

    // clear database and add new entries
    @Transaction
    suspend fun upsertNewUsers(users : List<UserEntity>){
        deleteEntries()
        saveAllUsers(users)
    }

    @Query("DELETE FROM UserEntity")
    suspend fun deleteEntries()

    @Query("SELECT * FROM UserEntity WHERE userId = :id")
    fun getUser(id: Int) : LiveData<UserEntity>
}