package com.example.h_mal.stackexchangeusers.ui.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.h_mal.stackexchangeusers.data.network.model.ApiResponse
import com.example.h_mal.stackexchangeusers.data.network.model.User
import com.example.h_mal.stackexchangeusers.data.repositories.Repository
import com.example.h_mal.stackexchangeusers.data.repositories.RepositoryImpl
import com.example.h_mal.stackexchangeusers.data.room.entities.UserItem
import com.example.h_mal.stackexchangeusers.ui.MainViewModel
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.MockitoAnnotations
import java.io.IOException
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class MainViewModelTest {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    lateinit var viewModel: MainViewModel

    @Mock
    lateinit var repository: Repository

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        viewModel = MainViewModel(repository)

    }

    @Test
    fun getApiFromRepository_SuccessfulReturn() = runBlocking{
        //GIVEN
        val mockApiResponse = mock(ApiResponse::class.java)

        //WHEN
        Mockito.`when`(repository.getUsersFromApi("12345")).thenReturn(mockApiResponse)

        //THEN
        viewModel.getUsers("12345")
        viewModel.operationState.observeForever{
            it.getContentIfNotHandled()?.let {result ->
                assertFalse { result }
            }
        }
    }

    @Test
    fun getApiFromRepository_unsuccessfulReturn() = runBlocking{
        // WHEN
        Mockito.`when`(repository.getUsersFromApi("12345")).thenAnswer{ throw IOException("throwed") }

        // THEN
        viewModel.getUsers("fsdfsdf")
        viewModel.operationError.observeForever{
            it.getContentIfNotHandled()?.let {result ->
                assertEquals(result, "throwed")
            }
        }
    }
}