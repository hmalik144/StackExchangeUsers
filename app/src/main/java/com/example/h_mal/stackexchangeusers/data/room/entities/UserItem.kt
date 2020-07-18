package com.example.h_mal.stackexchangeusers.data.room.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class UserItem(
    @PrimaryKey(autoGenerate = false)
    val userId: Int?,
    val displayName: String?,
    val bronze: Int?,
    val silver: Int?,
    val gold: Int?,
    val reputation: Int?,
    val creationDate: Int?,
    val profileImage: String?
)