package com.example.h_mal.stackexchangeusers.utils

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import com.example.h_mal.stackexchangeusers.R
import com.squareup.picasso.Picasso

fun View.show() {
    this.visibility = View.VISIBLE
}

fun View.hide() {
    this.visibility = View.GONE
}

fun Context.displayToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}

fun View.navigateTo(navDirections: NavDirections) {
    Navigation.findNavController(this).navigate(navDirections)
}

fun ImageView.loadImage(url: String?){
    Picasso.get()
        .load(url)
        .placeholder(R.mipmap.ic_launcher)
        .into(this)
}

fun ImageView.loadImage(url: String?, height: Int, width: Int){
    Picasso.get()
        .load(url)
        .resize(width, height)
        .centerCrop()
        .placeholder(R.mipmap.ic_launcher)
        .into(this)
}

fun SearchView.onSubmitListener(searchSubmit: (String) -> Unit) {
    this.setOnQueryTextListener(object :
        SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(s: String): Boolean {
            searchSubmit.invoke(s)
            return true
        }

        override fun onQueryTextChange(s: String): Boolean {
            return true
        }
    })
}