package org.khedr.project

import androidx.compose.runtime.Composable

@Composable
expect fun RequestRecordAudioPermission(onPermissionGranted: () -> Unit)
