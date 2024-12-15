package com.prathamngundikere.wasd.ui

import android.content.Context
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.prathamngundikere.wasd.data.model.BottomNavigationItem
import com.prathamngundikere.wasd.data.repository.impl.FireStoreRepositoryImpl
import com.prathamngundikere.wasd.data.repository.impl.GoogleAuthRepositoryImpl
import com.prathamngundikere.wasd.ui.habit.AddHabitScreen
import com.prathamngundikere.wasd.ui.habit.HabitScreen
import com.prathamngundikere.wasd.ui.habit.HabitViewModel
import com.prathamngundikere.wasd.ui.profile.ProfileScreen
import com.prathamngundikere.wasd.ui.profile.ProfileViewModel
import com.prathamngundikere.wasd.ui.tasks.AddTaskScreen
import com.prathamngundikere.wasd.ui.tasks.TaskScreen
import com.prathamngundikere.wasd.ui.tasks.TaskViewModel

@Composable
fun MainNavigation(
    context: Context
) {
    val navItems = listOf(
        BottomNavigationItem(
            title = "Task",
            selectedIcon = Icons.Filled.CheckCircle,
            unSelectedIcon = Icons.Outlined.CheckCircle
        ),
        BottomNavigationItem(
            title = "Habits",
            selectedIcon = Icons.Filled.Check,
            unSelectedIcon = Icons.Outlined.Check
        ),
        BottomNavigationItem(
            title = "Rewards",
            selectedIcon = Icons.Filled.Star,
            unSelectedIcon = Icons.Outlined.Star
        ),
        BottomNavigationItem(
            title = "Profile",
            selectedIcon = Icons.Filled.AccountCircle,
            unSelectedIcon = Icons.Outlined.AccountCircle
        )
    )
    var selectedItemIndex by rememberSaveable {
        mutableIntStateOf(0)
    }
    val navController = rememberNavController()
    val fireStoreRepository = FireStoreRepositoryImpl(
        context = context
    )

    val googleAuthRepository = GoogleAuthRepositoryImpl(
        context = context,
        fireStoreRepository = fireStoreRepository
    )
    val profileViewModel = viewModel<ProfileViewModel> {
        ProfileViewModel(
            googleAuthRepository = googleAuthRepository
        )
    }
    val taskViewMode = viewModel<TaskViewModel> {
        TaskViewModel(
            fireStoreRepository = fireStoreRepository,
            googleAuthRepository = googleAuthRepository
        )
    }
    val habitsViewModel = viewModel<HabitViewModel> {
        HabitViewModel(
            fireStoreRepository = fireStoreRepository,
            googleAuthRepository = googleAuthRepository
        )
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar {
                navItems.forEachIndexed { index, item ->
                    NavigationBarItem(
                        selected = selectedItemIndex == index,
                        onClick = {
                            selectedItemIndex = index
                            navController.navigate(item.route) {
                                popUpTo(navController.graph.startDestinationId)
                                launchSingleTop = true
                            }
                        },
                        icon = {
                            Icon(
                                imageVector = if (index == selectedItemIndex) {
                                    item.selectedIcon
                                } else {
                                    item.unSelectedIcon
                                },
                                contentDescription = item.title
                            )
                        },
                        label = {
                            Text(text = item.title)
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            NavHost(
                navController = navController,
                startDestination = "task"
            ) {
                composable("profile") {
                    LaunchedEffect(key1 = true) {
                        profileViewModel.getUserData()
                    }
                    ProfileScreen(
                        state = profileViewModel.state.collectAsStateWithLifecycle().value,
                        userData = profileViewModel.userData.collectAsStateWithLifecycle().value,
                        signOut = profileViewModel::signOut,
                        context = context
                    )
                }
                composable("task") {
                    LaunchedEffect(key1 = true) {
                        taskViewMode.getTasks()
                    }
                    TaskScreen(
                        tasks = taskViewMode.tasks.collectAsStateWithLifecycle().value,
                        navController = navController,
                        state = taskViewMode.state.collectAsStateWithLifecycle().value,
                        taskCompleted = taskViewMode::taskCompleted
                    )
                }
                composable("add_task") {
                    AddTaskScreen(
                        onAddTaskClick = {
                            taskViewMode.addTask(it)
                        },
                        navController = navController
                    )
                }
                composable("rewards") {

                }
                composable("habits") {
                    LaunchedEffect(key1 = true) {
                        habitsViewModel.getHabits()
                    }
                    HabitScreen(
                        habits = habitsViewModel.habits.collectAsStateWithLifecycle().value,
                        navController = navController,
                        state = habitsViewModel.state.collectAsStateWithLifecycle().value,
                        habitCompleted = habitsViewModel::habitCompleted
                    )
                }
                composable("add_habit") {
                    AddHabitScreen(
                        onAddHabitClick = {
                            habitsViewModel.addHabit(it)
                        },
                        navController = navController
                    )
                }
            }
        }
    }
}