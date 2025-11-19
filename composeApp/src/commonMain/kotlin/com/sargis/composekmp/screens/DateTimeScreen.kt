package com.sargis.composekmp.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.format.char
import kotlinx.datetime.toLocalDateTime
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
@Composable
fun DateTimeScreen() {
    val cities = remember {
        listOf(
            City("Berlin", TimeZone.of("Europe/Berlin")),
            City("London", TimeZone.of("Europe/London")),
            City("New York", TimeZone.of("America/New_York")),
            City("Los Angeles", TimeZone.of("America/Los_Angeles")),
            City("Tokyo", TimeZone.of("Asia/Tokyo")),
            City("Sydney", TimeZone.of("Australia/Sydney"))
        )
    }

    var cityTimes by remember {
        mutableStateOf(
            listOf<Pair<City, LocalDateTime>>()
        )
    }

    LaunchedEffect(true) {
        while (true) {
            cityTimes = cities.map { city ->
                val now = Clock.System.now()
                city to now.toLocalDateTime(city.timeZone)
            }
            delay( 1000)
        }
    }

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        cityTimes.forEach { (city, dateTime) ->
            CityRow(city, dateTime)
        }
    }
}

@Composable
fun CityRow(city: City, time: LocalDateTime) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = city.name,
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold
        )

        Column(
            horizontalAlignment = Alignment.End
        ) {
            Text(
                text = time.formatToTime(),
                fontSize = 30.sp,
                fontWeight = FontWeight.Light
            )
            Text(
                text = time.formatToDate(),
                fontSize = 16.sp,
                fontWeight = FontWeight.Light
            )
        }
    }
}

fun LocalDateTime.formatToTime(): String {
    return this.format(
        LocalDateTime.Format {
            hour()
            char(':')
            minute()
            char(':')
            second()
        }
    )
}

fun LocalDateTime.formatToDate(): String {
    return this.format(
        LocalDateTime.Format {
            dayOfMonth()
            char('/')
            monthNumber()
            char('/')
            year()
        }
    )
}

data class City(
    val name: String,
    val timeZone: TimeZone
)