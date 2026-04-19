package org.khedr.project

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

@Composable
actual fun rememberBatteryInfo(): BatteryInfo {
    return remember {
        BatteryInfo(
            level = 100,
            isCharging = false,
            health = "Good (Desktop)",
            capacity = 5000,
            cycleCount = null,
            temperature = 30f,
            voltage = 12000,
            technology = "AC"
        )
    }
}
