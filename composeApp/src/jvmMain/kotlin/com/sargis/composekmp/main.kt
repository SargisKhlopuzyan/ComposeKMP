package com.sargis.composekmp

import androidx.compose.runtime.remember
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import di.initKoin
import io.ktor.client.engine.okhttp.OkHttp
import networking.InsultCensorClient
import networking.createHttpClient

fun main() {
    initKoin()
    application {
        Window(
            onCloseRequest = ::exitApplication,
            title = "ComposeKMP",
        ) {
            App(
                client = remember {
                    InsultCensorClient(createHttpClient(OkHttp.create()))
                },
                batteryManager = BatteryManager()
            )
        }
    }
}