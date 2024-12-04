package com.prathamngundikere.wasd.ui.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prathamngundikere.wasd.data.repository.AuthManager
import com.prathamngundikere.wasd.data.repository.GoogleAuthRepository
import com.prathamngundikere.wasd.domain.AuthError
import com.prathamngundikere.wasd.domain.Result
import com.prathamngundikere.wasd.domain.State
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel(
    private val googleAuthRepository: GoogleAuthRepository
): ViewModel() {

    private val _state = MutableStateFlow<State>(State.Empty)
    val state = _state.asStateFlow()

    fun signIn() {
        _state.value = State.Loading
        viewModelScope.launch{
            val result = googleAuthRepository.signIn()
            _state.value = when(result) {
                is Result.Success -> {
                    if (result.data) {
                        State.Success
                    } else {
                        State.Error(AuthError.UnknownError.toString())
                    }
                }

                is Result.Error -> {
                    State.Error(result.error.toString())
                }
            }
        }
    }
}