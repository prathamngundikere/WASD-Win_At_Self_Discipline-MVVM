package com.prathamngundikere.wasd.ui

import android.util.Log
import android.window.SplashScreen
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.prathamngundikere.wasd.ui.viewModel.SplashScreenViewModel
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    modifier: Modifier = Modifier,
    viewModel: SplashScreenViewModel,
    navController: NavController
) {
    val isLoggedIn = viewModel.isLoggedIn.value
    LaunchedEffect(key1 = true) {
        delay(2000)
        viewModel.checkLoginStatus()
        Log.d("SplashScreen", "isLoggedIn: $isLoggedIn")
        if (isLoggedIn == true) {
            navController.navigate("profile") {
                popUpTo("splash") {
                    inclusive = true
                }
            }
        } else {
            navController.navigate("signIn") {
                popUpTo("splash") {
                    inclusive = true
                }
            }
        }
    }
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}