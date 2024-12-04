package com.prathamngundikere.wasd.ui

import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.prathamngundikere.wasd.domain.State
import com.prathamngundikere.wasd.ui.viewModel.ProfileViewModel

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    viewModel: ProfileViewModel,
    navController: NavController
) {
    Log.d("ProfileScreen", "ProfileScreen: I am Here")
    val userData = viewModel.userData.collectAsStateWithLifecycle().value
    val state = viewModel.state.collectAsStateWithLifecycle().value

    // Fetch user data when the screen is first launched
    LaunchedEffect(key1 = true) { // Avoid fetching multiple times
            viewModel.getUserData()
    }

    when (state) {
        is State.Error -> {
            val error = state.message
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = error)
            }
        }

        State.Loading -> {
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        State.Success -> {
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                AsyncImage(
                    model = userData?.profilePictureUrl,
                    contentDescription = "Profile Picture",
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .border(2.dp, Color.Gray, CircleShape)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = userData?.username ?: "",
                    style = MaterialTheme.typography.headlineMedium
                )
                Text(
                    text = userData?.email ?: "",
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(32.dp))
                Button(onClick = {
                    viewModel.signOut()
                    navController.navigate("signIn") {
                        popUpTo("profile") { inclusive = true }
                    }
                }) {
                    Text("Sign Out")
                }
            }
        }

        State.Empty -> {
            // Show a loading indicator until the data is fetched
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
    }
}