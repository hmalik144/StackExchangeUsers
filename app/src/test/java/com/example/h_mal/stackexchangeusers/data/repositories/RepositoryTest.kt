package com.example.h_mal.stackexchangeusers.data.repositories

import com.example.h_mal.stackexchangeusers.data.network.api.ApiClass
import com.example.h_mal.stackexchangeusers.data.network.response.ApiResponse
import com.example.h_mal.stackexchangeusers.data.preferences.PreferenceProvider
import com.example.h_mal.stackexchangeusers.data.room.AppDatabase
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.MockitoAnnotations
import retrofit2.Response
import okhttp3.ResponseBody
import okhttp3.ResponseBody.Companion.toResponseBody
import okio.IOException
import kotlin.test.assertFailsWith

class RepositoryTest {

    lateinit var repository: Repository

    @Mock
    lateinit var api: ApiClass
    @Mock
    lateinit var db: AppDatabase
    @Mock
    lateinit var prefs: PreferenceProvider

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        repository = RepositoryImpl(api, db, prefs)
    }

    @Test
    fun fetchUserFromApi_positiveResponse() = runBlocking {
        // GIVEN
        val input = "12345"
        val mockApiResponse = mock(ApiResponse::class.java)
        val mockResponse = Response.success(mockApiResponse)

        // WHEN
        Mockito.`when`(api.getUsersFromApi(input)).thenReturn(
            mockResponse
        )
        Mockito.`when`(prefs.getLastSavedAt(input)).thenReturn(null)

        // THEN
        val apiResponse = repository.getUsersFromApi(input)
        assertNotNull(apiResponse)
        assertEquals(mockApiResponse, apiResponse)
    }

    @Test
    fun fetchUserFromApi_negativeResponse() = runBlocking {
        //GIVEN
        val input = "12345"
        val errorMessage = "{\n" +
                "  \"status_code\": 7,\n" +
                "  \"error_message\": \"Invalid API key: You must be granted a valid key.\",\n" +
                "}"

        val errorResponseBody = errorMessage.toResponseBody("application/json".toMediaTypeOrNull())
        val mockResponse = Response.error<String>(404, errorResponseBody)

        //WHEN
        Mockito.`when`(api.getUsersFromApi(input)).then { mockResponse }

        //THEN - assert exception is not null
        val ioExceptionReturned = assertFailsWith<IOException> {
            repository.getUsersFromApi(input)
        }
        assertNotNull(ioExceptionReturned)
        assertEquals(ioExceptionReturned.message, "Invalid API key: You must be granted a valid key.")
    }

    @Test
    fun fetchUserFromApi_alreadySearched() = runBlocking {
        // GIVEN
        val input = "12345"
        val mockApiResponse = mock(ApiResponse::class.java)
        val mockResponse = Response.success(mockApiResponse)

        //WHEN
        Mockito.`when`(api.getUsersFromApi(input)).thenReturn(
            mockResponse
        )
        Mockito.`when`(prefs.getLastSavedAt(input)).thenReturn(System.currentTimeMillis())

        // THEN
        val getUser = repository.getUsersFromApi(input)
        assertEquals(getUser, null)
    }

}