package com.example.h_mal.stackexchangeusers.ui.main.pages.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.h_mal.stackexchangeusers.R
import com.example.h_mal.stackexchangeusers.data.room.entities.UserItem
import com.example.h_mal.stackexchangeusers.utils.loadImage
import com.example.h_mal.stackexchangeusers.utils.navigateTo
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.simple_item.view.*

/**
 * Create a Recyclerview adapter used to display [UserItem]
 * @param parentView is used for context and navigation
 * @param items is the list of users parsed from the database
 */
class UserRecyclerViewAdapter (
    val parentView: View,
    val items: List<UserItem>
): RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    // create a viewholder with [R.layout.simple_item]
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater
            .from(parentView.context)
            .inflate(R.layout.simple_item, parent, false)

        return ItemOne(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val view = holder as ItemOne

        items[position].let {
            view.bindUser(it)
        }
    }

    // bind userItem fields to the views in the layout
    internal inner class ItemOne(
        override val containerView: View
    ) : RecyclerView.ViewHolder(containerView), LayoutContainer {

        fun bindUser(userItem: UserItem){
            itemView.profile_img.loadImage(userItem.profileImage, 48, 48)
            itemView.text1.text = userItem.displayName
            itemView.text2.text = userItem.reputation.toString()

            itemView.setOnClickListener {
                val action =
                    MainFragmentDirections.toUserProfileFragment(items[position].userId!!)
                parentView.navigateTo(action)
            }
        }
    }

}

