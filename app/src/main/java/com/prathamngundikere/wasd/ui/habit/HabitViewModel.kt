package com.prathamngundikere.wasd.ui.habit

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prathamngundikere.wasd.data.model.Habit
import com.prathamngundikere.wasd.data.model.UserData
import com.prathamngundikere.wasd.data.repository.FireStoreRepository
import com.prathamngundikere.wasd.data.repository.GoogleAuthRepository
import com.prathamngundikere.wasd.domain.Result
import com.prathamngundikere.wasd.domain.State
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

class HabitViewModel (
    private val fireStoreRepository: FireStoreRepository,
    private val googleAuthRepository: GoogleAuthRepository
): ViewModel() {
    private val _state = MutableStateFlow<State>(State.Empty)
    val state = _state.asStateFlow()
    private val _userData = MutableStateFlow<UserData?>(null)
    private val _habits = MutableStateFlow<List<Habit>>(emptyList())
    val habits: StateFlow<List<Habit>> = _habits
    init {
        viewModelScope.launch {
            getHabits()
        }
    }
    fun getHabits() {
        viewModelScope.launch {
            _state.value = State.Loading
            val userId = googleAuthRepository.getUserData()?.uid ?: ""
            if (userId.isEmpty()) {
                _state.value = State.Error("User ID is empty")
                return@launch
            }
            val result = fireStoreRepository.getHabits(userId)
            if (result is Result.Success) {
                _habits.value = result.data
                _state.value = State.Success
            } else if (result is Result.Error) {
                _state.value = State.Error(result.error.toString())
            }
        }
    }
    fun addHabit(habit: Habit) {
        viewModelScope.launch {
            _state.value = State.Loading
            _userData.value = googleAuthRepository.getUserData()
            val result = fireStoreRepository.insertHabit(
                userId = _userData.value?.uid ?: "",
                habit = habit
            )
            if (result is Result.Success) {
                getHabits()
                _state.value = State.Success
                } else if (result is Result.Error) {
                _state.value = State.Success
            }
        }
    }

    fun habitCompleted(habit: Habit) {
        viewModelScope.launch {
            _state.value = State.Loading
            _userData.value = googleAuthRepository.getUserData()
            val today = LocalDate.now()
            val lastCompletedDate = Instant.ofEpochMilli(habit.lastCompleted).atZone(ZoneId.systemDefault()).toLocalDate()

            val updatedHabit = if (habit.type == "daily" && lastCompletedDate.isEqual(today)) {
                // Habit already completed today, reset completedCount and streak
                habit.copy(completedCount = 0, streak = 0, lastCompleted = 0L)
            } else {
                // Increment completedCount and streak if applicable
                val updatedStreak = if (habit.type == "daily" && lastCompletedDate.plusDays(1).isEqual(today)) {
                    habit.streak + 1 // Increment streak if completed on consecutive days
                } else {
                    1 // Reset streak if not completed on consecutive days
                }
                habit.copy(completedCount = habit.completedCount + 1, streak = updatedStreak, lastCompleted = System.currentTimeMillis())
            }

            val result = fireStoreRepository.updateHabit(userId = _userData.value?.uid ?: "", habit = updatedHabit)
            if (result is Result.Success) {
                getHabits() // Refresh habits list
                _state.value = State.Success
            } else if (result is Result.Error) {
                Log.e("HabitViewModel", "Error updating habit: $result")
                _state.value = State.Error("Update failed")
            }
        }
    }
}