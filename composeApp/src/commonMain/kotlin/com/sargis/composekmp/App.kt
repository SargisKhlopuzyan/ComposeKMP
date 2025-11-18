package com.sargis.composekmp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
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
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import composekmp.composeapp.generated.resources.Res
import composekmp.composeapp.generated.resources.test
import dependencies.MyViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
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

                HomeScreen(prefs, client, batteryManager, viewModel.getHelloWorldString())
            }
        }

    }
}

@Composable
fun HomeScreen(
    prefs: DataStore<Preferences>,
    client: InsultCensorClient,
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
        // dataStore part
        val counterKey = intPreferencesKey("counter")
        val counter by prefs
            .data
            .map {
                it[counterKey] ?: 0
            }
            .collectAsState(initial = 0)

        // censored part
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

        LazyColumn(modifier = Modifier.fillMaxSize()) {

            item {
                Text(text = "$counter")
            }
            item {
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

            item {
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
            }
            item {
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
            }

            censoredText?.let {
                item {
                    Text(it)
                }
            }

            errorMessage?.let {
                item {
                    Text(
                        text = it.name,
                        color = Color.Red
                    )
                }
            }

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