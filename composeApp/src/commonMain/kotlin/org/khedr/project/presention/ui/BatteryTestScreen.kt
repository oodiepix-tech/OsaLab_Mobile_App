package org.khedr.project.presention.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.stringResource
import org.khedr.project.rememberBatteryInfo
import org.khedr.project.presention.viewmodel.TestViewModel
import osacheck.composeapp.generated.resources.Res
import osacheck.composeapp.generated.resources.*

@Composable
fun BatteryTestScreen(
    viewModel: TestViewModel,
    onFinished: () -> Unit
) {
    val batteryInfo = rememberBatteryInfo()

    Scaffold(
        contentColor = Color.Transparent
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(color = Color.White)
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(Res.string.battery_diagnostic),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            // كفاءة البطارية (Efficiency)
            BatteryEfficiencyCard(batteryInfo.healthPercentage)

            Spacer(modifier = Modifier.height(20.dp))

            // مدة العمل المتوقعة - حل مشكلة العرض
            val timeRemaining = batteryInfo.estimatedTimeRemaining ?: ""
            if (timeRemaining.isNotEmpty()) {
                Text(
                    text = stringResource(Res.string.estimated_usage, timeRemaining),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF2196F3)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Details List
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    InfoRow(stringResource(Res.string.battery_status), if (batteryInfo.isCharging) stringResource(Res.string.charging) else stringResource(Res.string.discharging), if(batteryInfo.isCharging) Color(0xFF2196F3) else Color.Gray)
                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                    
                    InfoRow(stringResource(Res.string.health_state), batteryInfo.health, if(batteryInfo.health == "Good") Color(0xFF4CAF50) else Color.Red)
                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                    
                    InfoRow(stringResource(Res.string.current_charge), "${batteryInfo.level}%", Color.Black)
                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                    
                    InfoRow(stringResource(Res.string.cycles_count), batteryInfo.cycleCount?.toString() ?: stringResource(Res.string.not_supported), Color.DarkGray)
                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                    
                    InfoRow(stringResource(Res.string.temperature), "${batteryInfo.temperature} °C", Color.DarkGray)
                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                    
                    InfoRow(stringResource(Res.string.voltage), "${batteryInfo.voltage} mV", Color.DarkGray)
                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                    // عرض السعة الأصلية vs الفعلية
                    InfoRow(stringResource(Res.string.max_capacity), "${batteryInfo.capacity} mAh", Color.Blue)
                    batteryInfo.actualCapacity?.let {
                        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                        InfoRow("Real Capacity", "$it mAh", Color(0xFF4A90E2))
                    }
                }
            }

            Spacer(modifier = Modifier.height(30.dp))

            Button(
                onClick = {
                    viewModel.finishBatteryTest(batteryInfo.health == "Good")
                    onFinished()
                },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4A90E2))
            ) {
                Text(stringResource(Res.string.complete_diagnostic))
            }
        }
    }
}

@Composable
fun BatteryEfficiencyCard(percentage: Int?) {
    val color = when {
        percentage == null -> Color.Gray
        percentage >= 80 -> Color(0xFF4CAF50)
        percentage >= 65 -> Color(0xFFFFC107)
        else -> Color.Red
    }

    Box(
        modifier = Modifier
            .size(170.dp)
            .background(color.copy(alpha = 0.1f), RoundedCornerShape(85.dp)),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(stringResource(Res.string.battery_efficiency), fontSize = 14.sp, color = Color.Gray)
            Text(
                text = percentage?.let { "$it%" } ?: "--",
                fontSize = 48.sp,
                fontWeight = FontWeight.ExtraBold,
                color = color
            )
            Text(
                text = if (percentage != null && percentage >= 80) stringResource(Res.string.healthy) else stringResource(Res.string.service_needed),
                fontSize = 12.sp,
                color = color.copy(alpha = 0.8f)
            )
        }
    }
}

@Composable
fun InfoRow(label: String, value: String, valueColor: Color) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, fontWeight = FontWeight.Medium, color = Color.Gray)
        Text(text = value, fontWeight = FontWeight.Bold, color = valueColor)
    }
}
