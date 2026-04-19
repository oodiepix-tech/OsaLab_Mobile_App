package org.khedr.project

import android.annotation.SuppressLint
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import android.os.Build
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import java.io.File
import kotlin.concurrent.thread

class AndroidAudioRecorder(private val context: android.content.Context) : AudioRecorder {
    private var audioRecord: AudioRecord? = null
    private var mediaRecorder: MediaRecorder? = null
    @Volatile private var isRecording = false
    @Volatile private var _maxAmplitude = 0

    @SuppressLint("MissingPermission")
    override fun start() {
        if (isRecording) return
        try {
            val sampleRate = 44100
            val bufferSize = AudioRecord.getMinBufferSize(sampleRate, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT)
            
            audioRecord = AudioRecord(
                MediaRecorder.AudioSource.MIC,
                sampleRate,
                AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.ENCODING_PCM_16BIT,
                bufferSize.coerceAtLeast(2048)
            )

            if (audioRecord?.state == AudioRecord.STATE_INITIALIZED) {
                audioRecord?.startRecording()
                isRecording = true
                startAmplitudeThread()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun startAmplitudeThread() {
        thread(isDaemon = true) {
            val buffer = ShortArray(2048)
            while (isRecording && audioRecord != null) {
                try {
                    val readSize = audioRecord?.read(buffer, 0, buffer.size) ?: 0
                    if (readSize > 0) {
                        var max = 0
                        for (i in 0 until readSize) {
                            val abs = Math.abs(buffer[i].toInt())
                            if (abs > max) max = abs
                        }
                        _maxAmplitude = max
                    }
                } catch (e: Exception) { break }
            }
        }
    }

    override fun startRecording(outputPath: String) {
        stop() // Stop AudioRecord first
        try {
            val finalPath = if (!outputPath.startsWith("/")) {
                File(context.cacheDir, outputPath).absolutePath
            } else outputPath

            val file = File(finalPath)
            if (file.exists()) file.delete()

            mediaRecorder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                MediaRecorder(context)
            } else {
                @Suppress("DEPRECATION")
                MediaRecorder()
            }.apply {
                setAudioSource(MediaRecorder.AudioSource.MIC)
                setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
                setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
                setOutputFile(finalPath)
                prepare()
                start()
            }
            isRecording = true
            
            // Monitor amplitude during MediaRecorder session
            thread(isDaemon = true) {
                while (isRecording && mediaRecorder != null) {
                    try {
                        _maxAmplitude = mediaRecorder?.maxAmplitude ?: 0
                    } catch (e: Exception) { }
                    Thread.sleep(100)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun stop() {
        isRecording = false
        try {
            audioRecord?.stop()
            audioRecord?.release()
        } catch (e: Exception) {}
        audioRecord = null

        try {
            mediaRecorder?.stop()
            mediaRecorder?.release()
        } catch (e: Exception) {}
        mediaRecorder = null
        _maxAmplitude = 0
    }

    override fun release() {
        stop()
    }

    override val maxAmplitude: Int
        get() = _maxAmplitude
}

@Composable
actual fun rememberAudioRecorder(): AudioRecorder {
    val context = LocalContext.current
    val recorder = remember { AndroidAudioRecorder(context) }
    DisposableEffect(Unit) {
        onDispose { recorder.release() }
    }
    return recorder
}
