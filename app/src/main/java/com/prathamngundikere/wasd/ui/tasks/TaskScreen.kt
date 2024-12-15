package com.prathamngundikere.wasd.ui.tasks

import androidx.compose.foundation.clickable
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
import androidx.compose.ui.platform.LocalContext
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
    state: State
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
                        ListItem(
                            headlineContent = {
                                Text(text = task.title)
                            },
                            supportingContent = {
                                Text(text = task.description)
                            },
                            overlineContent = {
                                Text(text = task.priority)
                            },
                            trailingContent = {
                                Column {
                                    Checkbox(
                                        checked = task.isCompleted,
                                        onCheckedChange = { checked ->

                                        }
                                    )
                                    Text(text = Instant.ofEpochMilli(task.dueDate).atZone(ZoneId.systemDefault()).toLocalDate().toString())
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {}
                        )
                        HorizontalDivider()
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun TaskPrev() {
    TaskScreen(
        tasks = listOf<Task>(
            Task(
                taskId = "1",
                title = "Task 1",
                description = "Description 1",
                createdAt = System.currentTimeMillis()
            ),
            Task(
                taskId = "2",
                title = "Task 2",
                description = "Description 3",
                createdAt = System.currentTimeMillis()
            ),
            Task(
                taskId = "3",
                title = "Task 3",
                description = "Description 3",
                createdAt = System.currentTimeMillis()
            )
        ),
        navController = NavController(LocalContext.current),
        state = State.Success
    )
}