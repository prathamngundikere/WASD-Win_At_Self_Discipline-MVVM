package com.prathamngundikere.wasd.ui.habit

import android.util.Log
import androidx.compose.animation.core.copy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.prathamngundikere.wasd.domain.State
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.prathamngundikere.wasd.data.model.Habit

@Composable
fun HabitScreen(
    habits: List<Habit>,
    navController: NavController,
    state: State,
    habitCompleted: (Habit) -> Unit = {}
) {
    when (state) {
        State.Empty -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
        is State.Error -> {
            val error = state.message
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = error)
            }
        }
        State.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
        State.Success -> {
            Scaffold(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                floatingActionButton = {
                    FloatingActionButton(
                        onClick = {
                            // Navigate to add habit screen
                            navController.navigate("add_habit")
                        // Assuming you have a route for adding habits
                        }
                    ) {
                        Text(text = "+")
                    }
                }
            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(it)
                ) {
                    items(
                        items = habits,
                        key = { habit -> habit.habitId }
                    ) { habit ->
                        Log.d("HabitScreen", "Habit: $habit")
                        ListItem(
                            headlineContent = {
                                Text(text = habit.name)
                            },
                            supportingContent = {
                                Text(text = habit.description)
                            },
                            overlineContent = {
                                Text(text = habit.type) // Display habit type (daily, weekly, etc.)
                            },
                            trailingContent = {
                                Column {
                                    Checkbox(
                                        checked = habit.completedCount > 0, // Assuming completedCount > 0 means completed for the day/week/month
                                        onCheckedChange = { checked ->
                                            habits.map {
                                                if (it.habitId == habit.habitId) {
                                                    // Assuming completedCount > 0 means completed for the day/week/month
                                                    val updatedCompletedCount =
                                                        if (checked) habit.completedCount + 1 else 0
                                                    it.copy(completedCount = updatedCompletedCount)
                                                    habitCompleted(it) // Call habitCompleted in ViewModel
                                                } else {
                                                    it
                                                }
                                            }
                                        }
                                    )
                                    // You might want to display last completed date or other relevant information here
                                }
                            },
                            leadingContent = {
                                Text(
                                    text = "\uD83D\uDD25 ${habit.streak}",
                                    fontSize = 28.sp
                                )
                            },
                            // You can customize leading content for habits if needed
                            modifier = Modifier
                                .fillMaxWidth()
                        )
                        HorizontalDivider()
                    }
                }
            }
        }
    }
}