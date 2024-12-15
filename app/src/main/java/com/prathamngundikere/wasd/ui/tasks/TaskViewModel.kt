package com.prathamngundikere.wasd.ui.tasks

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prathamngundikere.wasd.data.model.Task
import com.prathamngundikere.wasd.data.model.UserData
import com.prathamngundikere.wasd.data.repository.FireStoreRepository
import com.prathamngundikere.wasd.data.repository.GoogleAuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import com.prathamngundikere.wasd.domain.Result
import com.prathamngundikere.wasd.domain.State
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.StateFlow

class TaskViewModel(
    private val fireStoreRepository: FireStoreRepository,
    private val googleAuthRepository: GoogleAuthRepository
): ViewModel() {

    private val _state = MutableStateFlow<State>(State.Empty)
    val state = _state.asStateFlow()

    private val _userData = MutableStateFlow<UserData?>(null)
    init {
        viewModelScope.launch{
            getTasks()
        }
    }
    private val _tasks = MutableStateFlow<List<Task>>(emptyList())
    val tasks: StateFlow<List<Task>> = _tasks

    fun getTasks() {
        viewModelScope.launch {
            _state.value = State.Loading
            val userId = googleAuthRepository.getUserData()?.uid ?: ""
            if (userId.isEmpty()) {
                Log.e("TaskViewModel", "User ID is empty")
                _state.value = State.Error("User ID is empty")
                return@launch
            }

            val result = fireStoreRepository.getTasks(userId)
            if (result is Result.Success) {
                Log.d("TaskViewModel", "Fetched tasks: ${result.data}")
                _tasks.value = result.data // Emit new task list
                _state.value = State.Success
            } else if (result is Result.Error) {
                Log.e("TaskViewModel", "Error fetching tasks: ${result.error}")
                _state.value = State.Error("Error fetching tasks")
            }
        }
    }
    fun addTask(task: Task) {
        viewModelScope.launch {
            _state.value = State.Loading
            _userData.value = googleAuthRepository.getUserData()
            Log.d("TaskViewModel", "Adding task: $task to user: ${_userData.value?.uid ?: ""}")
            val result = fireStoreRepository.insertTask(
                userId = _userData.value?.uid ?: "",
                task = task
            )
            if (result is Result.Success) {
                getTasks()
                _state.value = State.Success
            } else if (result is Result.Error) {
                Log.e("TaskViewModel", "Error adding task")
                _state.value = State.Success
            }
        }
    }
    fun taskCompleted(task: Task) {
        viewModelScope.launch {
            _state.value = State.Loading
            _userData.value = googleAuthRepository.getUserData()
            val result = fireStoreRepository.updateTask(
                userId = _userData.value?.uid ?: "",
                task = task.copy(isCompleted = !task.isCompleted) // Update isCompleted here
            )
            if (result is Result.Success) {
                getTasks() // Refresh tasks list
                _state.value = State.Success
            } else if (result is Result.Error) {
                Log.e("TaskViewModel", "Error updating task: ${result.error.toString()}")
                _state.value = State.Error("Update failed")
                // Consider displaying an error message to the user
            }
        }
    }
}