package com.prathamngundikere.wasd.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prathamngundikere.wasd.data.model.User
import com.prathamngundikere.wasd.data.repository.FireStoreRepository
import com.prathamngundikere.wasd.data.repository.GoogleAuthRepository
import com.prathamngundikere.wasd.domain.Result
import com.prathamngundikere.wasd.domain.State
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel(
    private val fireStoreRepository: FireStoreRepository,
    private val googleAuthRepository: GoogleAuthRepository
): ViewModel() {
    private val _user = MutableLiveData<User?>()
    val user: LiveData<User?> get() = _user

    private val _state = MutableStateFlow<State>(State.Empty)
    val state = _state.asStateFlow()

    private val _progress = MutableLiveData<Pair<Int, Int>>() // (currentExperience, maxExperience)
    val progress: LiveData<Pair<Int, Int>> get() = _progress

    private val _levelUp = MutableLiveData<Boolean>() // Trigger for level up animation
    val levelUp: LiveData<Boolean> get() = _levelUp

    fun fetchUser() {
        viewModelScope.launch {
            _state.value = State.Loading
            val userId = googleAuthRepository.getUserData()?.uid ?: ""
            if (userId.isEmpty()) {
                _state.value = State.Error("User ID is empty")
                return@launch
            }
            val result = fireStoreRepository.getUser(userId)
            if (result is Result.Success) {
                _user.postValue(result.data)
                calculateProgress(user.value!!)
                _state.value = State.Success
            } else if (result is Result.Error) {
                _state.value = State.Error("Error fetching user")
            }
        }
    }
    private fun updateStreak(user: User): User {
        val now = System.currentTimeMillis()
        val oneDayMillis = 24 * 60 * 60 * 1000
        val daysSinceLastLogin = (now - user.lastLogin) / oneDayMillis

        val updatedStreak = if (daysSinceLastLogin == 1L) user.streak + 1 else 1
        val updates = mapOf(
            "streak" to updatedStreak,
            "lastLogin" to now
        )
        viewModelScope.launch {
            fireStoreRepository.updateUser(user.copy(streak = updatedStreak, lastLogin = now))
        }

        return user.copy(streak = updatedStreak, lastLogin = now)
    }
    fun addExperience(user: User, taskPoints: Int) {
        val newExperience = user.experience + taskPoints
        val newLevel = calculateLevel(newExperience)
        val updates = mapOf(
            "experience" to newExperience,
            "level" to newLevel
        )

        viewModelScope.launch {
            fireStoreRepository.updateUser(user.copy(experience = newExperience, level = newLevel))
            _levelUp.postValue(newLevel > user.level) // Trigger level up animation if level increased
        }

        val updatedUser = user.copy(experience = newExperience, level = newLevel)
        _user.postValue(updatedUser)
        calculateProgress(updatedUser)
    }

    private fun calculateProgress(user: User) {
        val currentExperience = user.experience
        val nextLevelExperience = calculateNextLevelExperience(user.level)
        _progress.postValue(currentExperience to nextLevelExperience)
    }

    private fun calculateLevel(experience: Int): Int {
        return (experience / 100) + 1 // Level increases by 1 every 100 experience points
    }

    private fun calculateNextLevelExperience(level: Int): Int {
        return level * 100 // Example: Level 2 requires 200 experience, Level 3 requires 300
    }
}
