package com.prathamngundikere.wasd.ui.habit

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.prathamngundikere.wasd.data.model.Habit
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddHabitScreen(
    modifier: Modifier = Modifier,
    onAddHabitClick: (Habit) -> Unit,
    navController: NavController
) {
    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var type by remember { mutableStateOf("daily") } // Default to daily
    var frequency by remember { mutableStateOf(1) }
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Habit Type Selection (Daily, Weekly, Monthly)
        Row(modifier = Modifier.fillMaxWidth()) {
            Button(
                onClick = { type = "daily" },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (type == "daily") MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
                    contentColor = if (type == "daily") MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
                )
            ) {
                Text("Daily")
            }
            Button(
                onClick = { type = "weekly" },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (type == "weekly") MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
                    contentColor = if (type == "weekly") MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
                )
            ) {
                Text("Weekly")
            }
            Button(
                onClick = { type = "monthly" },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (type == "monthly") MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
                    contentColor = if (type == "monthly") MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
                )
            ) {
                Text("Monthly")
            }
        }

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Habit Name") },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(
                onNext = { focusManager.moveFocus(FocusDirection.Down) }
            )
        )

        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Description") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(
                onNext = { focusManager.moveFocus(FocusDirection.Down) }
            )
        )

        // Frequency Input (For daily, weekly, or monthly)
        OutlinedTextField(
            value = frequency.toString(),
            onValueChange = { frequency = it.toIntOrNull() ?: 1 }, // Handle invalid input
            label = { Text("Frequency (per ${type})") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            modifier = Modifier.fillMaxWidth(),
            keyboardActions = KeyboardActions(
                onDone = { focusManager.clearFocus() }
            )
        )

        Button(
            onClick = {
                val newHabit = Habit(
                    habitId = UUID.randomUUID().toString(),
                    name = name,
                    description = description,
                    createdAt = System.currentTimeMillis(),
                    frequency = frequency,
                    type = type
                )
                onAddHabitClick(newHabit)
                navController.navigate("habits") { // Navigate back to habits screen
                    popUpTo("habits") { inclusive = true }
                }
            },
            enabled = name.isNotBlank() && description.isNotBlank()
        ) {
            Text("Add Habit")
        }
    }
}