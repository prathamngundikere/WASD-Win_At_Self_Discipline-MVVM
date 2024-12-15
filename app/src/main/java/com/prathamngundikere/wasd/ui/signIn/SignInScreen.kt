package com.prathamngundikere.wasd.ui.signIn

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
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

    val composition = rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.loading))
    val composition2 = rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.signin))
    val progress by animateLottieCompositionAsState(
        composition = composition.value,
        iterations = LottieConstants.IterateForever
    )

    DisposableEffect(Unit) {
        onDispose {
            resetState() // Reset state when this screen is removed
        }
    }

    LaunchedEffect(key1 = state) {
        delay(10)
        if (state is State.Success) {
            navController.navigate("main") {
                popUpTo("signIn") {
                    inclusive = true
                }
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when(state) {
            is State.Error -> {
                val error = (state).message
                Text(text = error)
            }
            State.Loading -> {
                LottieAnimation(
                    composition = composition.value,
                    progress = { progress },
                    modifier = Modifier.size(100.dp)
                )
            }
            State.Success -> {
                LottieAnimation(
                    composition = composition.value,
                    progress = { progress },
                    modifier = Modifier.size(100.dp)
                )
            }
            State.Empty -> {
                Column(
                    modifier = modifier
                        .fillMaxSize()
                        .padding(30.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceEvenly
                ) {
                    LottieAnimation(
                        composition = composition2.value,
                        progress = { progress },
                        modifier = Modifier.size(500.dp)
                    )
                    OutlinedButton(
                        onClick = {
                            Log.d("SignInScreen", "SignInScreen: Clicked")
                            onClick()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
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