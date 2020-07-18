package com.example.h_mal.stackexchangeusers.ui.main

import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.h_mal.stackexchangeusers.R
import com.example.h_mal.stackexchangeusers.application.AppClass
import com.example.h_mal.stackexchangeusers.application.AppClass.Companion.idlingResources
import com.example.h_mal.stackexchangeusers.ui.MainViewModel
import com.example.h_mal.stackexchangeusers.ui.MainViewModelFactory
import com.example.h_mal.stackexchangeusers.utils.Event
import com.example.h_mal.stackexchangeusers.utils.displayToast
import com.example.h_mal.stackexchangeusers.utils.hide
import com.example.h_mal.stackexchangeusers.utils.show
import kotlinx.android.synthetic.main.main_activity.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance


/**
 *    [MainActivity] hosting the fragments and controlling a lot of the UI
 */
class MainActivity : AppCompatActivity(), KodeinAware {

    //retrieve the viewmodel factory from the kodein dependency injection
    override val kodein by kodein()
    private val factory by instance<MainViewModelFactory>()
    // Kotlin lazy instantiation of viewmodel
    val viewmodel by viewModels<MainViewModel> { factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        // setup home button for back navigation
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }

        // setup observers for operation live data
        viewmodel.operationState.observe(this, stateObserver)
        viewmodel.operationError.observe(this, errorObserver)
    }

    override fun onDestroy() {
        super.onDestroy()
        // remove observers on activity destroy
        viewmodel.operationState.removeObserver(stateObserver)
        viewmodel.operationError.removeObserver(errorObserver)
    }

    // toggle visibility of progress spinner while async operations are taking place
    private val stateObserver = Observer<Event<Boolean>> {
        when(it.getContentIfNotHandled()){
            true -> {
                progress_circular.show()
                idlingResources.increment()
            }
            false -> {
                progress_circular.hide()
                idlingResources.decrement()
            }
        }
    }

    // display a toast when operation fails
    private val errorObserver = Observer<Event<String>> {
        it.getContentIfNotHandled()?.let { message ->
            displayToast(message)
        }
    }

    //When home button in toolbar is pressed
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

}
