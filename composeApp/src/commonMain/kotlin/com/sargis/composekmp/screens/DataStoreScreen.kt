package com.sargis.composekmp.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

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