package com.prathamngundikere.wasd.ui

import android.util.Log
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.prathamngundikere.wasd.data.model.UserData
import com.prathamngundikere.wasd.domain.State

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    state: State,
    userData: UserData?,
    signOut: () -> Unit,
    navController: NavController
) {
    Log.d("ProfileScreen", "ProfileScreen: I am Here")
    Log.d("ProfileScreen", "state: $state  userData: $userData")
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
                    .background(
                        color = Color.White
                    ),
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
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color.Black
                )
                Text(
                    text = userData?.email ?: "",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(32.dp))
                Button(
                    onClick = {
                    signOut()
                        navController.navigate("signIn") {
                        popUpTo("profile") { inclusive = true }
                        }
                    },
                    shape = RoundedCornerShape(5.dp),
                    colors = ButtonColors(
                        containerColor = Color.Red,
                        contentColor = Color.White,
                        disabledContainerColor = Color.Gray,
                        disabledContentColor = Color.Black
                    )
                ) {
                    Text(
                        text = "Sign Out",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold
                    )
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

@Preview
@Composable
private fun ProfileScreenPrev() {
    ProfileScreen(
        state = State.Success,
        userData = UserData(
            username = "Pratham",
            email = "john.mckinley@examplepetstore.com",
            profilePictureUrl = "",
            uid = "1234567890"
        ),
        signOut = {},
        navController = NavController(LocalContext.current)
    )
}