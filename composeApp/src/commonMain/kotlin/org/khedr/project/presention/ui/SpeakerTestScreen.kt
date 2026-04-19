package org.khedr.project.presention.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import org.khedr.project.presention.viewmodel.TestViewModel
import org.khedr.project.rememberAudioPlayer

@Composable
fun SpeakerTestScreen(
    viewModel: TestViewModel,
    onFinished: () -> Unit
) {
    val audioPlayer = rememberAudioPlayer()
    var isPlaying by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }
    var retryTrigger by remember { mutableStateOf(0) }

    LaunchedEffect(retryTrigger) {
        isPlaying = false
        showDialog = false
    }
    Scaffold (contentColor = Color.Transparent){ padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding))
        {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Speaker Test 🔊",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(40.dp))

                // Speaker Animation / Icon
                Box(
                    modifier = Modifier
                        .size(200.dp)
                        .background(
                            if (isPlaying) Color(0xFFE3F2FD) else Color(0xFFF5F5F5),
                            CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    if (isPlaying) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(180.dp),
                            strokeWidth = 8.dp,
                            color = Color(0xFF2196F3)
                        )
                    }
                    IconButton(
                        onClick = {
                            if (!isPlaying) {
                                isPlaying = true
                                audioPlayer.playSound()
                            }
                        },
                        modifier = Modifier.size(100.dp)
                    ) {
                        Icon(
                            Icons.Default.PlayArrow,
                            contentDescription = "Play",
                            modifier = Modifier.size(80.dp),
                            tint = if (isPlaying) Color(0xFF2196F3) else Color.Gray
                        )
                    }
                }

                LaunchedEffect(isPlaying) {
                    if (isPlaying) {
                        delay(3000)
                        isPlaying = false
                        showDialog = true
                    }
                }

                Spacer(modifier = Modifier.height(40.dp))

                Text(
                    text = "Click play and listen if you hear a sound",
                    fontSize = 16.sp,
                    color = Color.Gray
                )
            }

            // Retry Button
            FloatingActionButton(
                onClick = { retryTrigger++ },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(16.dp),
                containerColor = Color(0xFF4A90E2),
                contentColor = Color.White
            ) {
                Icon(Icons.Default.Refresh, contentDescription = "Retry")
            }
        }
    }


    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Speaker Result") },
            text = { Text("Did you hear the sound clearly?") },
            confirmButton = {
                Button(onClick = {
                    viewModel.finishSpeakerTest(true)
                    showDialog = false
                    onFinished()
                }) {
                    Text("Yes, I heard it ✅")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    viewModel.finishSpeakerTest(false)
                    showDialog = false
                    onFinished()
                }) {
                    Text("No ❌", color = Color.Red)
                }
            }
        )
    }
}
