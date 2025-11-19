package com.sargis.composekmp

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.snapping.SnapPosition
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import composekmp.composeapp.generated.resources.Res
import composekmp.composeapp.generated.resources.test
import dependencies.MyViewModel
import dev.icerock.moko.permissions.PermissionState
import dev.icerock.moko.permissions.compose.BindEffect
import dev.icerock.moko.permissions.compose.rememberPermissionsControllerFactory
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import networking.InsultCensorClient
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import util.NetworkError
import util.onError
import util.onSuccess

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

@Composable
fun DataStoreScreen(
    prefs: DataStore<Preferences>
) {
    val counterKey = intPreferencesKey("counter")
    val counter by prefs
        .data
        .map {
            it[counterKey] ?: 0
        }
        .collectAsState(initial = 0)

    val scope = rememberCoroutineScope()

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(text = "$counter")
        Button(onClick = {
            scope.launch {
//                        counter += 1
                prefs.edit {
                    it[counterKey] = counter + 1
                }
            }
        }) {
            Text(text = "Increment")
        }
    }
}

@Composable
fun KtorScreen(
    client: InsultCensorClient
) {
    var censoredText by remember {
        mutableStateOf<String?>(null)
    }
    var uncensoredText by remember {
        mutableStateOf("")
    }
    var isLoading by remember {
        mutableStateOf(false)
    }
    var errorMessage by remember {
        mutableStateOf<NetworkError?>(null)
    }
    val scope = rememberCoroutineScope()

    Column(modifier = Modifier.fillMaxWidth()) {
        TextField(
            value = uncensoredText,
            onValueChange = {
                uncensoredText = it
            },
            placeholder = {
                Text("Uncensored text")
            },
            modifier = Modifier.padding(16.dp)
        )

        Button(onClick = {
            scope.launch {
                isLoading = true
                errorMessage = null
                client.censorWords(uncensoredText)
                    .onSuccess {
                        errorMessage = null
                        censoredText = it
                        isLoading = false
                    }.onError {
                        errorMessage = it
                        isLoading = false
                        censoredText = null
                    }
            }
        }) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.padding(16.dp),
                    strokeWidth = 1.dp,
                    color = Color.White
                )
            } else {
                Text(text = "Censor!")
            }
        }

        censoredText?.let {
            Text(it)
        }

        errorMessage?.let {
            Text(
                text = it.name,
                color = Color.Red
            )
        }
    }
}

@Composable
fun BatteryLevelScreen(
    batteryManager: BatteryManager
) {
    Text(
        modifier = Modifier.fillMaxWidth(),
        text = "The current battery level is ${batteryManager.getBatteryLevel()}"
    )
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