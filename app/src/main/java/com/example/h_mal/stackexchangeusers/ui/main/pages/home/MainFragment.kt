package com.example.h_mal.stackexchangeusers.ui.main.pages.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.h_mal.stackexchangeusers.R
import com.example.h_mal.stackexchangeusers.application.AppClass.Companion.idlingResources
import com.example.h_mal.stackexchangeusers.data.room.entities.UserItem
import com.example.h_mal.stackexchangeusers.ui.MainViewModel
import com.example.h_mal.stackexchangeusers.utils.onSubmitListener
import kotlinx.android.synthetic.main.main_fragment.*


/**
 * UI for the screen holding the list, search box
 * Results for users will be displayed here
 */
class MainFragment : Fragment() {
    /**
     * get [MainViewModel] from [MainActivity] (the calling activity)
     */
    val viewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        search_bar.apply {
            // use submit listener just to retrieve input upon submission of search view
            onSubmitListener {
                viewModel.getUsers(query.toString().trim())
            }
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.usersLiveData.observe(viewLifecycleOwner, usersObserver)
    }

    override fun onStop() {
        super.onStop()
        viewModel.usersLiveData.removeObserver(usersObserver)
    }

    private val usersObserver = Observer<List<UserItem>> {
        // create adapter the recycler view
        val mAdapter = UserRecyclerViewAdapter(
            requireView(),
            it
        )

        // setup the recyclerview
        recycler_view.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = mAdapter
        }
    }

}
