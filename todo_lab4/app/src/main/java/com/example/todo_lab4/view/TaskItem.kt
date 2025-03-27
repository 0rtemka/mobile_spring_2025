package com.example.todo_lab4.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.todo_lab4.model.Task
import com.example.todo_lab4.ui.theme.Typography
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun TaskItem(task: Task) {
    val currentDate = LocalDate.now()
    val isOverdue = task.dueDate.isBefore(currentDate)
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = task.title.take(15) + if (task.title.length > 15) "..." else "",
                style = Typography.titleLarge
            )
            Text(
                text = task.description.take(70) + if (task.description.length > 70) "..." else "",
                style = Typography.bodyMedium,
                modifier = Modifier.padding(top = 8.dp)
            )
            Text(
                text = task.dueDate.format(formatter).toString(), // TODO: год выводится неправильно
                style = Typography.bodyMedium,
                color = if (isOverdue) Color.Red else Color.White,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}