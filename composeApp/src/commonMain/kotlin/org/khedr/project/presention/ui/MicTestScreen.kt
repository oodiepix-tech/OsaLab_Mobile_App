package org.khedr.project.presention.ui

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.stringResource
import org.khedr.project.RequestRecordAudioPermission
import org.khedr.project.presention.viewmodel.TestViewModel
import org.khedr.project.rememberAudioPlayer
import org.khedr.project.rememberAudioRecorder
import osacheck.composeapp.generated.resources.Res
import osacheck.composeapp.generated.resources.*

enum class MicTestState {
    WAITING, RECORDING, PLAYING, FINISHED
}

@Composable
fun MicTestScreen(
    viewModel: TestViewModel,
    onFinished: () -> Unit
) {
    var testState by remember { mutableStateOf(MicTestState.WAITING) }
    var amplitude by remember { mutableStateOf(0) }
    var recordingProgress by remember { mutableStateOf(0f) }
    var hasPermission by remember { mutableStateOf(false) }
    var retryTrigger by remember { mutableStateOf(0) }
    
    val recorder = rememberAudioRecorder()
    val player = rememberAudioPlayer()
    val audioFile = "mic_test.mp4"

    RequestRecordAudioPermission {
        hasPermission = true
    }

    // إدارة دورة الحياة
    LaunchedEffect(hasPermission, retryTrigger) {
        if (hasPermission) {
            testState = MicTestState.WAITING
            recordingProgress = 0f
            amplitude = 0
            
            recorder.start() // ابدأ المراقبة (Visualizer)
            
            while (testState == MicTestState.WAITING) {
                val amp = recorder.maxAmplitude
                amplitude = amp
                
                if (amp > 3000) {
                    testState = MicTestState.RECORDING
                    break
                }
                delay(50)
            }
            
            if (testState == MicTestState.RECORDING) {
                recorder.stop()
                recorder.startRecording(audioFile)
                
                // سجل لمدة 5 ثوانٍ
                for (i in 1..50) {
                    recordingProgress = i / 50f
                    amplitude = recorder.maxAmplitude
                    delay(100)
                }
                
                recorder.stop()
                testState = MicTestState.PLAYING
                
                // تشغيل الملف المسجل
                player.playFile(audioFile)
                delay(5000) // انتظر حتى ينتهي التشغيل
                
                testState = MicTestState.FINISHED
            }
        }
    }

    val animatedHeight by animateDpAsState(
        targetValue = ((amplitude / 100).coerceIn(40, 300)).dp,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "MicArrowMotion"
    )
    Scaffold (contentColor = Color.Transparent){ padding ->

    Box(modifier = Modifier.fillMaxSize().padding(padding)) {
        Column(
            modifier = Modifier.fillMaxSize().padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(stringResource(Res.string.mic_diagnostic), fontSize = 26.sp, fontWeight = FontWeight.Bold)

            Spacer(modifier = Modifier.height(30.dp))

            Text(
                text = when(testState) {
                    MicTestState.WAITING -> stringResource(Res.string.say_something)
                    MicTestState.RECORDING -> stringResource(Res.string.recording_voice)
                    MicTestState.PLAYING -> stringResource(Res.string.playing_back)
                    MicTestState.FINISHED -> stringResource(Res.string.did_you_hear_voice)
                },
                fontSize = 18.sp,
                color = if (testState == MicTestState.RECORDING) Color.Red else Color.Gray,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(30.dp))

            if (testState == MicTestState.RECORDING) {
                LinearProgressIndicator(
                    progress = { recordingProgress },
                    modifier = Modifier.fillMaxWidth().height(8.dp),
                    color = Color.Red,
                    trackColor = Color.LightGray
                )
                Spacer(modifier = Modifier.height(20.dp))
            }

            // إطار المؤشر
            Box(
                modifier = Modifier
                    .width(90.dp)
                    .height(250.dp)
                    .background(Color(0xFFF2F2F2), RoundedCornerShape(45.dp)),
                contentAlignment = Alignment.BottomCenter
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(animatedHeight)
                        .background(
                            if (testState == MicTestState.RECORDING) Color.Red else Color(0xFF2196F3),
                            RoundedCornerShape(45.dp)
                        ),
                    contentAlignment = Alignment.TopCenter
                ) {
                    Icon(
                        Icons.Default.KeyboardArrowUp,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(45.dp).padding(top = 10.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            if (testState == MicTestState.FINISHED) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                    Button(
                        onClick = {
                            viewModel.finishMicTest(true)
                            onFinished()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
                    ) {
                        Text(stringResource(Res.string.yes_clear))
                    }
                    Button(
                        onClick = {
                            viewModel.finishMicTest(false)
                            onFinished()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                    ) {
                        Text(stringResource(Res.string.no_silent))
                    }
                }
            } else {
                Text(
                    text = "Amplitude: $amplitude",
                    fontSize = 12.sp,
                    color = Color.LightGray
                )
            }
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
    }}
}
