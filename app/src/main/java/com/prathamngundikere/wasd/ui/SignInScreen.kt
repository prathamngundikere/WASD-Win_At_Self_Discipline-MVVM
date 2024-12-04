package com.prathamngundikere.wasd.ui

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.prathamngundikere.wasd.domain.State
import com.prathamngundikere.wasd.ui.viewModel.AuthViewModel
import com.prathamngundikere.wasd.R
import kotlinx.coroutines.delay

@Composable
fun SignInScreen(
    modifier: Modifier = Modifier,
    viewModel: AuthViewModel,
    navController: NavController
) {
    Log.d("SignInScreen", "SignInScreen: I am Here")
    val uiState = viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = uiState.value) {
        delay(5000)
        if (uiState.value is State.Success) {
            navController.navigate("profile") {
                popUpTo("signIn") {
                    inclusive = true
                }
            }
        }
    }

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
                LinearProgressIndicator()
            }
            State.Empty -> {
                Column(
                    modifier = modifier
                        .fillMaxSize()
                        .padding(horizontal = 20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    OutlinedButton(
                        onClick = {
                            viewModel.signIn()
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.google),
                            contentDescription = "Google",
                            modifier = Modifier
                                .size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Sign-in with Google",
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }
            }
        }
    }
}