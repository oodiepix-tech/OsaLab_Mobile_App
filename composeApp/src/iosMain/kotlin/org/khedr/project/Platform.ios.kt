package org.khedr.project

import platform.Foundation.NSURL
import platform.UIKit.UIApplication
import platform.UIKit.UIDevice
import platform.UIKit.UIScreen

class IOSPlatform: Platform {
    override val name: String = UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion

    override fun openAboutPhoneSettings() {
        // "App-Prefs:General&path=About" is a common scheme to open the About page.
        // If it doesn't work on certain versions, it will fallback to opening the main settings.
        val url = NSURL.URLWithString("App-Prefs:General&path=About") ?: NSURL.URLWithString(platform.UIKit.UIApplicationOpenSettingsURLString)
        url?.let {
            UIApplication.sharedApplication.openURL(it)
        }
    }
}

actual fun getPlatform(): Platform = IOSPlatform()