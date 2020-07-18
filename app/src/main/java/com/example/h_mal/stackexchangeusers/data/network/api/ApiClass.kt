package com.example.h_mal.stackexchangeusers.data.network.api

import com.example.h_mal.stackexchangeusers.data.network.api.interceptors.NetworkConnectionInterceptor
import com.example.h_mal.stackexchangeusers.data.network.api.interceptors.QueryParamsInterceptor
import com.example.h_mal.stackexchangeusers.data.network.model.ApiResponse
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


interface ApiClass {

    @GET("users?")
    suspend fun getUsersFromApi(@Query("inname") inname: String): Response<ApiResponse>

    // invoke method creating an invocation of the api call
    companion object{
        operator fun invoke(
            // injected @params
            networkConnectionInterceptor: NetworkConnectionInterceptor,
            queryParamsInterceptor: QueryParamsInterceptor
        ) : ApiClass {

            // okHttpClient with interceptors
            val okkHttpclient = OkHttpClient.Builder()
                .addNetworkInterceptor(networkConnectionInterceptor)
                .addInterceptor(queryParamsInterceptor)
                .build()

            // creation of retrofit class
            return Retrofit.Builder()
                .client(okkHttpclient)
                .baseUrl("https://api.stackexchange.com/2.2/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiClass::class.java)
        }
    }
}