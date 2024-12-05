package com.prathamngundikere.wasd.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prathamngundikere.wasd.data.model.UserData
import com.prathamngundikere.wasd.data.repository.GoogleAuthRepository
import com.prathamngundikere.wasd.domain.State
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val googleAuthRepository: GoogleAuthRepository,
): ViewModel() {
    private val _userData = MutableStateFlow<UserData?>(null)
    val userData = _userData.asStateFlow()
    private val _state = MutableStateFlow<State>(State.Empty)
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            delay(10)
            getUserData()
        }
    }

    fun getUserData() {
        _state.value = State.Loading
        viewModelScope.launch {
            _userData.value = googleAuthRepository.getUserData()
            _state.value = State.Success
        }
    }
    fun signOut() {
        viewModelScope.launch {
            googleAuthRepository.signOut()
        }
    }
}