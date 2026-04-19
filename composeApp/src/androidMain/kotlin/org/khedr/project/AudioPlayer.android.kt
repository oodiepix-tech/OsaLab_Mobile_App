package org.khedr.project

import android.media.AudioAttributes
import android.media.MediaPlayer
import android.media.Ringtone
import android.media.RingtoneManager
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import java.io.File

class AndroidAudioPlayer(private val context: android.content.Context) : AudioPlayer {
    private var ringtone: Ringtone? = null
    private var mediaPlayer: MediaPlayer? = null

    override fun playSound(resId: Int?) {
        try {
            stop()
            val uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE)
            ringtone = RingtoneManager.getRingtone(context, uri)
            ringtone?.play()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun playFile(path: String) {
        stop()
        try {
            // التأكد من المسار الكامل للملف (إذا كان في الكاش)
            val finalPath = if (!path.startsWith("/")) {
                File(context.cacheDir, path).absolutePath
            } else path

            val file = File(finalPath)
            if (!file.exists()) {
                println("AudioPlayer: File not found at $finalPath")
                return
            }

            mediaPlayer = MediaPlayer().apply {
                setDataSource(finalPath)
                setAudioAttributes(
                    AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .build()
                )
                prepare()
                start()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun stop() {
        try {
            ringtone?.stop()
        } catch (e: Exception) {}
        ringtone = null

        try {
            mediaPlayer?.stop()
            mediaPlayer?.release()
        } catch (e: Exception) {}
        mediaPlayer = null
    }

    override fun release() {
        stop()
    }
}

@Composable
actual fun rememberAudioPlayer(): AudioPlayer {
    val context = LocalContext.current
    val player = remember { AndroidAudioPlayer(context) }
    DisposableEffect(Unit) {
        onDispose { player.release() }
    }
    return player
}
