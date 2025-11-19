package com.sargis.composekmp.screens

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.sargis.composekmp.BatteryManager

@Composable
fun BatteryLevelScreen(
    batteryManager: BatteryManager
) {
    Text(
        modifier = Modifier.fillMaxWidth(),
        text = "The current battery level is ${batteryManager.getBatteryLevel()}"
    )
}