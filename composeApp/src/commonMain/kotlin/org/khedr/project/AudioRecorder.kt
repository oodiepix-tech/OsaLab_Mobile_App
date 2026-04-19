package org.khedr.project

import androidx.compose.runtime.Composable

interface AudioRecorder {
    fun start()
    fun startRecording(outputPath: String)
    fun stop()
    fun release()
    val maxAmplitude: Int
}

@Composable
expect fun rememberAudioRecorder(): AudioRecorder
