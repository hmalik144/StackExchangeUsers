package com.example.h_mal.stackexchangeusers.data.model

import androidx.room.PrimaryKey
import com.example.h_mal.stackexchangeusers.data.room.entities.UserEntity

data class UserItem(
    val userId: Int?,
    val displayName: String?,
    val bronze: Int?,
    val silver: Int?,
    val gold: Int?,
    val reputation: Int?,
    val creationDate: Int?,
    val profileImage: String?
){

    constructor(userEntity: UserEntity): this(
        userEntity.userId,
        userEntity.displayName,
        userEntity.bronze,
        userEntity.silver,
        userEntity.gold,
        userEntity.reputation,
        userEntity.creationDate,
        userEntity.profileImage
    )
}