package com.prathamngundikere.wasd.ui.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.prathamngundikere.wasd.data.repository.AuthManager

class AuthViewModel(
    private val authManager: AuthManager
): ViewModel() {

}