package org.khedr.project

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext

@Composable
actual fun rememberImagePicker(onImagePicked: (ByteArray?) -> Unit): ImagePicker {
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            val bytes = context.contentResolver.openInputStream(it)?.readBytes()
            onImagePicked(bytes)
        }
    }
    return remember {
        object : ImagePicker {
            override fun pickImage(onImagePicked: (ByteArray?) -> Unit) {
                launcher.launch("image/*")
            }
        }
    }
}
