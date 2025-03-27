package com.example.todo_lab4

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.todo_lab4.ui.theme.Todo_lab4Theme
import com.example.todo_lab4.view.MainScreen
import com.example.todo_lab4.model.Task
import com.example.todo_lab4.view.AddTaskScreen
import com.example.todo_lab4.view.TaskItem
import com.example.todo_lab4.viewmodel.TaskViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDate

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Todo_lab4Theme{
                val navController = rememberNavController()
                val viewModel: TaskViewModel = hiltViewModel()

                NavHost(navController = navController, startDestination = "main") {
                    composable("main") {
                        MainScreen(viewModel = viewModel, onAddTask = {
                            navController.navigate("addTask")
                        })
                    }
                    composable("addTask") {
                        AddTaskScreen(viewModel = viewModel, onBack = {
                            navController.popBackStack()
                        })
                    }
                }
            }
        }
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun TaskItemPreview() {
    val task = Task(
        id = 1,
        title = "Купить продукты",
        description = "Молоко, хлеб, яйца, фрукты и овощи.",
        dueDate = LocalDate.now(),
    )

    Todo_lab4Theme {
        TaskItem(task = task)
    }
}