package com.example.h_mal.stackexchangeusers.data.room.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.h_mal.stackexchangeusers.data.network.response.User

@Entity
data class UserEntity(
    @PrimaryKey(autoGenerate = false)
    val userId: Int,
    val displayName: String? = null,
    val bronze: Int? = null,
    val silver: Int? = null,
    val gold: Int? = null,
    val reputation: Int? = null,
    val creationDate: Int? = null,
    val profileImage: String? = null
){

    constructor(user: User): this(
        user.userId!!,
        user.displayName,
        user.badgeCounts?.bronze,
        user.badgeCounts?.silver,
        user.badgeCounts?.gold,
        user.reputation,
        user.creationDate,
        user.profileImage
    )
}