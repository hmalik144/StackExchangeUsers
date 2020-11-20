package com.example.h_mal.stackexchangeusers.data.room

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.matcher.ViewMatchers
import com.example.h_mal.stackexchangeusers.data.room.entities.UserEntity
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import java.io.IOException
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors

class AppDatabaseTest{
    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    private lateinit var simpleDao: UsersDao
    private lateinit var db: AppDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, AppDatabase::class.java)
            .build()
        simpleDao = db.getUsersDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun writeEntryAndReadResponse(){
        // Given
        val entity = UserEntity(123)
        val latch = CountDownLatch(1)
        var userEntity: UserEntity? = null

        // When
        simpleDao.saveAllUsers(listOf(entity))

        // Then
        simpleDao.getUser(123).observeForever {
            userEntity = it
            latch.countDown()
        }

        ViewMatchers.assertThat(userEntity, CoreMatchers.equalTo(entity))
    }

    @Test
    @Throws(Exception::class)
    fun upsertUsersAndReadResponse() = runBlocking{
        // Given
        val userEntity = UserEntity(123)
        val newUserEntity = UserEntity(456)
        var result: List<UserEntity>? = null

        // When
        simpleDao.saveAllUsers(listOf(userEntity))
        simpleDao.upsertNewUsers(listOf(newUserEntity))

        // Then
        val latch = CountDownLatch(1)
        simpleDao.getAllUsers().observeForever {
            result = it
            latch.countDown()
        }

        ViewMatchers.assertThat(newUserEntity, CoreMatchers.equalTo(result?.get(0)))
    }

}