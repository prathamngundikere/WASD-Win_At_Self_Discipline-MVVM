package com.prathamngundikere.wasd.ui.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prathamngundikere.wasd.data.repository.GoogleAuthRepository
import kotlinx.coroutines.launch

class SplashScreenViewModel(
    private val googleAuthRepository: GoogleAuthRepository
): ViewModel() {
    private val _isLoggedIn = MutableLiveData<Boolean>()
    val isLoggedIn: LiveData<Boolean> get() = _isLoggedIn
    init {
        checkLoginStatus()
    }
     fun checkLoginStatus() {
        viewModelScope.launch {
            _isLoggedIn.value = googleAuthRepository.isSignedIn()
            Log.d("SplashScreenViewModel", "checkLoginStatus: ${_isLoggedIn.value}")
        }
    }
}