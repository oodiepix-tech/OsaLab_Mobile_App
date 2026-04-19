package org.khedr.project

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.os.Build
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import java.io.File

@Composable
actual fun rememberBatteryInfo(): BatteryInfo {
    val context = LocalContext.current
    var batteryInfo by remember { mutableStateOf(getBatteryInfo(context)) }

    DisposableEffect(context) {
        val receiver = object : android.content.BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                batteryInfo = getBatteryInfo(context!!)
            }
        }
        context.registerReceiver(receiver, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
        onDispose { context.unregisterReceiver(receiver) }
    }

    return batteryInfo
}

private fun getBatteryInfo(context: Context): BatteryInfo {
    val filter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
    val batteryStatus = context.registerReceiver(null, filter)

    val level = batteryStatus?.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) ?: -1
    val scale = batteryStatus?.getIntExtra(BatteryManager.EXTRA_SCALE, -1) ?: -1
    val batteryPct = if (scale > 0) (level * 100 / scale.toFloat()).toInt() else 0

    val status = batteryStatus?.getIntExtra(BatteryManager.EXTRA_STATUS, -1) ?: -1
    val isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING || status == BatteryManager.BATTERY_STATUS_FULL

    val voltage = batteryStatus?.getIntExtra(BatteryManager.EXTRA_VOLTAGE, 0) ?: 0
    val temp = (batteryStatus?.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 0) ?: 0) / 10f

    val designCapacity = getDesignCapacity(context)
    val realHealth = getSystemFileValue("/sys/class/power_supply/battery/fg_asoc")
    val cycles = getSystemFileValue("/sys/class/power_supply/battery/battery_cycle").toInt().takeIf { it > 0 }
        ?: getSystemFileValue("/sys/class/power_supply/battery/cycle_count").toInt()

    var actualCapacity = getSamsungRealCapacity()
    if (actualCapacity <= 0) {
        val bm = context.getSystemService(Context.BATTERY_SERVICE) as BatteryManager
        val chargeCounter = Math.abs(bm.getLongProperty(BatteryManager.BATTERY_PROPERTY_CHARGE_COUNTER).toDouble())
        if (chargeCounter > 0) actualCapacity = (chargeCounter / 1000.0)
    }

    var efficiency = when {
        realHealth > 0 -> realHealth.toInt()
        actualCapacity > 0 && designCapacity > 0 -> ((actualCapacity / designCapacity) * 100).toInt()
        else -> calculateHeuristicEfficiency(batteryPct, voltage, cycles)
    }

    if (cycles > 500) {
        val penalty = ((cycles - 500) / 40).coerceIn(0, 15)
        efficiency -= penalty
    }
    efficiency = efficiency.coerceIn(40, 100)

    val healthLabel = when {
        efficiency > 80 -> "Good"
        efficiency > 65 -> "Fair"
        else -> "Weak / Replace"
    }

    // حساب الوقت المتبقي بشكل يضمن عدم الفراغ
    val timeStr = if (isCharging) {
        if (status == BatteryManager.BATTERY_STATUS_FULL) "Full" 
        else "~${((100 - batteryPct) * 1.2).toInt()} min"
    } else {
        val hours = (batteryPct * 0.15).toInt().coerceAtLeast(0)
        val mins = ((batteryPct * 0.15 - hours) * 60).toInt().coerceAtLeast(0)
        if (hours > 0) "${hours}h ${mins}m" else "${mins}m"
    }

    return BatteryInfo(
        level = batteryPct,
        isCharging = isCharging,
        health = healthLabel,
        healthPercentage = efficiency,
        capacity = designCapacity.toInt(),
        actualCapacity = if (actualCapacity > 0) actualCapacity.toInt() else (designCapacity * (efficiency / 100.0)).toInt(),
        cycleCount = if (cycles > 0) cycles else null,
        temperature = temp,
        voltage = voltage,
        technology = batteryStatus?.getStringExtra(BatteryManager.EXTRA_TECHNOLOGY) ?: "Li-ion",
        estimatedTimeRemaining = timeStr
    )
}

private fun calculateHeuristicEfficiency(pct: Int, voltage: Int, cycles: Int): Int {
    var base = 100
    val ageInMonths = (System.currentTimeMillis() - Build.TIME) / (1000L * 60 * 60 * 24 * 30)
    base -= (ageInMonths * 0.4).toInt().coerceIn(0, 15)
    if (cycles > 0) base -= (cycles / 100) * 2
    return base.coerceIn(50, 100)
}

private fun getSystemFileValue(path: String): Double {
    return try {
        val file = File(path)
        if (file.exists()) file.readText().trim().toDouble() else 0.0
    } catch (e: Exception) { 0.0 }
}

private fun getSamsungRealCapacity(): Double {
    val paths = arrayOf(
        "/sys/class/power_supply/battery/fg_fullcapnom",
        "/sys/class/power_supply/battery/fg_full_cap",
        "/sys/class/power_supply/battery/batt_capacity_max"
    )
    for (path in paths) {
        val v = getSystemFileValue(path)
        if (v > 0) return v
    }
    return 0.0
}

private fun getDesignCapacity(context: Context): Double {
    try {
        val powerProfileClass = "com.android.internal.os.PowerProfile"
        val mPowerProfile = Class.forName(powerProfileClass).getConstructor(Context::class.java).newInstance(context)
        val cap = Class.forName(powerProfileClass).getMethod("getBatteryCapacity").invoke(mPowerProfile) as Double
        if (cap > 1000) return cap
    } catch (e: Exception) { }
    return 4500.0
}
