package org.khedr.project.presention.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import org.khedr.project.presention.viewmodel.TestViewModel

@Composable
fun TouchTestScreen(
    viewModel: TestViewModel,
    onFinished: () -> Unit
) {
    val rows = 24
    val cols = 14
    val coveredCells = remember { mutableStateSetOf<Pair<Int, Int>>() }
    var size by remember { mutableStateOf(IntSize.Zero) }

    Scaffold { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color.Black)
                .onSizeChanged { size = it }
                .pointerInput(Unit) {
                    detectDragGestures { change, _ ->
                        val cellWidth = size.width / cols
                        val cellHeight = size.height / rows
                        val col = (change.position.x / cellWidth).toInt()
                        val row = (change.position.y / cellHeight).toInt()
                        if (row in 0 until rows && col in 0 until cols) {
                            coveredCells.add(row to col)
                        }
                    }
                }
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val cellWidth = size.width / cols.toFloat()
                val cellHeight = size.height / rows.toFloat()
                for (row in 0 until rows) {
                    for (col in 0 until cols) {
                        val isCovered = coveredCells.contains(row to col)
                        drawRect(
                            color = if (isCovered) Color.White else Color(0xFF4A90E2),
                            topLeft = Offset(col * cellWidth, row * cellHeight),
                            size = Size(cellWidth, cellHeight)
                        )
                    }
                }
            }

            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                val total = rows * cols
                val covered = coveredCells.size
                val progress = (covered * 100) / total

                Text(
                    text = "Touch ALL boxes 👆 $progress%",
                    color = Color.Black,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(16.dp)
                )

                Button(
                    onClick = {
                        val missing = total - covered
                        viewModel.finishTouchTest(missing < total * 0.05f)
                        onFinished()
                    },
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(bottom = 16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF4A90E2),
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Finish Test")
                }
            }

            // Retry Button
            FloatingActionButton(
                onClick = { coveredCells.clear() },
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
