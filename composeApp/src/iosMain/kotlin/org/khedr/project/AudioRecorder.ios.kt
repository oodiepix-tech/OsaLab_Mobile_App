package org.khedr.project

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import kotlinx.cinterop.ExperimentalForeignApi
import platform.AVFoundation.*
import platform.Foundation.*
import platform.AudioToolbox.*

class IosAudioRecorder : AudioRecorder {
    private var recorder: AVAudioRecorder? = null
    private var timer: NSTimer? = null
    private var _maxAmplitude: Int = 0

    @OptIn(ExperimentalForeignApi::class)
    override fun start() {
        val session = AVAudioSession.sharedInstance()
        session.setCategory(AVAudioSessionCategoryPlayAndRecord, error = null)
        session.setActive(true, error = null)

        // Basic start for monitoring amplitude
        startMonitoring()
    }

    @OptIn(ExperimentalForeignApi::class)
    private fun startMonitoring() {
        val url = NSURL.fileURLWithPath(NSTemporaryDirectory() + "temp.wav")
        val settings = mapOf(
            AVFormatIDKey to kAudioFormatLinearPCM,
            AVSampleRateKey to 44100.0,
            AVNumberOfChannelsKey to 1,
            AVEncoderAudioQualityKey to AVAudioQualityHigh
        )
        
        recorder = AVAudioRecorder(url, settings as Map<Any?, *>, null)
        recorder?.prepareToRecord()
        recorder?.meteringEnabled = true
        recorder?.record()

        timer = NSTimer.scheduledTimerWithTimeInterval(0.1, true) {
            recorder?.updateMeters()
            val power = recorder?.averagePowerForChannel(0) ?: -160f
            // Convert dB to a positive linear scale (approximate to Android amplitude)
            _maxAmplitude = ((power + 160) * 200).toInt().coerceIn(0, 32767)
        }
    }

    @OptIn(ExperimentalForeignApi::class)
    override fun startRecording(outputPath: String) {
        stop()
        val url = NSURL.fileURLWithPath(NSTemporaryDirectory() + outputPath)
        val settings = mapOf(
            AVFormatIDKey to kAudioFormatMPEG4AAC,
            AVSampleRateKey to 44100.0,
            AVNumberOfChannelsKey to 1,
            AVEncoderAudioQualityKey to AVAudioQualityHigh
        )
        recorder = AVAudioRecorder(url, settings as Map<Any?, *>, null)
        recorder?.prepareToRecord()
        recorder?.meteringEnabled = true
        recorder?.record()
    }

    override fun stop() {
        timer?.invalidate()
        timer = null
        recorder?.stop()
        recorder = null
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
    val recorder = remember { IosAudioRecorder() }
    DisposableEffect(Unit) {
        onDispose { recorder.release() }
    }
    return recorder
}
