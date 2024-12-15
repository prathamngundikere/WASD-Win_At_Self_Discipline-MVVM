package com.prathamngundikere.wasd.ui.tasks

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.maxkeppeker.sheets.core.models.base.rememberUseCaseState
import com.maxkeppeler.sheets.calendar.CalendarDialog
import com.maxkeppeler.sheets.calendar.models.CalendarConfig
import com.maxkeppeler.sheets.calendar.models.CalendarSelection
import com.maxkeppeler.sheets.calendar.models.CalendarStyle
import com.prathamngundikere.wasd.data.model.Task
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTaskScreen(
    modifier: Modifier = Modifier,
    onAddTaskClick: (Task) -> Unit,
    navController: NavController
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var priority by remember { mutableStateOf("low") }
    var points by remember { mutableStateOf("") }
    val calendarDialogState = rememberUseCaseState()
    val selectedDate = remember {mutableStateOf<LocalDate?>(LocalDate.now())}
    val formatedDate = selectedDate.value?.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))
    val focusRequester = remember {
        FocusRequester()
    }
    val focusManager = LocalFocusManager.current


    CalendarDialog(
        state = calendarDialogState,
        selection = CalendarSelection.Date(
            selectedDate = selectedDate.value
        ) { newDate ->
            selectedDate.value = newDate
        },
        config = CalendarConfig(
            yearSelection = true,
            monthSelection = true,
            style = CalendarStyle.MONTH
        )
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp), // Add spacing
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Button(
                onClick = { priority = "low" },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (priority == "low") MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
                    contentColor = if (priority == "low") MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
                )
            ) {
                Text("Low")
            }
            Button(
                onClick = { priority = "medium" },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (priority == "medium") MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
                    contentColor = if (priority == "medium") MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
                )
            ) {
                Text("Medium")
            }
            Button(
                onClick = { priority = "high" },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (priority == "high") MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
                    contentColor = if (priority == "high") MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
                )
            ) {
                Text("High")
            }
        }
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    calendarDialogState.show()
                },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = formatedDate ?: selectedDate.value.toString(),
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                color = MaterialTheme.colorScheme.primary
            )
            IconButton(
                onClick = {
                    calendarDialogState.show()
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = null
                )
            }
        }
        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Title") },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = {
                    focusManager.moveFocus(
                        FocusDirection.Down
                    )
                }
            )
        )
        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Description") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = {
                    focusManager.moveFocus(
                        FocusDirection.Down
                    )
                }
            )
        )

        OutlinedTextField(
            value = points.toString(), // Convert to string for display
            onValueChange = { points = it }, // Handle invalid input
            label = { Text("Points") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            modifier = Modifier.fillMaxWidth(),
            keyboardActions = KeyboardActions(
                onDone = {
                    focusManager.clearFocus()
                    val newTask = Task(
                        taskId = UUID.randomUUID().toString(),
                        title = title,
                        description = description,
                        priority = priority,
                        dueDate = selectedDate.value?.atStartOfDay(ZoneId.systemDefault())?.toInstant()?.toEpochMilli() ?: 0L,
                        points = points.toInt(),
                        createdAt = System.currentTimeMillis(),
                        isCompleted = false
                    )
                    onAddTaskClick(newTask)
                    navController.navigate("task") {
                        popUpTo("task") {
                            inclusive = true
                        }
                    }
                }
            )
        )

        Button(
            onClick = {
                val newTask = Task(
                    taskId = UUID.randomUUID().toString(),
                    title = title,
                    description = description,
                    priority = priority,
                    dueDate = selectedDate.value?.atStartOfDay(ZoneId.systemDefault())?.toInstant()?.toEpochMilli() ?: 0L,
                    points = points.toInt(),
                    createdAt = System.currentTimeMillis(),
                    isCompleted = false
                )
                onAddTaskClick(newTask)
                navController.navigate("task") {
                    popUpTo("task") {
                        inclusive = true
                    }
                }
            },
            enabled = title.isNotBlank() && description.isNotBlank()
        ) {
            Text("Add Task")
        }
    }
}