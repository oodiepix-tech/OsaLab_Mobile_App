package org.khedr.project

import androidx.compose.runtime.Composable

interface ImagePicker {
    fun pickImage(onImagePicked: (ByteArray?) -> Unit)
}

@Composable
expect fun rememberImagePicker(onImagePicked: (ByteArray?) -> Unit): ImagePicker
