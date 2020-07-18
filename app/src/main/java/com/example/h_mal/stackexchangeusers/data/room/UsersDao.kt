package com.example.h_mal.stackexchangeusers.data.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.h_mal.stackexchangeusers.data.room.entities.UserItem

@Dao
interface UsersDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveAllUsers(users : List<UserItem>)

    @Query("SELECT * FROM UserItem")
    fun getAllUsers() : LiveData<List<UserItem>>

    // clear database and add new entries
    @Transaction
    suspend fun upsertNewUsers(users : List<UserItem>){
        deleteEntries()
        saveAllUsers(users)
    }

    @Query("DELETE FROM UserItem")
    suspend fun deleteEntries()

    @Query("SELECT * FROM UserItem WHERE userId = :id")
    fun getUser(id: Int) : LiveData<UserItem>
}