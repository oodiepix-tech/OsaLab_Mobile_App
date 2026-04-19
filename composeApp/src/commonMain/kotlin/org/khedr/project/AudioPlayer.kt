package org.khedr.project

import androidx.compose.runtime.Composable

interface AudioPlayer {
    fun playSound(resId: Int? = null)
    fun playFile(path: String)
    fun stop()
    fun release()
}

@Composable
expect fun rememberAudioPlayer(): AudioPlayer
