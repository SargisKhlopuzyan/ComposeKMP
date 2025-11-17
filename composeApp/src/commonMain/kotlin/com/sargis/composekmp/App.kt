package com.sargis.composekmp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import composekmp.composeapp.generated.resources.Res
import composekmp.composeapp.generated.resources.test
import dependencies.MyViewModel
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
@Preview
fun App(
    batteryManager: BatteryManager
) {
    MaterialTheme {
        NavHost(
            navController = rememberNavController(),
            startDestination = "home"
        ) {
            composable(
                route = "home"
            ) {
                val viewModel: MyViewModel = koinViewModel()

                HomeScreen(batteryManager, viewModel.getHelloWorldString())
            }
        }

    }
}

@Composable
fun HomeScreen(
    batteryManager: BatteryManager,
    viewModelText: String
) {
    Box(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.primaryContainer)
            .safeContentPadding()
            .fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            item {
                Text("The viewModelText: $viewModelText")
            }
            item {
                Text("The current battery level is ${batteryManager.getBatteryLevel()}")
            }
            item {
                Text("Common text : ${stringResource(Res.string.test)}")
            }
            items(count = 4) {
                Text("Item : $it")
            }
        }

//            Column(modifier = Modifier.fillMaxSize()) {
//                Text("The current battery level is ${batteryManager.getBatteryLevel()}")
//                Text("Common text : ${stringResource(Res.string.test)}")
//            }
    }
}