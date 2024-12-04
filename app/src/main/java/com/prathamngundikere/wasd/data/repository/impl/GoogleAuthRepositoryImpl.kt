package com.prathamngundikere.wasd.data.repository.impl

import android.content.Context
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.prathamngundikere.wasd.data.model.UserData
import com.prathamngundikere.wasd.data.repository.GoogleAuthRepository
import com.prathamngundikere.wasd.domain.AuthError
import com.prathamngundikere.wasd.domain.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class GoogleAuthRepositoryImpl(
    private val context: Context,
): GoogleAuthRepository {

    private val credentialManager = CredentialManager.create(context)
    private val firebaseAuth = FirebaseAuth.getInstance()

    override suspend fun isSignedIn(): Boolean {
        return try {
            withContext(Dispatchers.IO) {
                firebaseAuth.currentUser != null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    override suspend fun signIn(): Result<Boolean, AuthError> {
        return try {
            withContext(Dispatchers.IO) {
                if (isSignedIn()) {
                    Result.Success(true)
                } else {
                    val result = buildCredentialRequest()
                    Result.Success(handleSignIn(result))
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Result.Error(AuthError.UnknownError)
        }
    }

    private suspend fun handleSignIn(result: GetCredentialResponse): Boolean {
        val credential = result.credential
        if (
            credential is CustomCredential &&
            credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
        ) {
            try {
                val tokenCredential = GoogleIdTokenCredential.createFrom(credential.data)

                val authCredential = GoogleAuthProvider.getCredential(
                    tokenCredential.idToken, null
                )
                val authResult = firebaseAuth.signInWithCredential(authCredential).await()
                return authResult.user != null
            } catch (e: GoogleIdTokenParsingException) {
                e.printStackTrace()
                return false
            }
        } else {
            return false
        }
    }

    private suspend fun buildCredentialRequest(): GetCredentialResponse {
        val request = GetCredentialRequest.Builder()
            .addCredentialOption(
                GetGoogleIdOption.Builder()
                    .setFilterByAuthorizedAccounts(false)
                    .setServerClientId(
                        "563621539089-o9p86b9i8qgfrge1ejibucvsbhlo6q5f.apps.googleusercontent.com"
                    )
                    .setAutoSelectEnabled(false)
                    .build()
            )
            .build()
        return credentialManager.getCredential(
            request = request,
            context = context
        )
    }

    override suspend fun signOut() {
        try {
            withContext(Dispatchers.IO) {
                credentialManager.clearCredentialState(
                    ClearCredentialStateRequest()
                )
            }
            firebaseAuth.signOut()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override suspend fun getUserData(): UserData? {
        var user: UserData = UserData("", "", "", "")
        return try {
            withContext(Dispatchers.IO) {
                val firebaseUser = firebaseAuth.currentUser
                if(firebaseUser != null) {
                    user = UserData(
                        uid = firebaseUser.uid,
                        username = firebaseUser.displayName,
                        profilePictureUrl = firebaseUser.photoUrl?.toString(),
                        email = firebaseUser.email.toString()
                    )
                }
            }
            return user
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}