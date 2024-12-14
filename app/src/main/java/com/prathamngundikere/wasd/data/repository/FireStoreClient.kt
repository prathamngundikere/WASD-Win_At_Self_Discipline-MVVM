package com.prathamngundikere.wasd.data.repository

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.prathamngundikere.wasd.data.model.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch

class FireStoreClient {

    private val db = FirebaseFirestore.getInstance()
    private val usersCollection = "users"

    fun insertUser(
        user: User
    ): Flow<String?> {
        return callbackFlow {
            db.collection(usersCollection)
                .add(user.toHashMap())
                .addOnSuccessListener {
                    Log.d("FireStoreClient", "insertUser: Success")
                    CoroutineScope(Dispatchers.IO).launch {
                        updateUser(user.copy(userId = it.id)).collect{}
                    }
                    trySend(it.id)
                }
                .addOnFailureListener {e ->
                    Log.e("FireStoreClient", "insertUser: Failed message: ${e.message}")
                    trySend(null)
                }
            awaitClose {}
        }
    }

    fun updateUser(
        user: User
    ): Flow<Boolean> {
        return callbackFlow {
            db.collection(usersCollection)
                .document(user.userId)
                .set(user.toHashMap())
                .addOnSuccessListener {
                    Log.d("FireStoreClient", "UpdateUser: Success ${user.userId}")
                    trySend(true)
                }
                .addOnFailureListener {e ->
                    Log.e("FireStoreClient", "UpdatedUser: Failed message: ${e.message}")
                    trySend(false)
                }
            awaitClose {}
        }
    }

    fun getUser(
        userId: String
    ): Flow<User?> {
        return callbackFlow {
            db.collection(usersCollection)
                .get()
                .addOnSuccessListener { result ->
                    var user: User? = null
                    for (document in result) {
                        if (document.data["userId"] == userId) {
                            user = document.data.toUser()
                            Log.d("FireStoreClient", "getUser: Success ${user.toString()}")
                            trySend(user)
                        }
                    }
                    if (user == null) {
                        Log.d("FireStoreClient", "getUser: Failed")
                        trySend(null)
                    }

                }
                .addOnFailureListener {e ->
                    Log.e("FireStoreClient", "getUser: Failed message: ${e.message}")
                    trySend(null)
                }
            awaitClose {}
        }
    }

    private fun User.toHashMap(): HashMap<String, Any> {
        return hashMapOf(
            "userId" to userId,
            "userName" to userName,
            "email" to email,
            "level" to level,
            "experience" to experience,
            "streak" to streak,
            "lastLogin" to lastLogin
        )
    }

    private fun Map<String, Any>.toUser(): User {
        return User(
            userId = this["userId"] as String,
            userName = this["userName"] as String,
            email = this["email"] as String,
            level = this["level"] as Int,
            experience = this["experience"] as Int,
            streak = this["streak"] as Int,
            lastLogin = this["lastLogin"] as Long
        )
    }
}