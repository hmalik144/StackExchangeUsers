package com.example.h_mal.stackexchangeusers.ui.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import com.example.h_mal.stackexchangeusers.data.network.response.ApiResponse
import com.example.h_mal.stackexchangeusers.data.repositories.Repository
import com.example.h_mal.stackexchangeusers.data.room.entities.UserEntity
import com.example.h_mal.stackexchangeusers.ui.MainViewModel
import kotlinx.coroutines.delay
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

class MainViewModelTest {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    lateinit var viewModel: MainViewModel

    @Mock
    lateinit var repository: Repository

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        val mockLiveData = object: LiveData<List<UserEntity>>(){}
        Mockito.`when`(repository.getUsersFromDatabase()).thenReturn(mockLiveData)

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
        delay(50)
        assertFalse { viewModel.operationState.value?.getContentIfNotHandled()!! }
    }

    @Test
    fun getApiFromRepository_unsuccessfulReturn() = runBlocking{
        // WHEN
        Mockito.`when`(repository.getUsersFromApi("12345")).thenAnswer{ throw IOException("throwed") }

        // THEN
        viewModel.getUsers("12345")
        delay(50)
        assertEquals (viewModel.operationError.value?.getContentIfNotHandled()!!, "throwed")
    }
}