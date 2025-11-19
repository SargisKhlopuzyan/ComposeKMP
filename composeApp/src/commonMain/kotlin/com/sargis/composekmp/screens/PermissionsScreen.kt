package com.sargis.composekmp.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sargis.composekmp.PermissionsViewModel
import dev.icerock.moko.permissions.PermissionState
import dev.icerock.moko.permissions.compose.BindEffect
import dev.icerock.moko.permissions.compose.rememberPermissionsControllerFactory

@Composable
fun PermissionsScreen() {
    //permission
    val factory = rememberPermissionsControllerFactory()
    val controller = remember(factory) {
        factory.createPermissionsController()
    }

    BindEffect(controller)

    val viewModel = viewModel {
        PermissionsViewModel(controller)
    }

    Column(
        modifier = Modifier.wrapContentHeight().fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when (viewModel.state) {
            PermissionState.Granted -> {
                Text(text = "Request Audio permission granted!")
            }

            PermissionState.DeniedAlways -> {
                Text(text = "Request Audio permission was permanently denied.")
                Button(onClick = {
                    controller.openAppSettings()
                }) {
                    Text(text = "Open App Settings")
                }
            }

            else -> {
                Button(onClick = {
                    viewModel.provideRequestRecordAudioPermission()
                }) {
                    Text(text = "Request Audio permission")
                }
            }
        }
    }
}