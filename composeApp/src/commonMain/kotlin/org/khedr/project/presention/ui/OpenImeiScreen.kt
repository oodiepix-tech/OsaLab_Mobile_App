package org.khedr.project.presention.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import org.jetbrains.compose.resources.stringResource
import org.khedr.project.getPlatform
import org.khedr.project.rememberImagePicker
import org.khedr.project.navagation.Routes
import osacheck.composeapp.generated.resources.Res
import osacheck.composeapp.generated.resources.*

@Composable
fun OpenImeiScreen(navController: NavHostController) {
    val platform = getPlatform()
    var isImageUploaded by remember { mutableStateOf(false) }
    
    val imagePicker = rememberImagePicker { bytes ->
        if (bytes != null) {
            // Here you would normally upload the bytes to your server
            isImageUploaded = true
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        )
        {
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
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF4A90E2),
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = stringResource(Res.string.open_settings),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (!isImageUploaded) {
                Button(
                    onClick = {
                        imagePicker.pickImage { bytes ->
                            if (bytes != null) isImageUploaded = true
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Gray,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = stringResource(Res.string.upload_screenshot),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            } else {
                Text(
                    text = stringResource(Res.string.screenshot_uploaded),
                    color = Color(0xFF4CAF50),
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
                
                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        navController.navigate(Routes.DIAGNOSTIC)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF4A90E2),
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = stringResource(Res.string.continue_to_diagnostic),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}
