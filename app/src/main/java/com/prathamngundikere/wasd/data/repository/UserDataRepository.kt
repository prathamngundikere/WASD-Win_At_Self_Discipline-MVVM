package com.prathamngundikere.wasd.data.repository

import com.prathamngundikere.wasd.data.model.UserData

interface UserDataRepository {
    suspend fun saveUserData(userData: UserData)
    suspend fun getUserData(): UserData?
    suspend fun deleteUserData()
    suspend fun setLoggedIn(isLoggedIn: Boolean)
    suspend fun isLoggedIn(): Boolean
}