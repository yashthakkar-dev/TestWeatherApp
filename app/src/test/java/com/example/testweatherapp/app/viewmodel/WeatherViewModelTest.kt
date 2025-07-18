package com.example.testweatherapp.app.viewmodel

import com.example.testweatherapp.app.util.Resource
import com.example.testweatherapp.domain.model.DailyWeatherForecast
import com.example.testweatherapp.domain.model.Weather
import com.example.testweatherapp.domain.network.NetworkStatusProvider
import com.example.testweatherapp.domain.usecase.WeatherUseCase
import com.example.testweatherapp.domain.usecase.LocationUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE)
class WeatherViewModelTest {

    private val locationUseCase = mockk<LocationUseCase>()
    private val weatherUseCase = mockk<WeatherUseCase>()
    private val networkStatusProvider = mockk<NetworkStatusProvider>()
    private lateinit var viewModel: WeatherViewModel

    private val testDispatcher = UnconfinedTestDispatcher()

    val dummyWeather = Weather(
        id = 6077243,
        cityName = "Montreal",
        country = "CA",
        message = 1.3982181,
        cnt = 7,
        dailyWeatherForecastList = listOf(
            DailyWeatherForecast(
                date = 1752508800L,
                sunrise = 1752484753,
                sunset = 1752540043,
                tempDay = 300.23,
                tempMin = 295.56,
                tempMax = 301.0,
                tempNight = 297.76,
                tempEve = 300.4,
                tempMorn = 295.74,
                feelsLikeDay = 301.89,
                feelsLikeNight = 297.97,
                feelsLikeEve = 300.95,
                feelsLikeMorn = 296.48,
                humidity = 67,
                pressure = 1015,
                weatherId = 501,
                description = "moderate rain",
                icon = "10d",
                speed = 4.95,
                clouds = 87,
                rain = 4.78,
                main = "Rain"
            ),
            DailyWeatherForecast(
                date = 1752598800L,
                sunrise = 1752571208,
                sunset = 1752626400,
                tempDay = 301.55,
                tempMin = 294.61,
                tempMax = 302.64,
                tempNight = 296.7,
                tempEve = 300.68,
                tempMorn = 295.15,
                feelsLikeDay = 302.6,
                feelsLikeNight = 296.96,
                feelsLikeEve = 302.21,
                feelsLikeMorn = 295.57,
                humidity = 55,
                pressure = 1016,
                weatherId = 800,
                description = "sky is clear",
                icon = "01d",
                speed = 4.69,
                clouds = 3,
                rain = null,
                main = "Clear"
            )
        )
    )

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        every { networkStatusProvider.isNetworkAvailable() } returns MutableStateFlow(true)

        viewModel = WeatherViewModel(
            locationUseCase = locationUseCase,
            weatherUseCase = weatherUseCase,
            networkStatusProvider = networkStatusProvider
        )
    }

    @Test
    fun `fetchWeatherData successful location and weather fetch`() = runTest(testDispatcher) {
        // Arrange
        val location = mockk<android.location.Location>()
        val expectedWeather = dummyWeather
        coEvery { locationUseCase.getCurrentLocation() } returns location
        coEvery { weatherUseCase.fetchWeather(location) } returns flowOf(expectedWeather)

        // Act
        viewModel.fetchWeatherData()

        // Wait for all coroutines to complete
        advanceUntilIdle()

        // Give a small delay to ensure state updates are processed
        delay(100)

        println("weatherState.value = ${viewModel.weatherState.value}")

        // Assert
        assertTrue("Expected Success state but got ${viewModel.weatherState.value}",
            viewModel.weatherState.value is Resource.Success)
        assertEquals(
            expectedWeather,
            (viewModel.weatherState.value as Resource.Success).data
        )

        // Verify the use cases were called
        coVerify(exactly = 1) { locationUseCase.getCurrentLocation() }
        coVerify(exactly = 1) { weatherUseCase.fetchWeather(location) }
        coVerify(exactly = 0) { weatherUseCase.getWeather() }
    }

    @Test
    fun `fetchWeatherData successful cached weather fetch when location is null`() = runTest(testDispatcher) {
        // Arrange
        coEvery { locationUseCase.getCurrentLocation() } returns null
        val expectedWeather = dummyWeather
        coEvery { weatherUseCase.getWeather() } returns flowOf(expectedWeather)

        // Act
        viewModel.fetchWeatherData()

        // Wait for all coroutines to complete
        advanceUntilIdle()

        // Give a small delay to ensure state updates are processed
        delay(100)

        println("weatherState.value = ${viewModel.weatherState.value}")

        // Assert
        assertTrue("Expected Success state but got ${viewModel.weatherState.value}",
            viewModel.weatherState.value is Resource.Success)
        assertEquals(
            expectedWeather,
            (viewModel.weatherState.value as Resource.Success).data
        )

        // Verify the use cases were called
        coVerify(exactly = 1) { locationUseCase.getCurrentLocation() }
        coVerify(exactly = 0) { weatherUseCase.fetchWeather(any()) }
        coVerify(exactly = 1) { weatherUseCase.getWeather() }
    }

    @Test
    fun `fetchWeatherData handles error correctly`() = runTest(testDispatcher) {
        // Arrange
        val location = mockk<android.location.Location>()
        val exception = RuntimeException("Network error")
        coEvery { locationUseCase.getCurrentLocation() } returns location
        coEvery { weatherUseCase.fetchWeather(location) } returns flow { throw exception }

        // Act
        viewModel.fetchWeatherData()

        // Wait for all coroutines to complete
        advanceUntilIdle()

        // Give a small delay to ensure state updates are processed
        delay(100)

        println("weatherState.value = ${viewModel.weatherState.value}")

        // Assert
        assertTrue("Expected Error state but got ${viewModel.weatherState.value}",
            viewModel.weatherState.value is Resource.Error)
        assertEquals(
            "Error: Network error",
            (viewModel.weatherState.value as Resource.Error).message
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
}