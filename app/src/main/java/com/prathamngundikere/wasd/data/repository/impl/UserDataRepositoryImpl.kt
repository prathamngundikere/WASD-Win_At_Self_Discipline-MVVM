package com.prathamngundikere.wasd.data.repository.impl

import android.content.Context
import android.util.Log
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.prathamngundikere.wasd.data.model.UserData
import com.prathamngundikere.wasd.data.repository.UserDataRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext

private val Context.dataStore by preferencesDataStore(
    name = "user_data",
    corruptionHandler = ReplaceFileCorruptionHandler {
        emptyPreferences()
    }
)
class UserDataRepositoryImpl(
    private val context: Context
): UserDataRepository {

    private object PreferencesKeys {
        val UID = stringPreferencesKey("uid")
        val USERNAME = stringPreferencesKey("username")
        val PROFILE_PICTURE_URL = stringPreferencesKey("profilePictureUrl")
        val EMAIL = stringPreferencesKey("email")
        val IS_LOGGED_IN = booleanPreferencesKey("isLoggedIn")
    }
    override suspend fun saveUserData(userData: UserData) {
        try {
            withContext(Dispatchers.IO) {
                context.dataStore.edit { preferences ->
                    preferences[PreferencesKeys.UID] = userData.uid
                    preferences[PreferencesKeys.USERNAME] = userData.username ?: ""
                    preferences[PreferencesKeys.PROFILE_PICTURE_URL] = userData.profilePictureUrl ?: ""
                    preferences[PreferencesKeys.EMAIL] = userData.email
                    Log.d("UserDataRepository", "saveUserData: $userData")
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override suspend fun getUserData(): UserData? {
        return try {
            withContext(Dispatchers.IO) {
                val preferences = context.dataStore.data.first()
                val uid = preferences[PreferencesKeys.UID]
                val username = preferences[PreferencesKeys.USERNAME]
                val profilePictureUrl = preferences[PreferencesKeys.PROFILE_PICTURE_URL]
                val email = preferences[PreferencesKeys.EMAIL]
                Log.d("UserDataRepository", "getUserData: $uid $username $profilePictureUrl $email")
                if (uid != null && email != null) {
                    UserData(uid, username, profilePictureUrl, email)
                } else {
                    null
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    override suspend fun deleteUserData() {
        try {
            withContext(Dispatchers.IO) {
                context.dataStore.edit { preferences ->
                    preferences.remove(PreferencesKeys.UID)
                    preferences.remove(PreferencesKeys.USERNAME)
                    preferences.remove(PreferencesKeys.PROFILE_PICTURE_URL)
                    preferences.remove(PreferencesKeys.EMAIL)
                    preferences.clear()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override suspend fun setLoggedIn(isLoggedIn: Boolean) {
        try {
            withContext(Dispatchers.IO) {
                context.dataStore.edit { preferences ->
                    preferences[PreferencesKeys.IS_LOGGED_IN] = isLoggedIn
                }
            }
            Log.d("UserDataRepository", "setLoggedIn: $isLoggedIn")
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("UserDataRepository", "setLoggedIn: ${e.message}")
        }
    }

    override suspend fun isLoggedIn(): Boolean {
        return try {
            withContext(Dispatchers.IO) {
                val preferences = context.dataStore.data.first()
                Log.d("UserDataRepository", "isLoggedIn: ${preferences[PreferencesKeys.IS_LOGGED_IN]}")
                preferences[PreferencesKeys.IS_LOGGED_IN] == true
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("UserDataRepository", "isLoggedIn: ${e.message}")
            false
        }
    }
}