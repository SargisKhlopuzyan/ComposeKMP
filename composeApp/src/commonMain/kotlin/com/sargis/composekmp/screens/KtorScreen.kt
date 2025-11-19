package com.sargis.composekmp.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import networking.InsultCensorClient
import util.NetworkError
import util.onError
import util.onSuccess

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