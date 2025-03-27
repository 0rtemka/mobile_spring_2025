package com.example.calculator_lab3

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import net.objecthunter.exp4j.ExpressionBuilder
import java.math.BigDecimal
import java.math.RoundingMode

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CalculatorScreen()
        }
    }
}

@Composable
fun CalculatorScreen() {
    var input by remember { mutableStateOf("0") }
    var resetInput by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()

    val textSize = when {
        input.length > 10 -> 40.sp
        input.length > 7 -> 60.sp
        else -> 80.sp
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.End
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .padding(16.dp)
                .horizontalScroll(scrollState),
            horizontalArrangement = Arrangement.End
        ) {
            LaunchedEffect(input) {
                scrollState.scrollTo(scrollState.maxValue)
            }
            Text(
                text = input,
                style = TextStyle(
                    color = Color.White,
                    fontSize = textSize,
                    fontWeight = FontWeight.Light,
                    textAlign = TextAlign.End
                )
            )
        }
        CalculatorButtons { button ->
            when (button) {
                "AC" -> {
                    input = "0"
                    resetInput = false
                }

                "+/-" -> {
                    val tokens = input.split(" ").toMutableList()
                    if (tokens.isNotEmpty()) {
                        val lastIndex = tokens.lastIndex
                        val lastToken = tokens[lastIndex].replace(",", ".")

                        if (lastToken.startsWith("(-")) {
                            tokens[lastIndex] = lastToken.removeSurrounding("(-", ")");
                        } else {
                            val lastNumber = lastToken.toBigDecimalOrNull()

                            if (lastNumber != null) {
                                val hasPlusMinusBefore =
                                    lastIndex > 0 && tokens[lastIndex - 1] in listOf("+", "-")
                                val hasMultDivideBefore =
                                    lastIndex > 0 && tokens[lastIndex - 1] in listOf("×", "/")

                                if (hasPlusMinusBefore) {
                                    tokens[lastIndex - 1] =
                                        if (tokens[lastIndex - 1] == "+") "-" else "+"
                                } else {
                                    tokens[lastIndex] = when {
                                        lastToken.startsWith("(-") -> lastToken.removeSurrounding(
                                            "(-",
                                            ")"
                                        ).replace(".", ",")

                                        lastToken.startsWith("-") -> lastToken.removePrefix("-").replace(".", ",")
                                        hasMultDivideBefore -> "(-${lastToken})".replace(".", ",")
                                        else -> "-${lastToken}".replace(".", ",")
                                    }
                                }
                            }
                        }
                        input = tokens.joinToString(" ")
                    }
                }

                "%" -> {
                    val tokens = input.split(" ")
                    if (tokens.isNotEmpty()) {
                        val lastNumber = tokens.last().toBigDecimalOrNull()
                        if (lastNumber != null) {
                            val newNumber =
                                lastNumber.divide(BigDecimal(100), 10, RoundingMode.HALF_UP)
                                    .stripTrailingZeros().toPlainString()
                            input = input.dropLast(tokens.last().length) + newNumber
                        }
                    }
                }

                "+", "-", "×", "/" -> {
                    val tokens = input.split(" ").toMutableList()

                    if (input.isNotEmpty() && !input.last().isWhitespace()) {
                        input += " $button "
                        resetInput = false
                    } else {
                        tokens[tokens.lastIndex - 1] = button
                        resetInput = false
                        input = tokens.joinToString(" ")
                    }
                }

                "=" -> {
                    input = try {
                        val result = evaluateExpression(input)
                        result?.stripTrailingZeros()?.toPlainString()?.replace(".", ",") ?: "Error"
                    } catch (e: Exception) {
                        "Error"
                    }
                    resetInput = true
                }

                else -> {
                    if (resetInput) {
                        input = button
                        resetInput = false
                    } else {
                        input = if (input == "0") button else input + button
                    }
                }
            }
        }
    }
}

fun evaluateExpression(expression: String): BigDecimal? {
    val exp = expression.replace("×", "*").replace(",", ".")

    val result = ExpressionBuilder(exp)
        .build()
        .evaluate()
        .toBigDecimal()
        .setScale(8, RoundingMode.HALF_UP)

    return result
}

@Composable
fun CalculatorButtons(onClick: (String) -> Unit) {
    val buttons = listOf(
        listOf("AC", "+/-", "%", "/"),
        listOf("7", "8", "9", "×"),
        listOf("4", "5", "6", "-"),
        listOf("1", "2", "3", "+"),
        listOf("0", ",", "=")
    )
    val colors = mapOf(
        "AC" to Color.Gray, "+/-" to Color.Gray, "%" to Color.Gray,
        "/" to Color(255, 149, 0), "×" to Color(255, 149, 0), "-" to Color(255, 149, 0),
        "+" to Color(255, 149, 0), "=" to Color(255, 149, 0)
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp)
    ) {
        buttons.dropLast(1).forEach { row ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                row.forEach { label ->
                    CalculatorButton(label, colors[label] ?: Color.DarkGray, onClick)
                }
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            CalculatorButton("0", Color.DarkGray, onClick, isWide = true)
            CalculatorButton(",", Color.DarkGray, onClick)
            CalculatorButton("=", Color(255, 149, 0), onClick)
        }
    }
}

@Composable
fun CalculatorButton(
    label: String,
    color: Color,
    onClick: (String) -> Unit,
    isWide: Boolean = false
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(if (isWide) 176.dp else 88.dp, 88.dp)
            .background(color, shape = CircleShape)
            .clickable { onClick(label) }
            .padding(8.dp)
    ) {
        Text(
            text = label,
            style = TextStyle(
                color = Color.White,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        )
    }
}
