package com.prathamngundikere.wasd.data.model

data class Reward(
    val rewardId: String = "",
    val title: String = "",
    val description: String = "",
    val requiredPoints: Int = 0, // Points required to claim the reward
    val claimed: Boolean = false,
    val createdAt: Long = 0L, // Timestamp of creation
)
