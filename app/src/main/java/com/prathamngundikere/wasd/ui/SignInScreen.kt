package com.prathamngundikere.wasd.ui

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.prathamngundikere.wasd.domain.State
import com.prathamngundikere.wasd.R
import kotlinx.coroutines.delay

@Composable
fun SignInScreen(
    modifier: Modifier = Modifier,
    state: State,
    onClick: () -> Unit,
    navController: NavController,
    resetState: () -> Unit
) {
    Log.d("SignInScreen", "SignInScreen: I am Here")

    DisposableEffect(Unit) {
        onDispose {
            resetState() // Reset state when this screen is removed
        }
    }

    LaunchedEffect(key1 = state) {
        delay(5000)
        if (state is State.Success) {
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
        when(state) {
            is State.Error -> {
                val error = (state).message
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
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    OutlinedButton(
                        onClick = {
                            Log.d("SignInScreen", "SignInScreen: Clicked")
                            onClick()
                        },
                        modifier = Modifier
                            .padding(vertical = 10.dp, horizontal = 20.dp),
                        shape = RoundedCornerShape(2.dp),
                        border = BorderStroke(1.dp, Color.Black),
                        colors = ButtonColors(
                            containerColor = Color.White,
                            contentColor = Color.DarkGray,
                            disabledContainerColor = Color.Gray,
                            disabledContentColor = Color.White
                        )
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.google),
                            contentDescription = "Google",
                            modifier = Modifier
                                .size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "Sign in with Google",
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun SignInScreenPrev() {
    SignInScreen(
        state = State.Empty,
        onClick = {},
        navController = NavController(LocalContext.current),
        resetState = {}
    )
}