package com.prathamngundikere.wasd.data.repository

import com.prathamngundikere.wasd.data.model.Habit
import com.prathamngundikere.wasd.data.model.Reward
import com.prathamngundikere.wasd.data.model.Task
import com.prathamngundikere.wasd.data.model.User
import com.prathamngundikere.wasd.domain.FireStoreError
import com.prathamngundikere.wasd.domain.Result

interface FireStoreRepository {
    suspend fun insertUser(user: User): Result<Boolean, FireStoreError>
    suspend fun updateUser(user: User): Result<Boolean, FireStoreError>
    suspend fun getUser(userId: String): Result<User?, FireStoreError>
    suspend fun insertHabit(userId: String, habit: Habit): Result<Boolean, FireStoreError>
    suspend fun updateHabit(userId: String,habit: Habit): Result<Boolean, FireStoreError>
    suspend fun getHabit(userId: String, habitId: String): Result<Habit?, FireStoreError>
    suspend fun getHabits(userId: String): Result<List<Habit>, FireStoreError>
    suspend fun insertTask(userId: String, task: Task): Result<Boolean, FireStoreError>
    suspend fun updateTask(userId: String, task: Task): Result<Boolean, FireStoreError>
    suspend fun getTask(userId: String, taskId: String): Result<Task?, FireStoreError>
    suspend fun getTasks(userId: String): Result<List<Task>, FireStoreError>
    suspend fun insertReward(userId: String, reward: Reward): Result<Boolean, FireStoreError>
    suspend fun updateReward(userId: String, reward: Reward): Result<Boolean, FireStoreError>
    suspend fun getReward(userId: String, rewardId: String): Result<Reward?, FireStoreError>
    suspend fun getRewards(userId: String): Result<List<Reward>, FireStoreError>
}