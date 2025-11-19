package com.sargis.composekmp

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import createDataStore
import io.ktor.client.engine.okhttp.OkHttp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import networking.InsultCensorClient
import networking.createHttpClient

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

//        var isChecking = true
//        lifecycleScope.launch {
//            delay(3000)
//            isChecking = false
//        }
        installSplashScreen().apply {
//            // do anything during logo is visible
//            setKeepOnScreenCondition {
//                isChecking
//            }
        }

        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
            App(
                prefs = remember {
                    createDataStore(applicationContext)
                },
                client = remember {
                    InsultCensorClient(createHttpClient(OkHttp.create()))
                },
                batteryManager = remember {
                    BatteryManager(applicationContext)
                }
            )
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
//    App(batteryManager = BatteryManager(Context()))
}