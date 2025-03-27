package com.example.quadraticequation_lab2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.quadraticequation_lab2.ui.theme.QuadraticEquationTheme
import kotlin.math.sqrt

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            QuadraticEquationTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    Equation(
                        name = "Android",
                        modifier = Modifier.padding()
                    )
                }
            }
        }
    }
}

@Composable
fun Equation(name: String, modifier: Modifier = Modifier) {
    var a by remember { mutableStateOf("") }
    var b by remember { mutableStateOf("") }
    var c by remember { mutableStateOf("") }

    var showDialog by remember { mutableStateOf(false) }
    var answer by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 16.dp, end = 16.dp, top = 128.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp),
        ) {
            Text(
                text = stringResource(R.string.app_title),
                style = MaterialTheme.typography.titleLarge,
            )

            Text(
                text = "Введите значения коэффициентов",
                style = MaterialTheme.typography.bodyLarge,
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                TextField(
                    label = { Text("a") },
                    value = a,
                    onValueChange = { a = it },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.weight(1f),
                )

                TextField(
                    label = { Text("b") },
                    value = b,
                    onValueChange = { b = it },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.weight(1f),
                )

                TextField(
                    label = { Text("c") },
                    value = c,
                    onValueChange = { c = it },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.weight(1f),
                )
            }

            Button(
                onClick = {
                    if (a.isBlank() or b.isBlank() or c.isBlank()) {
                        answer = "Не все поля заполнены"
                        showDialog = true
                        return@Button
                    }

                    val aValue = a.toDoubleOrNull()
                    val bValue = b.toDoubleOrNull()
                    val cValue = c.toDoubleOrNull()

                    if (aValue == null || bValue == null || cValue == null) {
                        answer = "Некорректные значение коэффициентов"
                        showDialog = true
                        return@Button
                    }

                    if (aValue == 0.0) {
                        if (bValue == 0.0) {
                            if (cValue == 0.0) {
                                answer = "Уравнение имеет бесконечно много корней"
                            } else {
                                answer = "Уравнение не имеет решений"
                            }
                        } else {
                            val x = -cValue / bValue
                            answer = "Линейное уравнение.\n Корень: x = $x"
                        }
                    } else {
                        val discriminant = bValue * bValue - 4 * aValue * cValue

                        when {
                            discriminant > 0 -> {
                                val sqrtDiscriminant = sqrt(discriminant)
                                val x1 = (-bValue + sqrtDiscriminant) / (2 * aValue)
                                val x2 = (-bValue - sqrtDiscriminant) / (2 * aValue)
                                answer =
                                    "Квадратное уравнение.\n Дискриминант:\n D = $discriminant.\n Два корня:\n x1 = $x1,\n  x2 = $x2"
                            }

                            discriminant == 0.0 -> {
                                val x = -bValue / (2 * aValue)
                                answer =
                                    "Квадратное уравнение.\n Дискриминант:\n D = $discriminant.\n Два одинаковых корня:\n x1 = x2 = $x"
                            }

                            discriminant < 0 -> {
                                answer = "Уравнение не имеет действительных корней"
                            }
                        }
                    }
                    showDialog = true
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Вычислить", style = MaterialTheme.typography.bodyLarge)
            }

            if (showDialog) {
                AlertDialog(
                    onDismissRequest = { showDialog = false },
                    title = { Text("Решение уравнения", style = MaterialTheme.typography.titleMedium, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth()) },
                    text = { Text(answer, style = MaterialTheme.typography.bodyLarge, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp)) },
                    confirmButton = {
                        Button(
                            onClick = { showDialog = false },
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            Text("OK", style = MaterialTheme.typography.bodyLarge)
                        }
                    },
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun EquationPreview() {
    QuadraticEquationTheme {
        Equation("Android")
    }
}