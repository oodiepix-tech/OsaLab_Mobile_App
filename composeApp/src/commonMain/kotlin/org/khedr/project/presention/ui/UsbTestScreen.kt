package org.khedr.project.presention.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.stringResource
import org.khedr.project.ObserveUsbStatus
import org.khedr.project.presention.viewmodel.TestViewModel
import osacheck.composeapp.generated.resources.Res
import osacheck.composeapp.generated.resources.*

@Composable
fun UsbTestScreen(
    viewModel: TestViewModel,
    onFinished: () -> Unit
) {
    var isCharging by remember { mutableStateOf(false) }
    var plugType by remember { mutableStateOf("Unknown") }
    var isStable by remember { mutableStateOf(false) }
    var lastState by remember { mutableStateOf(false) }

    ObserveUsbStatus { chargingNow, type ->
        isCharging = chargingNow
        plugType = type

        // ✅ Check stability
        if (chargingNow && lastState == chargingNow) {
            isStable = true
        }
        lastState = chargingNow
    }

    val isPortWorking = isCharging && isStable

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(stringResource(Res.string.plug_charger))

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            if (isCharging) stringResource(Res.string.charging_detected) 
            else stringResource(Res.string.no_connection)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(stringResource(Res.string.usb_type, plugType))

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            if (isStable) stringResource(Res.string.connection_stable) 
            else stringResource(Res.string.unstable_loose)
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(onClick = {
            viewModel.finishUsbTest(isPortWorking)
            onFinished()
        },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF4A90E2),
                contentColor = Color.White
            )
        ) {
            Text(stringResource(Res.string.finish_test))
        }
    }
}
