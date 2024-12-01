package com.prathamngundikere.wasd.data.repository

import com.google.firebase.auth.FirebaseUser
import com.prathamngundikere.wasd.domain.AuthError
import com.prathamngundikere.wasd.domain.Result

interface AuthManager {
    suspend fun createAccountWithEmailAndPassword(email: String, password: String): Result<FirebaseUser, AuthError>
    suspend fun signInWithEmailAndPassword(email: String, password: String): Result<FirebaseUser, AuthError>
    suspend fun sendEmailVerification(): Result<Unit, AuthError>
    suspend fun reloadUser(): Result<FirebaseUser, AuthError>
    suspend fun signOut()
}