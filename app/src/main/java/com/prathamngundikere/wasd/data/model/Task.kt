package com.prathamngundikere.wasd.data.model

data class Task(
    val taskId: String = "",
    val title: String = "",
    val description: String = "",
    val dueDate: Long = 0L, // Timestamp of due date
    val priority: String = "low", // Priority of the task
    val isCompleted: Boolean = false,
    val points: Int = 0, // Points rewarded for completing the task
    val createdAt: Long = 0L, // Timestamp of creation
)
