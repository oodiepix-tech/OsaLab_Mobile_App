package org.khedr.project

class JVMPlatform: Platform {
    override val name: String = "Java ${System.getProperty("java.version")}"
    
    override fun openAboutPhoneSettings() {
        // Not implemented for JVM
    }
}

actual fun getPlatform(): Platform = JVMPlatform()