package com.sargis.composekmp

import DATA_STORE_FILE_NAME
import androidx.compose.runtime.remember
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import createDataStore
import di.initKoin
import io.ktor.client.engine.okhttp.OkHttp
import networking.InsultCensorClient
import networking.createHttpClient

fun main() {

    initKoin()

    // TODO in real app it will not be like this
    val prefs = createDataStore {
        DATA_STORE_FILE_NAME
    }

    application {
        Window(
            onCloseRequest = ::exitApplication,
            title = "ComposeKMP",
        ) {
            App(
//                prefs = remember {
//                    prefs
//                },
                prefs = prefs, // cause prefs is created out of composable file
                client = remember {
                    InsultCensorClient(createHttpClient(OkHttp.create()))
                },
                batteryManager = BatteryManager()
            )
        }
    }
}