package com.example.h_mal.stackexchangeusers.data.network.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class BadgeCounts {
    @SerializedName("bronze")
    @Expose
    var bronze: Int? = null
    @SerializedName("silver")
    @Expose
    var silver: Int? = null
    @SerializedName("gold")
    @Expose
    var gold: Int? = null
}