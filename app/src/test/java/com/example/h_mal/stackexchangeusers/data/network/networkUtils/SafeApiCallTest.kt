package com.example.h_mal.stackexchangeusers.data.network.networkUtils

import com.example.h_mal.stackexchangeusers.data.network.response.ApiResponse
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.*
import org.junit.Test
import retrofit2.Response
import java.io.IOException
import kotlin.test.assertFailsWith

class SafeApiCallTest: SafeApiCall(){

    @Test
    fun successfulResponse_SuccessfulOutput() = runBlocking{
        // GIVEN
        val mockApiResponse = mockk<ApiResponse>()
        val mockResponse = Response.success(mockApiResponse)

        // WHEN
        val result = responseUnwrap { mockResponse }

        // THEN
        assertNotNull(result)
        assertEquals(mockApiResponse, result)
    }

    @Test
    fun unsuccessfulResponse_thrownOutput() = runBlocking{
        // GIVEN
        val errorMessage = "{\n" +
                "  \"status_code\": 7,\n" +
                "  \"error_message\": \"Invalid API key: You must be granted a valid key.\",\n" +
                "}"

        val errorResponseBody = errorMessage.toResponseBody("application/json".toMediaTypeOrNull())
        val mockResponse = Response.error<String>(404, errorResponseBody)

        //THEN - assert exception is not null
        val ioExceptionReturned = assertFailsWith<IOException> {
            responseUnwrap { mockResponse }!!
        }

        assertNotNull(ioExceptionReturned)
        assertEquals(ioExceptionReturned.message, "Invalid API key: You must be granted a valid key.")
    }

}