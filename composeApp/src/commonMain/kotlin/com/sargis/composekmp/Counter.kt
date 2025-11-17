package com.sargis.composekmp

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier

@Composable
fun Counter(modifier: Modifier = Modifier) {
    var count by remember {
        mutableIntStateOf(0)
    }
    Column {
        Text(count.toString())
        Button(
            onClick = {
                count++
            }
        ) {
            Text("Increment")
        }
    }
}