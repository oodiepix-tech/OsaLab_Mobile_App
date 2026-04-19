package org.khedr.project

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect

@Composable
actual fun RequestRecordAudioPermission(onPermissionGranted: () -> Unit) {
    LaunchedEffect(Unit) {
        onPermissionGranted()
    }
}
