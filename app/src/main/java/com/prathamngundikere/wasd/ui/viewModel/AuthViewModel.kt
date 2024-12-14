package com.prathamngundikere.wasd.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prathamngundikere.wasd.data.model.User
import com.prathamngundikere.wasd.data.repository.FireStoreRepository
import com.prathamngundikere.wasd.data.repository.GoogleAuthRepository
import com.prathamngundikere.wasd.domain.AuthError
import com.prathamngundikere.wasd.domain.Result
import com.prathamngundikere.wasd.domain.State
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel(
    private val googleAuthRepository: GoogleAuthRepository,
    private val fireStoreRepository: FireStoreRepository
): ViewModel() {

    private val _state = MutableStateFlow<State>(State.Empty)
    val state = _state.asStateFlow()

    fun resetState() {
        _state.value = State.Empty
    }

    fun signIn() {
        _state.value = State.Loading
        viewModelScope.launch{
            val result = googleAuthRepository.signIn()
            _state.value = when(result) {
                is Result.Success -> {
                    if (result.data) {
                        val userData = googleAuthRepository.getUserData()
                        if (userData != null) {
                            fireStoreRepository.insertUser(
                                User(
                                    userId = userData.uid,
                                    userName = userData.username.toString(),
                                    email = userData.email,
                                    lastLogin = System.currentTimeMillis()
                                )
                            )
                        }
                        State.Success
                    } else {
                        _state.value = State.Empty
                        State.Error(AuthError.UnknownError.toString())
                    }
                }

                is Result.Error -> {
                    _state.value = State.Empty
                    State.Error(result.error.toString())
                }
            }
        }
    }
}