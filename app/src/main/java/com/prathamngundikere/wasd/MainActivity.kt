package com.prathamngundikere.wasd

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.prathamngundikere.wasd.data.model.BottomNavigationItem
import com.prathamngundikere.wasd.data.repository.impl.ConnectivityObserverImpl
import com.prathamngundikere.wasd.data.repository.impl.FireStoreRepositoryImpl
import com.prathamngundikere.wasd.data.repository.impl.GoogleAuthRepositoryImpl
import com.prathamngundikere.wasd.ui.AppNavigation
import com.prathamngundikere.wasd.ui.profile.ProfileScreen
import com.prathamngundikere.wasd.ui.signIn.SignInScreen
import com.prathamngundikere.wasd.ui.splash.SplashScreen
import com.prathamngundikere.wasd.ui.tasks.AddTaskScreen
import com.prathamngundikere.wasd.ui.tasks.TaskScreen
import com.prathamngundikere.wasd.ui.theme.WASDTheme
import com.prathamngundikere.wasd.ui.signIn.AuthViewModel
import com.prathamngundikere.wasd.ui.viewModel.ConnectivityViewModel
import com.prathamngundikere.wasd.ui.profile.ProfileViewModel
import com.prathamngundikere.wasd.ui.splash.SplashScreenViewModel
import com.prathamngundikere.wasd.ui.tasks.TaskViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
        enableEdgeToEdge()
        setContent {
            WASDTheme {
                AppNavigation(
                    context = applicationContext
                )
            }
        }
    }
}