package com.prathamngundikere.wasd.ui.tasks

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.prathamngundikere.wasd.data.model.Task

@Composable
fun TaskScreen(
    tasks: List<Task>,
    navController: NavController
) {
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
            items(tasks.size) { index ->
                TaskItem(task = tasks[index])
            }
        }
    }
}

@Composable
fun TaskItem(
    task: Task
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Text(
            text = task.title,
            style = MaterialTheme.typography.headlineMedium
        )
        Text(
            text = task.description,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}