package org.khedr.project

class JsPlatform: Platform {
    override val name: String = "Web with Kotlin/JS"

    override fun openAboutPhoneSettings() {
        // Not implemented
    }
}

actual fun getPlatform(): Platform = JsPlatform()