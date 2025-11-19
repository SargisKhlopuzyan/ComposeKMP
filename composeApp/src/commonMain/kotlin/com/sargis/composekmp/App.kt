package com.sargis.composekmp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.sargis.composekmp.screens.BatteryLevelScreen
import com.sargis.composekmp.screens.DataStoreScreen
import com.sargis.composekmp.screens.DateTimeScreen
import com.sargis.composekmp.screens.KtorScreen
import com.sargis.composekmp.screens.PermissionsScreen
import composekmp.composeapp.generated.resources.Res
import composekmp.composeapp.generated.resources.test
import dependencies.MyViewModel
import networking.InsultCensorClient
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
@Preview
fun App(
    prefs: DataStore<Preferences>,
    client: InsultCensorClient,
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

                Box(
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.primaryContainer)
                        .safeContentPadding()
                        .fillMaxSize()
                ) {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        item {
                            DateTimeScreen()
                        }
                        item {
                            PermissionsScreen()
                        }
                        item {
                            DataStoreScreen(prefs)
                        }
                        item {
                            KtorScreen(client)
                        }
                        item {
                            BatteryLevelScreen(batteryManager)
                        }
                        item {
                            HomeScreen(viewModel.getHelloWorldString())
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun HomeScreen(
    viewModelText: String
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text("The viewModelText: $viewModelText")
        Text("Common text : ${stringResource(Res.string.test)}")
    }
}