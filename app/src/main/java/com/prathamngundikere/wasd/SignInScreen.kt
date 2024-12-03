package com.prathamngundikere.wasd

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun SignIn(
    viewModel: GoogleSignInViewModel
) {
    val isSignIn = viewModel.isSignedIn.value

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        if (isSignIn) {
            OutlinedButton(
                onClick = {
                    viewModel.signOut()
                },
            ) {
                Text(text = "Sign Out")
            }
        } else {
            OutlinedButton(
                onClick = {
                    viewModel.signIn()
                },
            ) {
                Text(text = "Sign In with Google")
            }
        }
    }
}