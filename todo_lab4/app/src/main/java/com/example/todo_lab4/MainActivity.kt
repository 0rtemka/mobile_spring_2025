package com.example.todo_lab4

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.todo_lab4.ui.theme.Todo_lab4Theme
import com.example.todo_lab4.view.MainScreen
import com.example.todo_lab4.view.AddTaskScreen
import com.example.todo_lab4.view.EditTaskScreen
import com.example.todo_lab4.view.TaskDetailsScreen
import com.example.todo_lab4.viewmodel.TaskViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Todo_lab4Theme {
                val navController = rememberNavController()
                val viewModel: TaskViewModel = hiltViewModel()

                NavHost(navController = navController, startDestination = "main") {
                    composable("main") {
                        MainScreen(
                            viewModel = viewModel,
                            onAddTask = { navController.navigate("addTask") },
                            onTaskClick = { taskId -> navController.navigate("taskDetails/$taskId") })
                    }
                    composable("addTask") {
                        AddTaskScreen(
                            viewModel = viewModel,
                            onBack = { navController.popBackStack() },
                            onSuccess = { navController.popBackStack("main", inclusive = false) }
                        )
                    }
                    composable(
                        route = "taskDetails/{taskId}",
                        arguments = listOf(navArgument("taskId") { type = NavType.IntType })
                    ) { backStackEntry ->
                        val taskId = backStackEntry.arguments?.getInt("taskId") ?: 0
                        TaskDetailsScreen(
                            viewModel = viewModel,
                            taskId = taskId,
                            onBack = { navController.popBackStack() },
                            onEditTask = { taskId -> navController.navigate("editTask/$taskId") }
                        )
                    }
                    composable(
                        route = "editTask/{taskId}",
                        arguments = listOf(navArgument("taskId") { type = NavType.IntType })
                    ) { navBackStackEntry ->
                        val taskId = navBackStackEntry.arguments?.getInt("taskId") ?: 0
                        EditTaskScreen(
                            viewModel = viewModel,
                            taskId = taskId,
                            onBack = { navController.popBackStack() },
                            onDeleteSuccess = {
                                navController.popBackStack(
                                    "main",
                                    inclusive = false
                                )
                            }
                        )
                    }
                }
            }
        }
    }
}