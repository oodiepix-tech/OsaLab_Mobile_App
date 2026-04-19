package org.khedr.project

import androidx.compose.runtime.Composable

@Composable
actual fun ObserveUsbStatus(onStatusChanged: (isCharging: Boolean, plugType: String) -> Unit) {
    // No-op for iOS for now or implement using UIDevice
}
