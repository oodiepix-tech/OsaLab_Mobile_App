package org.khedr.project.navagation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import org.khedr.project.presention.ui.BatteryTestScreen
import org.khedr.project.presention.ui.ColorTestScreen
import org.khedr.project.presention.ui.DeviceDiagnosticScreen
import org.khedr.project.presention.ui.DeviceTestScreen
import org.khedr.project.presention.ui.MainScreen
import org.khedr.project.presention.ui.MicTestScreen
import org.khedr.project.presention.ui.OpenImeiScreen
import org.khedr.project.presention.ui.SpeakerTestScreen
import org.khedr.project.presention.ui.TouchTestScreen
import org.khedr.project.presention.ui.UsbTestScreen
import org.khedr.project.presention.viewmodel.TestViewModel


@Composable
fun AppNavHost(
    navController: NavHostController,
    viewModel: TestViewModel
) {

    NavHost(
        navController = navController,
        startDestination = Routes.DIAGNOSTIC
    ) {

        composable(Routes.MAIN) {
            MainScreen(navController,
                viewModel = viewModel )
        }




        composable(Routes.TEST_LIST) {
            DeviceTestScreen(
                navController = navController,
                viewModel = viewModel
            )
        }

        composable(Routes.TOUCH) {
            TouchTestScreen(
                viewModel = viewModel,
                onFinished = {
                    navController.popBackStack()
                }
            )
        }
        composable(Routes.DIAGNOSTIC) {
            DeviceDiagnosticScreen(navController = navController)
        }

        composable(Routes.COLOR) {
            ColorTestScreen(
                viewModel = viewModel,
                onFinished = {
                    navController.popBackStack()
                }
            )
        }

        composable(Routes.BATTERY) {
            BatteryTestScreen(
                viewModel = viewModel,
                onFinished = { navController.popBackStack() }
            )
        }

        composable(Routes.USB) {
            UsbTestScreen(
                viewModel = viewModel,
                onFinished = { navController.popBackStack() }
            )
        }

        composable(Routes.SPEAKER) {
            SpeakerTestScreen(
                viewModel = viewModel,
                onFinished = { navController.popBackStack() }
            )
        }

        composable(Routes.MIC) {
            MicTestScreen(
                viewModel = viewModel,
                onFinished = { navController.popBackStack() }
            )
        }

        composable(Routes.OPEN_IMEI) {
            OpenImeiScreen(
                navController = navController
            )
        }
    }
}
