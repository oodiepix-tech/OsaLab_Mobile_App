package org.khedr.project

import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings

class AndroidPlatform : Platform {
    override val name: String = "Android ${Build.VERSION.SDK_INT}"

    override fun openAboutPhoneSettings() {
        AppContextHolder.context?.let { ctx ->
            val intent = Intent(Settings.ACTION_DEVICE_INFO_SETTINGS).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            ctx.startActivity(intent)
        }
    }
}

actual fun getPlatform(): Platform = AndroidPlatform()

object AppContextHolder {
    var context: Context? = null
}
