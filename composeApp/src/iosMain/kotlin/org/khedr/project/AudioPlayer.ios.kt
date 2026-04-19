package org.khedr.project

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import kotlinx.cinterop.ExperimentalForeignApi
import platform.AVFoundation.*
import platform.Foundation.*

class IosAudioPlayer : AudioPlayer {
    private var player: AVAudioPlayer? = null

    override fun playSound(resId: Int?) {
        // Play default system sound for simplicity
        val url = NSBundle.mainBundle.URLForResource("default_ringtone", withExtension = "mp3") 
            ?: return
        playFile(url.path!!)
    }

    @OptIn(ExperimentalForeignApi::class)
    override fun playFile(path: String) {
        stop()
        val fullPath = if (path.startsWith("/")) path else NSTemporaryDirectory() + path
        val url = NSURL.fileURLWithPath(fullPath)
        player = AVAudioPlayer(url, null)
        player?.prepareToPlay()
        player?.play()
    }

    override fun stop() {
        player?.stop()
        player = null
    }

    override fun release() {
        stop()
    }
}

@Composable
actual fun rememberAudioPlayer(): AudioPlayer {
    val player = remember { IosAudioPlayer() }
    DisposableEffect(Unit) {
        onDispose { player.release() }
    }
    return player
}
