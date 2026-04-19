package org.khedr.project

class WasmPlatform: Platform {
    override val name: String = "Web with Kotlin/Wasm"

    override fun openAboutPhoneSettings() {
        // Not implemented
    }
}

actual fun getPlatform(): Platform = WasmPlatform()