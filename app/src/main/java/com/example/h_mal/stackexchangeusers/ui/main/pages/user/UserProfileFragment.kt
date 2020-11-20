package com.example.h_mal.stackexchangeusers.ui.main.pages.user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.example.h_mal.stackexchangeusers.R
import com.example.h_mal.stackexchangeusers.data.room.entities.UserEntity
import com.example.h_mal.stackexchangeusers.ui.MainViewModel
import com.example.h_mal.stackexchangeusers.utils.epochToData
import com.example.h_mal.stackexchangeusers.utils.loadImage
import kotlinx.android.synthetic.main.fragment_user_profile.*

/**
 * Fragment to show the user overview
 */
class UserProfileFragment : Fragment() {
    val viewModel: MainViewModel by activityViewModels()

    var userId: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_user_profile, container, false)
    }

    //Update the data for viewbinding onResume as data would have changed when selecting a new user
    override fun onResume() {
        super.onResume()
        userId = UserProfileFragmentArgs.fromBundle(requireArguments()).userId
        viewModel.getSingleUser(userId).observe(viewLifecycleOwner, singleUserObserver)
    }

    override fun onPause() {
        super.onPause()
        viewModel.getSingleUser(id).removeObserver(singleUserObserver)
    }

    private val singleUserObserver = Observer<UserEntity> {
        username.text = it.displayName
        reputation.text = it.reputation.toString()
        gold_score.text = it.gold.toString()
        silver_score.text = it.silver.toString()
        bronze_score.text = it.bronze.toString()
        date_joined.text = epochToData(it.creationDate)

        imageView.loadImage(it.profileImage)
    }

}
