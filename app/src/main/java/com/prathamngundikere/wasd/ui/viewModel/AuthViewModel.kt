package com.prathamngundikere.wasd.ui.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prathamngundikere.wasd.data.repository.AuthManager
import com.prathamngundikere.wasd.domain.AuthError
import com.prathamngundikere.wasd.domain.Result
import com.prathamngundikere.wasd.domain.State
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel(
    private val authManager: AuthManager
): ViewModel() {

    private val _state = MutableStateFlow<State>(State.Empty)
    val state = _state.asStateFlow()

    fun createAccountWithEmailAndPassword(email: String, password: String) {
        _state.value = State.Loading
        viewModelScope.launch {
            val result = authManager.createAccountWithEmailAndPassword(
                email = email,
                password = password
            )
            when(result) {
                is Result.Error -> {
                    _state.value = when(result.error) {
                        AuthError.InvalidEmailAndPassword -> State.Error("Invalid email or password")
                        AuthError.UnknownError -> State.Error("Unknown error")
                    }
                }
                is Result.Success -> {
                    Log.d("AuthViewModel", "createAccountWithEmailAndPassword: ${result.data}")
                    _state.value = State.Success
                }
            }
        }
    }

    fun signInWithEmailAndPassword(email: String, password: String) {
        _state.value = State.Loading
        viewModelScope.launch {
            val result = authManager.signInWithEmailAndPassword(
                email = email,
                password = password
            )
            when(result) {
                is Result.Error -> {
                    _state.value = when(result.error) {
                        AuthError.InvalidEmailAndPassword -> State.Error("Invalid email or password")
                        AuthError.UnknownError -> State.Error("Unknown error")
                    }
                }
                is Result.Success -> {
                    Log.d("AuthViewModel", "signInWithEmailAndPassword: ${result.data}")
                    _state.value = State.Success
                }
            }
        }
    }
}