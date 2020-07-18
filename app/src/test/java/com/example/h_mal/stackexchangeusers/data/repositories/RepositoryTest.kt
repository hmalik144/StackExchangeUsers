package com.example.h_mal.stackexchangeusers.data.repositories

import com.example.h_mal.stackexchangeusers.data.network.api.ApiClass
import com.example.h_mal.stackexchangeusers.data.network.model.ApiResponse
import com.example.h_mal.stackexchangeusers.data.preferences.PreferenceProvider
import com.example.h_mal.stackexchangeusers.data.room.AppDatabase
import kotlinx.coroutines.runBlocking
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
        val getUser = repository.getUsersFromApi(input)
        assertNotNull(getUser)
        assertEquals(mockApiResponse, getUser)
    }

    @Test
    fun fetchUserFromApi_negativeResponse() = runBlocking {
        //GIVEN
        //mock retrofit error response
        val mockBody = mock(ResponseBody::class.java)
        val mockRaw = mock(okhttp3.Response::class.java)
        val re = Response.error<String>(mockBody, mockRaw)

        //WHEN
        Mockito.`when`(api.getUsersFromApi("12345")).then { re }

        //THEN - assert exception is not null
        val ioExceptionReturned = assertFailsWith<IOException> {
            repository.getUsersFromApi("12345")
        }
        assertNotNull(ioExceptionReturned)
        assertNotNull(ioExceptionReturned.message)
    }

    @Test
    fun fetchUserFromApi_alreadySearched() = runBlocking {
        // GIVEN
        val mockApiResponse = mock(ApiResponse::class.java)
        val mockResponse = Response.success(mockApiResponse)

        //WHEN
        Mockito.`when`(api.getUsersFromApi("12345")).thenReturn(
            mockResponse
        )
        Mockito.`when`(prefs.getLastSavedAt("12345")).thenReturn(System.currentTimeMillis())

        // THEN
        val getUser = repository.getUsersFromApi("12345")
        assertEquals(getUser, null)
    }

}