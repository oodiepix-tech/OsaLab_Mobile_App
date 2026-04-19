package org.khedr.project

import androidx.compose.runtime.Composable

data class BatteryInfo(
    val level: Int,
    val isCharging: Boolean,
    val health: String,
    val healthPercentage: Int?,
    val capacity: Int, // السعة التصميمية (مثلاً 4500)
    val actualCapacity: Int?, // السعة الفعلية الحالية (مثلاً 3800)
    val cycleCount: Int?,
    val temperature: Float,
    val voltage: Int,
    val technology: String,
    val estimatedTimeRemaining: String?
)

@Composable
expect fun rememberBatteryInfo(): BatteryInfo
