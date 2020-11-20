package com.example.h_mal.stackexchangeusers.utils

import java.text.SimpleDateFormat
import java.util.*


fun epochToData(number: Int?): String {
    number ?: return  ""
    return try {
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val netDate = Date(number.toLong() * 1000)
        sdf.format(netDate)
    } catch (e: Exception) {
        ""
    }
}

