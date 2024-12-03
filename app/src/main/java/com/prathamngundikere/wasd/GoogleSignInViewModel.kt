package com.prathamngundikere.wasd

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class GoogleSignInViewModel(
    private val googleAuthClient: GoogleAuthClient
): ViewModel() {
    private val _isSignedIn = mutableStateOf(
        googleAuthClient.isSignedIn()
    )
    val isSignedIn = _isSignedIn
    init {
        _isSignedIn.value = googleAuthClient.isSignedIn()
    }
    fun signIn(): Boolean {
        var r = false
        viewModelScope.launch {
            r = googleAuthClient.signIn()
        }
        return r
    }
    fun signOut(){
        viewModelScope.launch {
           googleAuthClient.signOut()
            _isSignedIn.value = false
        }
    }
}