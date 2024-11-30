package com.prathamngundikere.wasd.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prathamngundikere.wasd.data.repository.ConnectivityObserver
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ConnectivityViewModel(
    connectivityObserver: ConnectivityObserver
): ViewModel()  {
    val isConnected = connectivityObserver
        .isConnected
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            true
        )

    private val _wasDisconnected = MutableStateFlow(false)
    val wasDisconnected = _wasDisconnected.asStateFlow()

    init {
        viewModelScope.launch {
            isConnected.collect { isConnected ->
                if (!isConnected) {
                    _wasDisconnected.value = true
                }
            }
        }
    }

    fun resetWasDisconnected() {
        _wasDisconnected.value = false
    }
}