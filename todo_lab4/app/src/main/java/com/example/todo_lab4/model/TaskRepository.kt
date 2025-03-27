package com.example.todo_lab4.model

import androidx.lifecycle.asLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class TaskRepository(private val taskDao: TaskDao) {

    val allTasks = taskDao.getAllTasks().asLiveData()

    suspend fun insert(task: Task) {
        withContext(Dispatchers.IO) {
            taskDao.insert(task)
        }
    }
}
