package org.khedr.project

import androidx.compose.runtime.Composable

@Composable
expect fun ObserveUsbStatus(onStatusChanged: (isCharging: Boolean, plugType: String) -> Unit)
