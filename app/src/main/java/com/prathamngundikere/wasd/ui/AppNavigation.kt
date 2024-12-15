package com.prathamngundikere.wasd.ui

import android.content.Context
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.prathamngundikere.wasd.data.repository.impl.ConnectivityObserverImpl
import com.prathamngundikere.wasd.data.repository.impl.FireStoreRepositoryImpl
import com.prathamngundikere.wasd.data.repository.impl.GoogleAuthRepositoryImpl
import com.prathamngundikere.wasd.ui.signIn.AuthViewModel
import com.prathamngundikere.wasd.ui.signIn.SignInScreen
import com.prathamngundikere.wasd.ui.splash.SplashScreen
import com.prathamngundikere.wasd.ui.splash.SplashScreenViewModel
import com.prathamngundikere.wasd.ui.viewModel.ConnectivityViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun AppNavigation(
    context: Context
) {
    val navController = rememberNavController()
    val viewModel = viewModel<ConnectivityViewModel> {
        ConnectivityViewModel(
            connectivityObserver = ConnectivityObserverImpl(
                context = context
            )
        )
    }
    val isConnected = viewModel.isConnected.collectAsStateWithLifecycle()
    val coroutineScope = rememberCoroutineScope()
    val snackBarHostState = remember { SnackbarHostState() }

    LaunchedEffect(isConnected.value) {
        coroutineScope.launch {
            delay(5000)
        }
        if (!isConnected.value) {
            coroutineScope.launch {
                snackBarHostState.showSnackbar(
                    message = "No Internet Connection",
                    duration = SnackbarDuration.Short
                )
            }
        } else {
            if (viewModel.wasDisconnected.value) {
                coroutineScope.launch{
                    snackBarHostState.showSnackbar(
                        message = "Internet Connection Restored",
                        duration = SnackbarDuration.Short
                    )
                }
                viewModel.resetWasDisconnected()
            }
        }
    }
    val fireStoreRepository = FireStoreRepositoryImpl(
        context = context
    )
    val googleAuthRepository = GoogleAuthRepositoryImpl(
        context = context,
        fireStoreRepository = fireStoreRepository
    )
    val splashScreenViewModel = viewModel<SplashScreenViewModel> {
        SplashScreenViewModel(
            googleAuthRepository = googleAuthRepository
        )
    }
    val authViewModel = viewModel<AuthViewModel> {
        AuthViewModel(
            googleAuthRepository = googleAuthRepository
        )
    }
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = {
            SnackbarHost(hostState = snackBarHostState)
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            NavHost(
                navController = navController,
                startDestination = "splash"
            ) {
                composable("splash") {
                    SplashScreen(
                        isLoggedIn = splashScreenViewModel.isLoggedIn.observeAsState(
                            initial = false
                        ).value,
                        navController = navController
                    )
                }
                composable("signIn") {
                    SignInScreen(
                        state = authViewModel.state.collectAsStateWithLifecycle().value,
                        onClick = authViewModel::signIn,
                        navController = navController,
                        resetState = authViewModel::resetState
                    )
                }
                composable("main") {
                    MainNavigation(
                        context = context
                    )
                }
            }
        }
    }
}