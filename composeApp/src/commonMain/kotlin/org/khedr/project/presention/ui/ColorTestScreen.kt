package org.khedr.project.presention.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.khedr.project.presention.viewmodel.TestViewModel

@Composable
fun ColorTestScreen(
    viewModel: TestViewModel,
    onFinished: () -> Unit
) {

    val colors = listOf(
        Color.White,
        Color.Black,
        Color.Red,
        Color.Green,
        Color.Blue,
        Color.Yellow
    )

    var currentIndex by remember { mutableStateOf(0) }

    val currentColor = colors[currentIndex]

    Scaffold { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(currentColor)
                .clickable {
                    if (currentIndex < colors.lastIndex) {
                        currentIndex++
                    }
                }
        ) {
            // Text on top
            Text(
                text = "Tap to change color",
                color = if (currentColor == Color.Black) Color.White else Color.Black,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(16.dp)
            )

            // Color Name
            Text(
                text = when (currentColor) {
                    Color.White -> "WHITE"
                    Color.Black -> "BLACK"
                    Color.Red -> "RED"
                    Color.Green -> "GREEN"
                    Color.Blue -> "BLUE"
                    Color.Yellow -> "YELLOW"
                    else -> ""
                },
                color = if (currentColor == Color.Black) Color.White else Color.Black,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.Center)
            )

            // Buttons at the bottom
            Row(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp)
            ) {
                Button(
                    onClick = {
                        viewModel.finishColorTest(true)
                        onFinished()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF4A90E2),
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("No Issues ✅")
                }

                Spacer(modifier = Modifier.width(8.dp))

                Button(
                    onClick = {
                        viewModel.finishColorTest(false)
                        onFinished()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Red,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Has Issue ❌")
                }
            }

            // Retry Button
            FloatingActionButton(
                onClick = { currentIndex = 0 },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(16.dp),
                containerColor = Color.White.copy(alpha = 0.7f),
                contentColor = Color.Black
            ) {
                Icon(Icons.Default.Refresh, contentDescription = "Retry")
            }
        }
    }
}
