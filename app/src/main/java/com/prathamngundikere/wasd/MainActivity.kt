package com.prathamngundikere.wasd

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.prathamngundikere.wasd.data.repository.impl.ConnectivityObserverImpl
import com.prathamngundikere.wasd.data.repository.impl.FirebaseAuthManager
import com.prathamngundikere.wasd.data.repository.impl.UserDataRepositoryImpl
import com.prathamngundikere.wasd.ui.ProfileScreen
import com.prathamngundikere.wasd.ui.SignInScreen
import com.prathamngundikere.wasd.ui.SplashScreen
import com.prathamngundikere.wasd.ui.theme.WASDTheme
import com.prathamngundikere.wasd.ui.viewModel.AuthViewModel
import com.prathamngundikere.wasd.ui.viewModel.ConnectivityViewModel
import com.prathamngundikere.wasd.ui.viewModel.ProfileViewModel
import com.prathamngundikere.wasd.ui.viewModel.SplashScreenViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val googleAuthClient = GoogleAuthClient(applicationContext)
        setContent {
            WASDTheme {
                val navController = rememberNavController()
                val viewModel = viewModel<ConnectivityViewModel> {
                    ConnectivityViewModel(
                        connectivityObserver = ConnectivityObserverImpl(
                            context = applicationContext
                        )
                    )
                }

                val authViewModel = viewModel<AuthViewModel> {
                    AuthViewModel(
                        authManager = FirebaseAuthManager(
                            userDataRepository = UserDataRepositoryImpl(
                                context = applicationContext
                            )
                        )
                    )
                }
                val profileViewModel = viewModel<ProfileViewModel> {
                    ProfileViewModel(
                        userDataRepository = UserDataRepositoryImpl(
                            context = applicationContext
                        )
                    )
                }
                val splashScreenViewModel = viewModel<SplashScreenViewModel> {
                    SplashScreenViewModel(
                        userDataRepository = UserDataRepositoryImpl(
                            context = applicationContext
                        )
                    )
                }

                val googleSignInViewModel = viewModel<GoogleSignInViewModel> {
                    GoogleSignInViewModel(
                        googleAuthClient = googleAuthClient
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

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    snackbarHost = {
                        SnackbarHost(hostState = snackBarHostState)
                    }
                ) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        contentAlignment = Alignment.Center
                    ) {
                        SignIn(
                            viewModel = googleSignInViewModel
                        )
                        /*NavHost(
                            navController = navController,
                            startDestination = "splash"
                        ) {
                            composable("splash") {
                                SplashScreen(
                                    viewModel = splashScreenViewModel,
                                    navController = navController
                                )
                            }
                            composable("signIn") {
                                SignInScreen(
                                    viewModel = authViewModel,
                                    navController = navController
                                )
                            }
                            composable("profile") {
                                ProfileScreen(
                                    viewModel = profileViewModel
                                )
                            }
                        }*/
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    WASDTheme {
        Greeting("Android")
    }
}