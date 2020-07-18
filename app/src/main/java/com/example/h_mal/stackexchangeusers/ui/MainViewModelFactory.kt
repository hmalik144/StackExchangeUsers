package com.example.h_mal.stackexchangeusers.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.h_mal.stackexchangeusers.data.repositories.Repository

/**
 * Viewmodel factory for [MainViewModel]
 * @repository injected into MainViewModel
 */
class MainViewModelFactory(
    private val repository: Repository
) : ViewModelProvider.Factory{

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return (MainViewModel(repository)) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}