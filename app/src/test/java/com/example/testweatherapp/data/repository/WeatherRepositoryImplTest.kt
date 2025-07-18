package com.example.testweatherapp.data.repository

import com.example.testweatherapp.data.network.model.NetworkCity
import com.example.testweatherapp.data.network.model.NetworkCoord
import com.example.testweatherapp.data.network.model.NetworkFeelsLike
import com.example.testweatherapp.data.network.model.NetworkTemp
import com.example.testweatherapp.data.network.model.NetworkWeather
import com.example.testweatherapp.data.network.model.NetworkWeatherItem
import com.example.testweatherapp.data.network.model.NetworkWeatherObject
import com.example.testweatherapp.data.repository.datasource.WeatherLocalDataSource
import com.example.testweatherapp.data.repository.datasource.WeatherRemoteDataSource
import com.example.testweatherapp.data.repository.datasource.entity.DailyForecastEntity
import com.example.testweatherapp.data.repository.datasource.entity.WeatherForecastEntity
import com.example.testweatherapp.data.repository.datasource.entity.WeatherWithDailyForecast
import com.example.testweatherapp.domain.network.NetworkStatusProvider
import io.mockk.Runs
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
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
class WeatherRepositoryImplTest {

    private val weatherRemoteDataSource = mockk<WeatherRemoteDataSource>()
    private val weatherLocalDataSource = mockk<WeatherLocalDataSource>()
    private val networkStatusProvider = mockk<NetworkStatusProvider>()

    private lateinit var repository: WeatherRepositoryImpl

    private val testDispatcher = UnconfinedTestDispatcher()

    // Mock response data
    private val mockWeatherResponse = NetworkWeather(
        city = NetworkCity(
            id = 6077243,
            name = "Montreal",
            country = "CA",
            coord = NetworkCoord(lat = 45.5017, lon = -73.5673),
            population = 1000000,
            timezone = -18000
        ),
        cnt = 2,
        cod = "200",
        message = 1.0,
        list = listOf(
            NetworkWeatherItem(
                date = 1752508800L,
                sunrise = 1752484753,
                sunset = 1752540043,
                temp = NetworkTemp(
                    day = 300.23,
                    min = 295.56,
                    max = 301.0,
                    night = 297.76,
                    eve = 300.4,
                    morn = 295.74
                ),
                feelsLike = NetworkFeelsLike(
                    day = 301.89,
                    night = 297.97,
                    eve = 300.95,
                    morn = 296.48
                ),
                pressure = 1015,
                humidity = 67,
                weather = listOf(
                    NetworkWeatherObject(
                        id = 501,
                        main = "Rain",
                        description = "moderate rain",
                        icon = "10d"
                    )
                ),
                speed = 4.95,
                deg = 230,
                gust = 8.5,
                clouds = 87,
                pop = 0.78,
                rain = 4.78
            )
        ),
        id = 6077243
    )

    private val mockWeatherWithDailyForecast = WeatherWithDailyForecast(
        weather = WeatherForecastEntity(
            cityId = 6077243,
            cityName = "Montreal",
            country = "CA",
            message = 1.0,
            cnt = 2
        ),
        daily = listOf(
            DailyForecastEntity(
                cityId = 0,
                weatherId = 6077243,
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
                pressure = 1015,
                humidity = 67,
                main = "Rain",
                description = "moderate rain",
                icon = "10d",
                speed = 4.95,
                clouds = 87,
                rain = 4.78
            )
        )
    )

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        clearAllMocks()

        repository = WeatherRepositoryImpl(
            weatherRemoteDataSource = weatherRemoteDataSource,
            weatherLocalDataSource = weatherLocalDataSource,
            networkStatusProvider = networkStatusProvider
        )
    }

    @Test
    fun `fetchWeather should fetch from remote and cache data when network is available`() = runTest(testDispatcher) {
        // Arrange
        val latitude = 45.5017
        val longitude = -73.5673

        every { networkStatusProvider.isNetworkAvailable() } returns MutableStateFlow(true)
        coEvery { weatherRemoteDataSource.fetchWeatherByLocation(latitude, longitude) } returns flowOf(mockWeatherResponse)
        coEvery { weatherLocalDataSource.deleteWeatherData() } just Runs
        coEvery { weatherLocalDataSource.saveWeatherData(any()) } just Runs

        // Act
        val result = repository.fetchWeather(latitude, longitude).first()

        // Assert
        assertNotNull("Weather result should not be null", result)
        assertEquals("Montreal", result?.cityName)
        assertEquals("CA", result?.country)
        assertEquals(6077243, result?.id)
        assertEquals(1, result?.dailyWeatherForecastList?.size)

        // Verify interactions
        coVerify(exactly = 1) { weatherRemoteDataSource.fetchWeatherByLocation(latitude, longitude) }
        coVerify(exactly = 1) { weatherLocalDataSource.deleteWeatherData() }
        coVerify(exactly = 1) { weatherLocalDataSource.saveWeatherData(any()) }

        // Verify the saved data structure
        val savedDataSlot = slot<WeatherWithDailyForecast>()
        coVerify { weatherLocalDataSource.saveWeatherData(capture(savedDataSlot)) }

        val savedData = savedDataSlot.captured
        assertEquals(6077243, savedData.weather.cityId)
        assertEquals("Montreal", savedData.weather.cityName)
        assertEquals(1, savedData.daily.size)
        assertEquals(501, savedData.daily[0].weatherId)
        assertEquals("moderate rain", savedData.daily[0].description)
    }

    @Test
    fun `fetchWeather should return cached data when network is not available`() = runTest(testDispatcher) {
        // Arrange
        val latitude = 45.5017
        val longitude = -73.5673

        every { networkStatusProvider.isNetworkAvailable() } returns MutableStateFlow(false)
        every { weatherLocalDataSource.getWeatherWithDailyForecast() } returns flowOf(mockWeatherWithDailyForecast)

        // Act
        val result = repository.fetchWeather(latitude, longitude).first()

        // Assert
        assertNotNull("Weather result should not be null", result)
        assertEquals("Montreal", result?.cityName)
        assertEquals("CA", result?.country)
        assertEquals(6077243, result?.id)
        assertEquals(1, result?.dailyWeatherForecastList?.size)

        // Verify interactions
        verify(exactly = 1) { networkStatusProvider.isNetworkAvailable() }
        coVerify(exactly = 1) { weatherLocalDataSource.getWeatherWithDailyForecast() }

        // Verify remote data source and cache operations are NOT called
        coVerify(exactly = 0) { weatherRemoteDataSource.fetchWeatherByLocation(any(), any()) }
        coVerify(exactly = 0) { weatherLocalDataSource.deleteWeatherData() }
        coVerify(exactly = 0) { weatherLocalDataSource.saveWeatherData(any()) }

        // Verify the returned data structure
        val dailyForecast = result?.dailyWeatherForecastList?.first()
        assertNotNull(dailyForecast)
        assertEquals(1752508800L, dailyForecast?.date)
        assertEquals(300.23, dailyForecast?.tempDay)
        assertEquals("moderate rain", dailyForecast?.description)
        assertEquals("Rain", dailyForecast?.main)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
}