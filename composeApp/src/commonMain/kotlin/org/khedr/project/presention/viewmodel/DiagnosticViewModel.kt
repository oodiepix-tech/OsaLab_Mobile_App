package org.khedr.project.presention.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import org.khedr.project.presention.ui.DeviceInfo

class DiagnosticViewModel : ViewModel() {

    var imei by mutableStateOf("")
        private set

    var device by mutableStateOf<DeviceInfo?>(null)
        private set

    var isRegistered by mutableStateOf(true)
        private set

    fun onImeiChange(value: String) {
        imei = value
    }

    fun checkDevice() {
        // 🔥 هنا تحط API أو logic
        if (imei.length < 10) {
            isRegistered = false
            device = null
        } else {
            isRegistered = true
            device = DeviceInfo(
                name = "Samsung A50",
                storage = "128 GB",
                color = "Black"
            )
        }
    }
}