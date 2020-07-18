package com.example.h_mal.stackexchangeusers.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.h_mal.stackexchangeusers.application.AppClass.Companion.idlingResources
import com.example.h_mal.stackexchangeusers.data.repositories.Repository
import com.example.h_mal.stackexchangeusers.utils.Event
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.IOException

class MainViewModel(
    private val repository: Repository
) : ViewModel() {

    // livedata for user items in room database
    val usersLiveData = repository.getUsersFromDatabase()

    val operationState = MutableLiveData<Event<Boolean>>()
    val operationError = MutableLiveData<Event<String>>()


    fun getUsers(username: String?){
        // validate that search term is not empty
        if (username.isNullOrBlank()){
            operationError.postValue(Event("Enter a valid username"))
            return
        }
        // open a coroutine on the IO thread for async operations
        CoroutineScope(Dispatchers.IO).launch {
            operationState.postValue(Event(true))
            try {
                // get users from api
                val response = repository.getUsersFromApi(username)
                // null check response exists and contains list of users
                response?.items?.let {
                    // save users to database
                    repository.saveUsersToDatabase(it)
                    // save current search entry to preferences
                    repository.saveCurrentSearchToPrefs(username)
                }
            }catch (e: IOException){
                operationError.postValue(Event(e.message!!))
            }finally {
                operationState.postValue(Event(false))
            }
        }
    }

    fun getSingleUser(id: Int) = repository.getSingleUserFromDatabase(id)

}
