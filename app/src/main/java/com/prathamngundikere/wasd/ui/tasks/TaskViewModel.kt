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

class TaskViewModel(
    private val fireStoreRepository: FireStoreRepository,
    private val googleAuthRepository: GoogleAuthRepository
): ViewModel() {
    private val _tasks = MutableStateFlow<List<Task>>(emptyList())
    val tasks = _tasks.asStateFlow()

    private val _userData = MutableStateFlow<UserData?>(null)
    init {
        getTasks()
    }
    private fun getTasks() {
        viewModelScope.launch {
            _userData.value = googleAuthRepository.getUserData()
            val result = fireStoreRepository.getTasks(
                _userData.value?.uid ?: ""
            )
            if (result is Result.Success) {
                _tasks.value = result.data
            } else if (result is Result.Error) {
                Log.e("TaskViewModel", "Error fetching tasks")
            }
        }
    }
    fun addTask(task: Task) {
        viewModelScope.launch {
            _userData.value = googleAuthRepository.getUserData()
            Log.d("TaskViewModel", "Adding task: $task to user: ${_userData.value?.uid ?: ""}")
            val result = fireStoreRepository.insertTask(
                userId = _userData.value?.uid ?: "",
                task = task
            )
            if (result is Result.Success) {
                getTasks()
            } else if (result is Result.Error) {
                Log.e("TaskViewModel", "Error adding task")
            }
        }
    }
}