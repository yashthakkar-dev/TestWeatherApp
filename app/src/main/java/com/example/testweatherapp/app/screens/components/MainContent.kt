package com.example.testweatherapp.app.screens.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.testweatherapp.R
import com.example.testweatherapp.app.screens.WeatherAppBar
import com.example.testweatherapp.app.util.formatDate
import com.example.testweatherapp.app.util.formatDecimals
import com.example.testweatherapp.domain.model.DailyWeatherForecast
import com.example.testweatherapp.domain.model.Weather

@Composable
fun MainContent(
    navController: NavController,
    data: Weather,
    isImperial: Boolean = false
) {
    val weatherItem = data.dailyWeatherForecastList.firstOrNull() ?: return
    val imageUrl = "https://openweathermap.org/img/wn/${weatherItem.icon}.png"

    Scaffold(
        topBar = {
            WeatherAppBar(
                title = data.cityName + ", ${data.country}",
                navController = navController,
                elevation = 5.dp
            )
        }
    ) { paddingValues ->
        Surface(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {

            Column(
                modifier = Modifier
                    .padding(4.dp)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = formatDate(weatherItem.date),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(6.dp)
                )

                Surface(
                    modifier = Modifier
                        .padding(4.dp)
                        .size(200.dp),
                    shape = CircleShape,
                    color = Color(0xFFFFC400)
                ) {

                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        WeatherConditionImage(imageUrl = imageUrl)
                        Text(
                            text = formatDecimals(weatherItem.tempDay) + "º",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.ExtraBold
                        )
                        Text(
                            text = weatherItem.main,
                            fontStyle = FontStyle.Italic
                        )
                    }
                }
                HumidityWindPressureRow(
                    weather = data.dailyWeatherForecastList[0],
                    isImperial = isImperial
                )
                HorizontalDivider()
                SunsetSunRiseRow(weather = data.dailyWeatherForecastList[0])

                Text(
                    stringResource(R.string.this_week),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(),
                    color = Color(0xFFEEF1EF),
                    shape = RoundedCornerShape(size = 14.dp)
                ) {
                    LazyColumn(
                        modifier = Modifier.padding(2.dp),
                        contentPadding = PaddingValues(1.dp)
                    ) {
                        items(items = data.dailyWeatherForecastList) { item: DailyWeatherForecast ->
                            WeatherDetailRow(weather = item)
                        }
                    }
                }
            }
        }
    }
}