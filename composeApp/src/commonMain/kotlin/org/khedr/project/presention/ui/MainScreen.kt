package org.khedr.project.presention.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import org.khedr.project.navagation.Routes
import org.khedr.project.presention.ui.components.FancyBottomBar
import org.khedr.project.presention.viewmodel.TestViewModel


@Composable
fun MainScreen(navController: NavHostController, viewModel: TestViewModel) {
    var selectedBottom by rememberSaveable { mutableStateOf(0) }
    Scaffold(
    ) { innerPadding ->

//        Box(Modifier.padding(innerPadding)) {
//            when (selectedBottom) {
//                0 -> DeviceDiagnosticScreen(navController = navController, onClick = {
//                   selectedBottom = 1
//                })
//                1 -> DeviceTestScreen(navController = navController,
//                    viewModel = viewModel
//                )
//            }
//
//        }
    }


}