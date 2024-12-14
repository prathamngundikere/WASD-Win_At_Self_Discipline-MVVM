package com.prathamngundikere.wasd.data.repository.impl

import android.content.Context
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.prathamngundikere.wasd.R
import com.prathamngundikere.wasd.data.model.Habit
import com.prathamngundikere.wasd.data.model.Reward
import com.prathamngundikere.wasd.data.model.Task
import com.prathamngundikere.wasd.data.model.User
import com.prathamngundikere.wasd.data.repository.FireStoreRepository
import com.prathamngundikere.wasd.domain.FireStoreError
import com.prathamngundikere.wasd.domain.Result
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class FireStoreRepositoryImpl(
    context: Context
): FireStoreRepository {
    
    private val db = FirebaseFirestore.getInstance()
    private val usersCollection = db.collection(context.getString(R.string.users))
    
    override suspend fun insertUser(user: User): Result<Boolean, FireStoreError> {
        return try {
            withContext(Dispatchers.IO) {
                var result: Boolean = false
                usersCollection
                    .add(user.toHashMap())
                    .addOnSuccessListener {
                        CoroutineScope(Dispatchers.IO).launch {
                            updateUser(user.copy(userId = it.id))
                        }
                        result = true
                    }
                    .addOnFailureListener {
                        result = false
                        Log.e("FireStoreRepository", "insertUser: Failed message: ${it.message}")
                    }
                if (result) {
                    Result.Success(true)
                } else {
                    Result.Error(FireStoreError.UNABLE_TO_INSERT_USER)
                }
            }
        } catch (e: Exception) {
            Log.e("FireStoreRepository", "insertUser: Failed message: ${e.message}")
            Result.Error(FireStoreError.UNABLE_TO_INSERT_USER)
        }
    }

    override suspend fun updateUser(user: User): Result<Boolean, FireStoreError> {
        return try {
            withContext(Dispatchers.IO) {
                var result: Boolean = false
                usersCollection
                    .document(user.userId)
                    .set(user.toHashMap())
                    .addOnSuccessListener{
                        result = true
                    }
                    .addOnFailureListener {
                        result = false
                        Log.e("FireStoreRepository", "updateUser: Failed message: ${it.message}")
                    }
                if (result) {
                    Result.Success(true)
                } else {
                    Result.Error(FireStoreError.UNABLE_TO_UPDATE_USER)
                }
            }
        } catch (e: Exception) {
            Log.e("FireStoreRepository", "updateUser: Failed message: ${e.message}")
            Result.Error(FireStoreError.UNABLE_TO_UPDATE_USER)
        }
    }

    override suspend fun getUser(userId: String): Result<User?, FireStoreError> {
        return try {
            withContext(Dispatchers.IO) {
                var exp: Boolean = true
                var result: User? = null
                usersCollection
                    .get()
                    .addOnSuccessListener {
                        for (document in it) {
                            if (document.data["userId"] == userId) {
                                result = document.data.toUser()
                                exp = false
                                Log.d("FireStoreRepository", "getUser: Success $result")
                            }
                        }
                    }
                    .addOnFailureListener {e ->
                        Log.e("FireStoreRepository", "getUser: Failed message: ${e.message}")
                        exp = true
                    }
                if (exp) {
                    Result.Error(FireStoreError.UNABLE_TO_GET_USER)
                } else {
                    if (result == null) {
                        Result.Error(FireStoreError.USER_NOT_FOUND)
                    } else {
                        Result.Success(result)
                    }
                }
            }
        } catch (e: Exception) {
            Log.e("FireStoreRepository", "getUser: Failed message: ${e.message}")
            Result.Error(FireStoreError.UNABLE_TO_GET_USER)
        }
    }

    override suspend fun insertHabit(userId: String, habit: Habit): Result<Boolean, FireStoreError> {
        return try {
            withContext(Dispatchers.IO) {
                var result: Boolean = false
                usersCollection
                    .document(userId)
                    .collection("habits")
                    .add(habit.toHashMap())
                    .addOnSuccessListener{
                        CoroutineScope(Dispatchers.IO).launch {
                            updateHabit(userId, habit.copy(habitId = it.id))
                        }
                        result = true
                    }
                    .addOnFailureListener {
                        result = false
                        Log.e("FireStoreRepository", "insertHabit: Failed message: ${it.message}")
                    }
                if (result) {
                    Result.Success(true)
                } else {
                    Result.Error(FireStoreError.UNABLE_TO_INSERT_HABIT)
                }
            }
        } catch (e: Exception) {
            Log.e("FireStoreRepository", "insertHabit: Failed message: ${e.message}")
            Result.Error(FireStoreError.UNABLE_TO_INSERT_HABIT)
        }
    }

    override suspend fun updateHabit(
        userId: String,
        habit: Habit
    ): Result<Boolean, FireStoreError> {
        return try {
            withContext(Dispatchers.IO) {
                var result: Boolean = false
                usersCollection
                    .document(userId)
                    .collection("habits")
                    .document(habit.habitId)
                    .set(habit.toHashMap(), SetOptions.merge())
                    .addOnSuccessListener{
                        result = true
                    }
                    .addOnFailureListener {
                        result = false
                        Log.e("FireStoreRepository", "updateHabit: Failed message: ${it.message}")
                    }
                if (result) {
                    Result.Success(true)
                } else {
                    Result.Error(FireStoreError.UNABLE_TO_UPDATE_HABIT)
                }
            }
        } catch (e: Exception) {
            Log.e("FireStoreRepository", "updateHabit: Failed message: ${e.message}")
            Result.Error(FireStoreError.UNABLE_TO_UPDATE_HABIT)
        }
    }

    override suspend fun getHabit(
        userId: String,
        habitId: String
    ): Result<Habit?, FireStoreError> {
        return try {
            withContext(Dispatchers.IO) {
                var result: Habit? = null
                var exp: Boolean = true
              usersCollection
                  .document(userId)
                  .collection("habits")
                  .get()
                  .addOnSuccessListener{
                      for (document in it) {
                          if(document.data["habitId"] == habitId) {
                              result = document.data.toHabit()
                              exp = false
                              Log.d("FireStoreRepository", "getHabit: Success $result")
                          }
                      }
                  }
                  .addOnFailureListener {
                      Log.e("FireStoreRepository", "getHabit: Failed message: ${it.message}")
                      exp = true
                  }
                if (exp) {
                    Result.Error(FireStoreError.UNABLE_TO_GET_HABIT)
                } else {
                    if (result == null) {
                        Result.Error(FireStoreError.HABIT_NOT_FOUND)
                    } else {
                        Result.Success(result)
                    }
                }
            }
        } catch (e: Exception) {
            Log.e("FireStoreRepository", "getHabit: Failed message: ${e.message}")
            Result.Error(FireStoreError.UNABLE_TO_GET_HABIT)
        }
    }

    override suspend fun getHabits(userId: String): Result<List<Habit>, FireStoreError> {
        return try {
            withContext(Dispatchers.IO) {
                val ref = usersCollection
                    .document(userId)
                    .collection("habits")
                val query = ref.get().await()
                val habits = query.documents.mapNotNull {
                    it.toObject(Habit::class.java)
                }
                Result.Success(habits)
            }
        } catch (e: Exception) {
            Log.e("FireStoreRepository", "getHabits: Failed message: ${e.message}")
            Result.Error(FireStoreError.UNABLE_TO_GET_HABIT)
        }
    }

    override suspend fun insertTask(
        userId: String,
        task: Task
    ): Result<Boolean, FireStoreError> {
        return try {
            withContext(Dispatchers.IO) {
                var result: Boolean = false
                usersCollection
                    .document(userId)
                    .collection("tasks")
                    .add(task.toHashMap())
                    .addOnSuccessListener{
                        CoroutineScope(Dispatchers.IO).launch {
                            updateTask(userId, task.copy(taskId = it.id))
                        }
                        result = true
                    }
                    .addOnFailureListener {
                        result = false
                        Log.e("FireStoreRepository", "insertTask: Failed message: ${it.message}")
                    }
                if (result) {
                    Result.Success(true)
                } else {
                    Result.Error(FireStoreError.UNABLE_TO_INSERT_TASK)
                }
            }
        } catch (e: Exception) {
            Log.e("FireStoreRepository", "insertTask: Failed message: ${e.message}")
            Result.Error(FireStoreError.UNABLE_TO_INSERT_TASK)
        }
    }

    override suspend fun updateTask(
        userId: String,
        task: Task
    ): Result<Boolean, FireStoreError> {
        return try {
            withContext(Dispatchers.IO) {
                var result: Boolean = false
                usersCollection
                    .document(userId)
                    .collection("tasks")
                    .document(task.taskId)
                    .set(task.toHashMap(), SetOptions.merge())
                    .addOnSuccessListener{
                        result = true
                    }
                    .addOnFailureListener {
                        result = false
                        Log.e("FireStoreRepository", "updateTask: Failed message: ${it.message}")
                    }
                if (result) {
                    Result.Success(true)
                } else {
                    Result.Error(FireStoreError.UNABLE_TO_UPDATE_TASK)
                }
            }
        } catch (e: Exception) {
            Log.e("FireStoreRepository", "updateTask: Failed message: ${e.message}")
            Result.Error(FireStoreError.UNABLE_TO_UPDATE_TASK)
        }
    }

    override suspend fun getTask(
        userId: String,
        taskId: String
    ): Result<Task?, FireStoreError> {
        return try {
            withContext(Dispatchers.IO) {
                var result: Task? = null
                var exp: Boolean = true
                usersCollection
                    .document(userId)
                    .collection("tasks")
                    .get()
                    .addOnSuccessListener{
                        for (document in it) {
                            if(document.data["taskId"] == taskId) {
                                result = document.data.toTask()
                                exp = false
                                Log.d("FireStoreRepository", "getTask: Success $result")
                            }
                        }
                    }
                    .addOnFailureListener {
                        Log.e("FireStoreRepository", "getTask: Failed message: ${it.message}")
                        exp = true
                    }
                if (exp) {
                    Result.Error(FireStoreError.UNABLE_TO_GET_TASK)
                } else {
                    if (result == null) {
                        Result.Error(FireStoreError.TASK_NOT_FOUND)
                    } else {
                        Result.Success(result)
                    }
                }
            }
        } catch (e: Exception) {
            Log.e("FireStoreRepository", "getTask: Failed message: ${e.message}")
            Result.Error(FireStoreError.UNABLE_TO_GET_TASK)
        }
    }

    override suspend fun getTasks(userId: String): Result<List<Task>, FireStoreError> {
        return try {
            withContext(Dispatchers.IO) {
                val ref = usersCollection
                    .document(userId)
                    .collection("tasks")
                val query = ref.get().await()
                val tasks = query.documents.mapNotNull {
                    it.toObject(Task::class.java)
                }
                Result.Success(tasks)
            }
        } catch (e: Exception) {
            Log.e("FireStoreRepository", "getTasks: Failed message: ${e.message}")
            Result.Error(FireStoreError.UNABLE_TO_GET_TASK)
        }
    }

    override suspend fun insertReward(
        userId: String,
        reward: Reward
    ): Result<Boolean, FireStoreError> {
        return try {
            withContext(Dispatchers.IO) {
                var result: Boolean = false
                usersCollection
                    .document(userId)
                    .collection("rewards")
                    .add(reward.toHashMap())
                    .addOnSuccessListener{
                        CoroutineScope(Dispatchers.IO).launch {
                            updateReward(userId, reward.copy(rewardId = it.id))
                        }
                        result = true
                    }
                    .addOnFailureListener {
                        result = false
                        Log.e("FireStoreRepository", "insertReward: Failed message: ${it.message}")
                    }
                if (result) {
                    Result.Success(true)
                } else {
                    Result.Error(FireStoreError.UNABLE_TO_INSERT_REWARD)
                }
            }
        } catch (e: Exception) {
            Log.e("FireStoreRepository", "insertReward: Failed message: ${e.message}")
            Result.Error(FireStoreError.UNABLE_TO_INSERT_REWARD)
        }
    }

    override suspend fun updateReward(
        userId: String,
        reward: Reward
    ): Result<Boolean, FireStoreError> {
        return try {
            withContext(Dispatchers.IO) {
                var result: Boolean = false
                usersCollection
                    .document(userId)
                    .collection("rewards")
                    .document(reward.rewardId)
                    .set(reward.toHashMap(), SetOptions.merge())
                    .addOnSuccessListener{
                        result = true
                    }
                    .addOnFailureListener {
                        result = false
                        Log.e("FireStoreRepository", "updateReward: Failed message: ${it.message}")
                    }
                if (result) {
                    Result.Success(true)
                } else {
                    Result.Error(FireStoreError.UNABLE_TO_UPDATE_REWARD)
                }
            }
        } catch (e: Exception) {
            Log.e("FireStoreRepository", "updateReward: Failed message: ${e.message}")
            Result.Error(FireStoreError.UNABLE_TO_UPDATE_REWARD)
        }
    }

    override suspend fun getReward(
        userId: String,
        rewardId: String
    ): Result<Reward?, FireStoreError> {
        return try {
            withContext(Dispatchers.IO) {
                var result: Reward? = null
                var exp: Boolean = true
                usersCollection
                    .document(userId)
                    .collection("rewards")
                    .get()
                    .addOnSuccessListener{
                        for (document in it) {
                            if(document.data["rewardId"] == rewardId) {
                                result = document.data.toReward()
                                exp = false
                                Log.d("FireStoreRepository", "getReward: Success $result")
                            }
                        }
                    }
                    .addOnFailureListener {
                        Log.e("FireStoreRepository", "getReward: Failed message: ${it.message}")
                        exp = true
                    }
                if (exp) {
                    Result.Error(FireStoreError.UNABLE_TO_GET_REWARD)
                } else {
                    if (result == null) {
                        Result.Error(FireStoreError.REWARD_NOT_FOUND)
                    } else {
                        Result.Success(result)
                    }
                }
            }
        } catch (e: Exception) {
            Log.e("FireStoreRepository", "getReward: Failed message: ${e.message}")
            Result.Error(FireStoreError.UNABLE_TO_GET_REWARD)
        }
    }

    override suspend fun getRewards(userId: String): Result<List<Reward>, FireStoreError> {
        return try {
            withContext(Dispatchers.IO) {
                val ref = usersCollection
                    .document(userId)
                    .collection("rewards")
                val query = ref.get().await()
                val rewards = query.documents.mapNotNull {
                    it.toObject(Reward::class.java)
                }
                Result.Success(rewards)
            }
        } catch (e: Exception) {
            Log.e("FireStoreRepository", "getRewards: Failed message: ${e.message}")
            Result.Error(FireStoreError.UNABLE_TO_GET_REWARD)
        }
    }

    private fun User.toHashMap(): HashMap<String, Any> {
        return hashMapOf(
            "userId" to userId,
            "userName" to userName,
            "email" to email,
            "level" to level,
            "experience" to experience,
            "streak" to streak,
            "lastLogin" to lastLogin
        )
    }

    private fun Habit.toHashMap(): HashMap<String, Any> {
        return hashMapOf(
            "habitId" to habitId,
            "name" to name,
            "description" to description,
            "createdAt" to createdAt,
            "frequency" to frequency,
            "type" to type,
            "completedCount" to completedCount,
            "streak" to streak,
            "lastCompleted" to lastCompleted
        )
    }

    private fun Task.toHashMap(): HashMap<String, Any> {
        return hashMapOf(
            "taskId" to taskId,
            "title" to title,
            "description" to description,
            "dueDate" to dueDate,
            "priority" to priority,
            "isCompleted" to isCompleted,
            "points" to points,
            "createdAt" to createdAt
        )
    }

    private fun Reward.toHashMap(): HashMap<String, Any> {
        return hashMapOf(
            "rewardId" to rewardId,
            "title" to title,
            "description" to description,
            "requiredPoints" to requiredPoints,
            "claimed" to claimed,
            "createdAt" to createdAt
        )
    }

    private fun Map<String, Any>.toUser(): User {
        return User(
            userId = this["userId"] as String,
            userName = this["userName"] as String,
            email = this["email"] as String,
            level = this["level"] as Int,
            experience = this["experience"] as Int,
            streak = this["streak"] as Int,
            lastLogin = this["lastLogin"] as Long
        )
    }

    private fun Map<String, Any>.toHabit(): Habit {
        return Habit(
            habitId = this["habitId"] as String,
            name = this["name"] as String,
            description = this["description"] as String,
            createdAt = this["createdAt"] as Long,
            frequency = this["frequency"] as Int,
            type = this["type"] as String,
            completedCount = this["completedCount"] as Int,
            streak = this["streak"] as Int,
            lastCompleted = this["lastCompleted"] as Long
        )
    }

    private fun Map<String, Any>.toTask(): Task {
        return Task(
            taskId = this["taskId"] as String,
            title = this["title"] as String,
            description = this["description"] as String,
            dueDate = this["dueDate"] as Long,
            priority = this["priority"] as String,
            isCompleted = this["isCompleted"] as Boolean,
            points = this["points"] as Int,
            createdAt = this["createdAt"] as Long
        )
    }

    private fun Map<String, Any>.toReward(): Reward {
        return Reward(
            rewardId = this["rewardId"] as String,
            title = this["title"] as String,
            description = this["description"] as String,
            requiredPoints = this["requiredPoints"] as Int,
            claimed = this["claimed"] as Boolean,
            createdAt = this["createdAt"] as Long
        )
    }
}