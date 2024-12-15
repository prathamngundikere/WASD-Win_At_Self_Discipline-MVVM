package com.prathamngundikere.wasd.ui.tasks

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.prathamngundikere.wasd.data.model.Task
import java.util.UUID

@Composable
fun AddTaskScreen(
    modifier: Modifier = Modifier,
    onAddTaskClick: (Task) -> Unit,
    navController: NavController
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Title") }
        )
        TextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Description") }
        )
        Button(onClick = {
            val newTask = Task(
                taskId = UUID.randomUUID().toString(),
                title = title,
                description = description,
                createdAt = System.currentTimeMillis()
                // ... (Other task properties)
            )
            onAddTaskClick(newTask)
            navController.navigate("task") {
                popUpTo("task") {
                    inclusive = true
                }
            }
        }) {
            Text("Add Task")
        }
    }
}