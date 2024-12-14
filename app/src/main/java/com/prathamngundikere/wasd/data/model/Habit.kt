package com.prathamngundikere.wasd.data.model

data class Habit(
    val habitId: String = "",
    val name: String = "",
    val description: String = "",
    val createdAt: Long = 0L, // Timestamp of creation
    val frequency: Int = 1, // Frequency per day/week/month
    val type: String = "daily", // Type of habit (daily, weekly, monthly)
    val completedCount: Int = 0,
    val streak: Int = 0, // Habit specific streak
    val lastCompleted: Long = 0L // Timestamp of last completion
)
