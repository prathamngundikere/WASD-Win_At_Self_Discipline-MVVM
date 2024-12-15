package com.prathamngundikere.wasd.data.model

import com.google.firebase.firestore.PropertyName

data class Task(
    val taskId: String = "",
    val title: String = "",
    val description: String = "",
    val dueDate: Long = 0L, // Timestamp of due date
    val priority: String = "low", // Priority of the task
    @get:PropertyName("isCompleted")
    @set:PropertyName("isCompleted")
    var isCompleted: Boolean = false,
    val points: Int = 0, // Points rewarded for completing the task
    val createdAt: Long = 0L, // Timestamp of creation
)
