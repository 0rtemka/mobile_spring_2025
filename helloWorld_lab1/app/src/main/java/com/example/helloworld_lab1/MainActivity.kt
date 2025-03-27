package com.example.helloworld_lab1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.helloworld_lab1.ui.theme.HelloWorldTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HelloWorldTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    TextApp()
                }
            }
        }
    }
}

@Composable
fun TextApp() {
    var textFieldValue by remember { mutableStateOf("") }
    var textValue by remember { mutableStateOf("") }

    var dialogValue by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 128.dp).fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(
                text = stringResource(R.string.app_title),
                style = MaterialTheme.typography.titleLarge,
            )
            TextField(
                value = textFieldValue,
                onValueChange = { textFieldValue = it },
                label = { Text(stringResource(R.string.text_field_input)) },
                modifier = Modifier.fillMaxWidth(),
            )

            Button(
                onClick = {
                    if (textFieldValue.isBlank()) {
                        dialogValue = "Необходимо заполнить поле"
                        showDialog = true
                        return@Button
                    }
                    textValue = textFieldValue
                    textFieldValue = ""
                },
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(stringResource(R.string.button_text), style = MaterialTheme.typography.bodyLarge)
            }

            Text(
                text = textValue,
                style = MaterialTheme.typography.bodyLarge,
            )

            if (showDialog) {
                AlertDialog(
                    onDismissRequest = { showDialog = false },
                    title = { Text("Предупреждение", style = MaterialTheme.typography.titleMedium, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth()) },
                    text = { Text(dialogValue, style = MaterialTheme.typography.bodyLarge, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp)) },
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
fun TextAppPreview() {
    HelloWorldTheme {
        TextApp()
    }
}
