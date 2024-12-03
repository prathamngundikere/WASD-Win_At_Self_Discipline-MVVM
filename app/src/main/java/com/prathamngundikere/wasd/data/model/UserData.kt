package com.prathamngundikere.wasd.data.model

data class UserData(
    val uid: String,
    val username: String? = "",
    val profilePictureUrl: String? = "",
    val email: String
)
