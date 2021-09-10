package com.example.kldstadium.models

import com.google.firebase.Timestamp
import java.util.*

data class User(
    val id: String? = null,
    val name: String? = null,
    val date: Date? = null,
    val state: Int? = null,
    //val avatar: String? = null,
    val purpose: String? = null
)

