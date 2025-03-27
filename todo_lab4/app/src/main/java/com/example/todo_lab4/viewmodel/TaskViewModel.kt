package com.example.todo_lab4.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todo_lab4.model.Task
import com.example.todo_lab4.model.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor (private val repository: TaskRepository) : ViewModel() {
    val allTasks = repository.allTasks

    fun insert(task: Task) = viewModelScope.launch {
        repository.insert(task)
    }
}