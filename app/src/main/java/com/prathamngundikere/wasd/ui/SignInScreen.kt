package com.prathamngundikere.wasd.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.prathamngundikere.wasd.domain.State
import com.prathamngundikere.wasd.ui.viewModel.AuthViewModel

@Composable
fun SignInScreen(
    modifier: Modifier = Modifier,
    viewModel: AuthViewModel
) {
    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val uiState = viewModel.state.collectAsStateWithLifecycle()

    Column(
        modifier = modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when(uiState.value) {
            is State.Error -> {
                val error = (uiState.value as State.Error).message
                Text(text = error)
            }
            State.Loading -> {
                CircularProgressIndicator()
            }
            State.Success -> {
                Text(text = "Success")
            }
            State.Empty -> {
                TextField(
                    modifier = modifier.fillMaxWidth(),
                    value = email.value,
                    onValueChange = { email.value = it },
                    label = { Text("Email") }
                )
                TextField(
                    modifier = modifier.fillMaxWidth(),
                    value = password.value,
                    onValueChange = { password.value = it },
                    label = { Text("Password") }
                )
                Button(
                    modifier = modifier.fillMaxWidth(),
                    onClick = {
                        viewModel.signInWithEmailAndPassword(email.value, password.value)
                    }
                ) {
                    Text("Sign In")
                }
            }
        }
    }
}