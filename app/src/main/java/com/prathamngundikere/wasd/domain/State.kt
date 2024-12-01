package com.prathamngundikere.wasd.domain

sealed class State {
    object Success: State()
    object Loading: State()
    object Empty: State()
    data class Error(val message: String): State()
}