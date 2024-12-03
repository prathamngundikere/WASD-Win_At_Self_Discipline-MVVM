package com.prathamngundikere.wasd.data.repository.impl

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseUser
import com.prathamngundikere.wasd.data.model.UserData
import com.prathamngundikere.wasd.data.repository.AuthManager
import com.prathamngundikere.wasd.data.repository.UserDataRepository
import com.prathamngundikere.wasd.domain.AuthError
import com.prathamngundikere.wasd.domain.Result
import kotlinx.coroutines.tasks.await

class FirebaseAuthManager(
    val userDataRepository: UserDataRepository
): AuthManager {

    private val auth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    override suspend fun createAccountWithEmailAndPassword(
        email: String,
        password: String
    ): Result<FirebaseUser, AuthError> {
        return try {
            auth.createUserWithEmailAndPassword(email, password).await()
            val user = auth.currentUser
            if (user != null) {
                userDataRepository.saveUserData(
                    UserData(
                        uid = user.uid,
                        username = user.displayName,
                        profilePictureUrl = user.photoUrl?.toString(),
                        email = user.email ?: ""
                    )
                )
                userDataRepository.setLoggedIn(true)
                Log.d("AuthManager", "createAccountWithEmailAndPassword: $user")
            }
            Log.d("AuthManager", "createAccountWithEmailAndPassword: $user")
            if (user != null)
                Result.Success(user)
            else
                Result.Error(AuthError.UnknownError)
        } catch (e: Exception) {
            Log.e("AuthManager", "createAccountWithEmailAndPassword: ${e.message}")
            handleAuthError(e)
        }
    }

    override suspend fun signInWithEmailAndPassword(
        email: String,
        password: String
    ): Result<FirebaseUser, AuthError> {
        return try {
            auth.signInWithEmailAndPassword(email, password).await()
            val user = auth.currentUser
            if (user != null) {
                userDataRepository.saveUserData(
                    UserData(
                        uid = user.uid,
                        username = user.displayName,
                        profilePictureUrl = user.photoUrl?.toString(),
                        email = user.email ?: ""
                    )
                )
                userDataRepository.setLoggedIn(true)
                Log.d("AuthManager", "signInWithEmailAndPassword: $user")
            }
            Log.d("AuthManager", "signInWithEmailAndPassword: $user")
            if (user != null)
                Result.Success(user)
            else
                Result.Error(AuthError.UnknownError)
        } catch (e: Exception) {
            Log.e("AuthManager", "signInWithEmailAndPassword: ${e.message}")
            handleAuthError(e)
        }
    }

    override suspend fun sendEmailVerification(): Result<Unit, AuthError> {
        val user = auth.currentUser
        return try {
            user?.sendEmailVerification()?.await()
            Result.Success(Unit)
        } catch (e: Exception) {
            Log.e("AuthManager", "sendEmailVerification: ${e.message}")
            Result.Error(AuthError.UnknownError)
        }
    }

    override suspend fun reloadUser(): Result<FirebaseUser, AuthError> {
        return try {
            val user = auth.currentUser
            user?.reload()?.await()
            if (user != null) {
                userDataRepository.saveUserData(
                    UserData(
                        uid = user.uid,
                        username = user.displayName,
                        profilePictureUrl = user.photoUrl?.toString(),
                        email = user.email ?: ""
                    )
                )
                userDataRepository.setLoggedIn(true)
            }
            if (user != null)
                Result.Success(user)
            else
                Result.Error(AuthError.UnknownError)
        } catch (e: Exception) {
            handleAuthError(e)
        }
    }

    override suspend fun signOut() {
        userDataRepository.deleteUserData()
        userDataRepository.setLoggedIn(false)
        auth.signOut()
    }

    private fun handleAuthError(e: Exception): Result<FirebaseUser, AuthError> {
        return when(e) {
            is FirebaseAuthInvalidUserException,
                is FirebaseAuthInvalidCredentialsException -> {
                    Result.Error(AuthError.InvalidEmailAndPassword)
                }
                else -> {
                    Result.Error(AuthError.UnknownError)
                }
        }
    }
}