package com.example.h_mal.stackexchangeusers.application

import android.app.Application
import androidx.test.espresso.idling.CountingIdlingResource
import com.example.h_mal.stackexchangeusers.data.network.api.ApiClass
import com.example.h_mal.stackexchangeusers.data.network.api.interceptors.NetworkConnectionInterceptor
import com.example.h_mal.stackexchangeusers.data.network.api.interceptors.QueryParamsInterceptor
import com.example.h_mal.stackexchangeusers.data.preferences.PreferenceProvider
import com.example.h_mal.stackexchangeusers.data.repositories.RepositoryImpl
import com.example.h_mal.stackexchangeusers.data.room.AppDatabase
import com.example.h_mal.stackexchangeusers.ui.MainViewModelFactory
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

class AppClass : Application(), KodeinAware{

    companion object{
        // idling resource to be used for espresso testing
        // when we need to wait for async operations to complete
        val idlingResources = CountingIdlingResource("Data_loader")
    }

    // Kodein creation of modules to be retrieve within the app
    override val kodein = Kodein.lazy {
        import(androidXModule(this@AppClass))

        bind() from singleton { NetworkConnectionInterceptor(instance()) }
        bind() from singleton { QueryParamsInterceptor() }
        bind() from singleton { ApiClass(instance(), instance())}
        bind() from singleton { AppDatabase(instance()) }
        bind() from singleton { PreferenceProvider(instance()) }
        bind() from singleton { RepositoryImpl(instance(), instance(), instance()) }
        bind() from provider { MainViewModelFactory(instance()) }
    }

}