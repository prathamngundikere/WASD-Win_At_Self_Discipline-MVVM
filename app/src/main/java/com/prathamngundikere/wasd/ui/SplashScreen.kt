package com.prathamngundikere.wasd.ui

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    isLoggedIn: Boolean,
    navController: NavController
) {
    Log.d("SplashScreen", "SplashScreen: I am Here")
    LaunchedEffect(key1 = isLoggedIn) {
        delay(10)
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
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    )
}