package com.prathamngundikere.wasd.data.repository

import com.prathamngundikere.wasd.data.model.UserData
import com.prathamngundikere.wasd.domain.AuthError
import com.prathamngundikere.wasd.domain.Result

interface GoogleAuthRepository {
    suspend fun isSignedIn(): Boolean
    suspend fun signIn(): Result<Boolean, AuthError>
    suspend fun signOut()
    suspend fun getUserData(): UserData?
}