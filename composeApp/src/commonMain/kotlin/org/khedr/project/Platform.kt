package org.khedr.project

interface Platform {
    val name: String
    fun openAboutPhoneSettings()
}

expect fun getPlatform(): Platform