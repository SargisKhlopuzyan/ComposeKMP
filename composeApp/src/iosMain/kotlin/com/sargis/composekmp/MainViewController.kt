package com.sargis.composekmp

import androidx.compose.runtime.remember
import androidx.compose.ui.window.ComposeUIViewController
import createDataStore
import di.initKoin
import io.ktor.client.engine.darwin.Darwin
import networking.InsultCensorClient
import networking.createHttpClient

fun MainViewController() = ComposeUIViewController(
    configure = {
        initKoin()
    }
) {
    val prefs = createDataStore()

    App(
        prefs = remember {
            prefs
        },
        client = remember {
            InsultCensorClient(createHttpClient(Darwin.create()))
        },
        batteryManager = remember {
            BatteryManager()
        }
    )
}