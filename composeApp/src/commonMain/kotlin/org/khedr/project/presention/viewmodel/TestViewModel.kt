package org.khedr.project.presention.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import org.khedr.project.navagation.Routes
import org.khedr.project.presention.model.TestStatus
import org.khedr.project.presention.ui.DeviceTestItem

class TestViewModel : ViewModel() {

    var tests by mutableStateOf(listOf<DeviceTestItem>())
        private set

    init {
        loadTests()
    }

    private fun loadTests() {
        tests = listOf(
            DeviceTestItem("touch", "test_touch", TestStatus.PENDING),
            DeviceTestItem("color", "test_color", TestStatus.PENDING),
            DeviceTestItem("battery", "test_battery", TestStatus.PENDING),
            DeviceTestItem("usb", "test_usb", TestStatus.PENDING),
            DeviceTestItem("speaker", "test_speaker", TestStatus.PENDING),
            DeviceTestItem("mic", "test_mic", TestStatus.PENDING)
        )
    }
    fun getRouteFromTest(id: String): String {
        return when (id) {
            "touch" -> Routes.TOUCH
            "color" -> Routes.COLOR
            "battery" -> Routes.BATTERY
            "usb" -> Routes.USB
            "speaker" -> Routes.SPEAKER
            "mic" -> Routes.MIC
            else -> Routes.TEST_LIST
        }
    }

    fun finishTouchTest(passed: Boolean) {
        updateTest("touch", if (passed) TestStatus.PASSED else TestStatus.FAILED)
    }

    fun finishColorTest(passed: Boolean) {
        updateTest("color", if (passed) TestStatus.PASSED else TestStatus.FAILED)
    }


    fun finishBatteryTest(passed: Boolean) {
        updateTest("battery", if (passed) TestStatus.PASSED else TestStatus.FAILED)
    }

    fun finishUsbTest(passed: Boolean) {
        updateTest("usb", if (passed) TestStatus.PASSED else TestStatus.FAILED)
    }

    fun finishSpeakerTest(passed: Boolean) {
        updateTest("speaker", if (passed) TestStatus.PASSED else TestStatus.FAILED)
    }

    fun finishMicTest(passed: Boolean) {
        updateTest("mic", if (passed) TestStatus.PASSED else TestStatus.FAILED)
    }

    private fun updateTest(id: String, status: TestStatus) {
        tests = tests.map {
            if (it.id == id) it.copy(status = status) else it
        }
    }
}
