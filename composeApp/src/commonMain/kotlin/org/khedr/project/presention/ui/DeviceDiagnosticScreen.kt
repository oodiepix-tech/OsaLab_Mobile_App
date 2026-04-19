package org.khedr.project.presention.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.khedr.project.getPlatform
import org.khedr.project.navagation.Routes
import org.khedr.project.presention.model.TestStatus
import org.khedr.project.presention.viewmodel.DiagnosticViewModel
import osacheck.composeapp.generated.resources.Res
import osacheck.composeapp.generated.resources.*


@Composable
fun DeviceDiagnosticScreen(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    val viewModel: DiagnosticViewModel = viewModel()
    val platform = getPlatform()
    val focusManager = LocalFocusManager.current

    Scaffold(
        containerColor = Color.Transparent,
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color.White)
                // إغلاق الكيبورد عند الضغط في أي مكان فارغ
                .pointerInput(Unit) {
                    detectTapGestures(onTap = {
                        focusManager.clearFocus()
                    })
                }
        ) {
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 24.dp)
                    .background(Color.White),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = stringResource(Res.string.imei_info_title),
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = stringResource(Res.string.imei_info_desc),
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    onClick = {
                        platform.openAboutPhoneSettings()
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF4A90E2),
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(text = stringResource(Res.string.open_settings))
                }

                Spacer(Modifier.height(16.dp))

                OutlinedTextField(
                    value = viewModel.imei,
                    onValueChange = viewModel::onImeiChange,
                    label = { Text(stringResource(Res.string.enter_imei), color = Color.Black) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    // خيارات الكيبورد (أرقام + زر Done)
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done
                    ),
                    // تنفيذ إغلاق الكيبورد عند الضغط على Done
                    keyboardActions = KeyboardActions(
                        onDone = { focusManager.clearFocus() }
                    ),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF4A90E2),
                        unfocusedBorderColor = Color(0xFF4A90E2)
                    )
                )

                Spacer(Modifier.height(12.dp))

                Button(
                    onClick = {
                        focusManager.clearFocus() // إغلاق الكيبورد عند الضغط على زر الفحص
                        viewModel.checkDevice()
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF4A90E2),
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(stringResource(Res.string.check_device))
                }

                Spacer(Modifier.height(12.dp))

                if (!viewModel.isRegistered) {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFE4E1))
                    ) {
                        Text(
                            text = stringResource(Res.string.imei_not_registered),
                            modifier = Modifier.padding(12.dp),
                            color = Color.Red
                        )
                    }
                }

                Spacer(Modifier.height(16.dp))

                viewModel.device?.let {
                    DeviceCard(it, onNextClick = {
                        focusManager.clearFocus()
                        navController.navigate(Routes.TEST_LIST)
                    })
                }
            }
        }
    }
}

@Composable
fun DeviceCard(device: DeviceInfo, onNextClick: () -> Unit = {}) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(6.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(modifier = Modifier.padding(16.dp)) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .background(Color.LightGray, RoundedCornerShape(12.dp))
            ) {
                Image(painter = painterResource(Res.drawable.ic_test), contentDescription = null)
            }

            Spacer(Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(device.name, fontWeight = FontWeight.Bold)
                Text(stringResource(Res.string.storage, device.storage))
                Text(stringResource(Res.string.color, device.color))

                Spacer(Modifier.height(8.dp))

                Button(
                    onClick = onNextClick,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF4A90E2),
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(stringResource(Res.string.next))
                }
            }
        }
    }
}

data class DeviceInfo(
    val name: String,
    val storage: String,
    val color: String
)

@Composable
fun TestCard(
    test: DeviceTestItem,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val backgroundColor = when (test.status) {
        TestStatus.PENDING -> Color.White
        TestStatus.PASSED -> Color(0xFF4A90E2)
        TestStatus.FAILED -> Color(0xFFFF5252)
    }

    val contentColor = when (test.status) {
        TestStatus.PENDING -> Color.Black
        else -> Color.White
    }

    val cardTitle = when (test.id) {
        "touch" -> stringResource(Res.string.test_touch)
        "color" -> stringResource(Res.string.test_color)
        "battery" -> stringResource(Res.string.test_battery)
        "usb" -> stringResource(Res.string.test_usb)
        "speaker" -> stringResource(Res.string.test_speaker)
        "mic" -> stringResource(Res.string.test_mic)
        else -> test.title
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .then(
                if (test.status == TestStatus.PENDING) 
                    Modifier.border(1.dp, Color(0xFFE0E0E0), RoundedCornerShape(18.dp))
                else Modifier
            ),
        shape = RoundedCornerShape(18.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        onClick = onClick,
        colors = CardDefaults.cardColors(containerColor = backgroundColor)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(14.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(
                                if (test.status == TestStatus.PENDING)
                                    Color(0xFFF2F4F7)
                                else
                                    Color.White.copy(alpha = 0.2f)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = getEmojiForTitle(test.id),
                            fontSize = 18.sp
                        )
                    }

                    when (test.status) {
                        TestStatus.PASSED -> StatusCircle("✔", Color(0xFF4CAF50))
                        TestStatus.FAILED -> StatusCircle("✖", Color.Red)
                        TestStatus.PENDING -> {}
                    }
                }

                Text(
                    text = cardTitle,
                    color = contentColor,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    lineHeight = 18.sp
                )
            }
        }
    }
}

@Composable
fun StatusCircle(text: String, color: Color) {
    Box(
        modifier = Modifier
            .size(22.dp)
            .clip(CircleShape)
            .background(color),
        contentAlignment = Alignment.Center
    ) {
        Text(text = text, color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
    }
}

fun getEmojiForTitle(id: String): String {
    return when (id) {
        "touch" -> "✋"
        "color" -> "🎨"
        "battery" -> "🔋"
        "usb" -> "🔌"
        "speaker" -> "🔊"
        "mic" -> "🎤"
        else -> "📱"
    }
}

data class DeviceTestItem(val id: String, val title: String, val status: TestStatus)
