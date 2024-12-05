package com.prathamngundikere.wasd.ui

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import kotlinx.coroutines.delay

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
                        color = MaterialTheme.colorScheme.background
                    ),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Animation state for the image
                val imageVisible = remember { mutableStateOf(false) }
                val textVisible = remember { mutableStateOf(false) }

                // Trigger animations with delay
                LaunchedEffect(Unit) {
                    imageVisible.value = true
                    delay(300) // Delay before showing the text
                    textVisible.value = true
                }
                AnimatedVisibility(
                    visible = imageVisible.value,
                    enter = scaleIn() + fadeIn(),
                    exit = scaleOut() + fadeOut()
                ) {
                    AsyncImage(
                        model = userData?.profilePictureUrl,
                        contentDescription = "Profile Picture",
                        modifier = Modifier
                            .size(150.dp)
                            .clip(CircleShape)
                            .border(2.dp, Color.Gray, CircleShape)
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                AnimatedVisibility(
                    visible = textVisible.value,
                    enter = slideInVertically() + fadeIn(),
                    exit = slideOutVertically() + fadeOut()
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = userData?.username ?: "",
                            style = MaterialTheme.typography.headlineMedium,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = userData?.email ?: "",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }
                }

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