package org.khedr.project

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalContext

@Composable
actual fun ObserveUsbStatus(onStatusChanged: (isCharging: Boolean, plugType: String) -> Unit) {
    val context = LocalContext.current

    DisposableEffect(context) {
        val receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                val status = intent?.getIntExtra(BatteryManager.EXTRA_STATUS, -1)
                val plugged = intent?.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1)

                val chargingNow = status == BatteryManager.BATTERY_STATUS_CHARGING ||
                        status == BatteryManager.BATTERY_STATUS_FULL

                val plugType = when (plugged) {
                    BatteryManager.BATTERY_PLUGGED_USB -> "USB"
                    BatteryManager.BATTERY_PLUGGED_AC -> "AC ⚡"
                    BatteryManager.BATTERY_PLUGGED_WIRELESS -> "Wireless"
                    else -> "Not Connected"
                }

                onStatusChanged(chargingNow, plugType)
            }
        }

        val filter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        context.registerReceiver(receiver, filter)

        onDispose {
            context.unregisterReceiver(receiver)
        }
    }
}
