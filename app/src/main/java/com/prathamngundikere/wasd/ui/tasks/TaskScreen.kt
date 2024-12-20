package com.prathamngundikere.wasd.ui.tasks

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.prathamngundikere.wasd.data.model.Task
import com.prathamngundikere.wasd.domain.State
import java.time.Instant
import java.time.ZoneId

@Composable
fun TaskScreen(
    tasks: List<Task>,
    navController: NavController,
    state: State,
    taskCompleted: (Task) -> Unit = {}
) {
    when(state) {
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
                            navController.navigate("add_task")
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
                        items = tasks,
                        key = {task -> task.taskId}
                    ) {task ->
                        Log.d("TaskScreen", "Task: $task")
                        ListItem(
                            headlineContent = {
                                Text(text = task.title)
                            },
                            supportingContent = {
                                Text(text = task.description)
                            },
                            overlineContent = {
                                Text(
                                    text = task.priority,
                                    color = when (task.priority) {
                                        "low" -> Color.Green
                                        "medium" -> Color.Yellow // Or any color you prefer for medium priority
                                        "high" -> Color.Red
                                        else -> Color.Unspecified // Default color if priority is unknown
                                    }
                                )
                            },
                            trailingContent = {
                                Column {
                                    Checkbox(
                                        checked = task.isCompleted,
                                        onCheckedChange = { checked ->
                                            tasks.map{
                                                if (it.taskId == task.taskId) {
                                                    it.copy(isCompleted = checked)
                                                    taskCompleted(it)
                                                } else {
                                                    it
                                                }
                                            }
                                        }
                                    )
                                    Text(text = Instant.ofEpochMilli(task.dueDate).atZone(ZoneId.systemDefault()).toLocalDate().toString())
                                }
                            },
                            leadingContent = {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.Star, // Or any game-related icon
                                        contentDescription = "Points",
                                        tint = Color.Yellow // Or any game-inspired color
                                    )
                                    Spacer(modifier = Modifier.height(4.dp)) // Add some spacing
                                    Text(
                                        text = "+${task.points}",
                                        fontWeight = FontWeight.Bold, // Or any contrasting color
                                        style = MaterialTheme.typography.headlineMedium
                                    )
                                }
                            },
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
