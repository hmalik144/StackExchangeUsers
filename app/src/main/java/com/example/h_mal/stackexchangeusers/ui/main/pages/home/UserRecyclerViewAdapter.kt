package com.example.h_mal.stackexchangeusers.ui.main.pages.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.persistableBundleOf
import androidx.recyclerview.widget.RecyclerView
import com.example.h_mal.stackexchangeusers.R
import com.example.h_mal.stackexchangeusers.data.model.UserItem
import com.example.h_mal.stackexchangeusers.data.room.entities.UserEntity
import com.example.h_mal.stackexchangeusers.utils.loadImage
import com.example.h_mal.stackexchangeusers.utils.navigateTo
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.simple_item.view.*

/**
 * Create a Recyclerview adapter used to display [UserEntity]
 * @param parentView is used for context and navigation
 */
class UserRecyclerViewAdapter (
    val parentView: View
): RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    var list = mutableListOf<UserItem>()

    fun updateList(users: List<UserItem>) {
        list.clear()
        list.addAll(users)
        notifyDataSetChanged()
    }


    // create a viewholder with [R.layout.simple_item]
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater
            .from(parentView.context)
            .inflate(R.layout.simple_item, parent, false)

        return ItemOne(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val view = holder as ItemOne

        list[position].let {
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
                val action = MainFragmentDirections.toUserProfileFragment(list[layoutPosition].userId!!)
                parentView.navigateTo(action)
            }
        }
    }

}

