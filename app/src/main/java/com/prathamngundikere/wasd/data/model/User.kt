package com.prathamngundikere.wasd.data.model

data class User(
    val userId: String = "",
    val userName: String = "",
    val email: String = "",
    val level: Int = 1,
    val experience: Int = 0,
    val streak: Int = 0, // Current Streak in days
    val lastLogin: Long = 0L // Timestamp of last login
)
