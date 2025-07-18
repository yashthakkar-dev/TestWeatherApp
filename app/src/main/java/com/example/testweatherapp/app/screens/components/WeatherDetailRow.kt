package com.example.testweatherapp.app.screens.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.example.testweatherapp.app.util.formatDate
import com.example.testweatherapp.app.util.formatDecimals
import com.example.testweatherapp.domain.model.DailyWeatherForecast

@Composable
fun WeatherDetailRow(weather: DailyWeatherForecast) {
    val imageUrl = "https://openweathermap.org/img/wn/${weather.icon}.png"

    // Get screen width percentage
    val configuration = LocalConfiguration.current
    val boxWidth = configuration.screenWidthDp * 0.35f
    val boxHeight = configuration.screenHeightDp * 0.04f

    // Outer Box with shadow effect
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(6.dp)
            .shadow(
                elevation = 12.dp, // Shadow elevation
                shape = CircleShape.copy(
                    topEnd = CornerSize(6.dp),
                    bottomStart = CornerSize(6.dp)
                ), // Preserve custom shape
                clip = false
            )
    ) {
        // Main content card
        Surface(
            modifier = Modifier
                .fillMaxWidth(),
            shape = CircleShape.copy(
                topEnd = CornerSize(6.dp),
                bottomStart = CornerSize(6.dp)
            ),
            color = Color.White
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Date Text
                Text(
                    text = formatDate(weather.date).split(",")[0],
                    style = MaterialTheme.typography.bodyMedium
                )

                // Weather Icon
                WeatherConditionImage(imageUrl = imageUrl)

                // Weather Description inside Ellipse with percentage of screen width
                Surface(
                    modifier = Modifier
                        .width(boxWidth.dp)
                        .height(boxHeight.dp),
                    shape = CircleShape,
                    color = Color(0xFFFFC400)
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Text(
                            text = weather.description,
                            style = MaterialTheme.typography.bodyMedium,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                // Max and Min Temperatures
                Text(
                    text = buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                color = Color.Blue.copy(alpha = 0.7f),
                                fontWeight = FontWeight.SemiBold
                            )
                        ) {
                            append(formatDecimals(weather.tempMax) + "º")
                        }
                        withStyle(
                            style = SpanStyle(
                                color = Color.LightGray
                            )
                        ) {
                            append(" / " + formatDecimals(weather.tempMin) + "º")
                        }
                    },
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}
