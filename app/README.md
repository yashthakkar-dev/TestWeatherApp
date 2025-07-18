# Trackforce Mobile Weather App

-This is a weather forecast app built for the Trackforce Mobile Challenge.

## 📱 App Overview

-The app shows a 7-day weather forecast based on the user’s current location. 
-It supports offline access by caching the latest data locally and refreshing it on each launch when internet is available.

---

## ✅ Challenge Requirements Covered

- [x] MVVM architecture with Use Case layer
- [x] Dependency Injection using Hilt
- [x] Offline support using Room Database
- [x] Clean code structure and best practices
- [x] Unit tests implemented
- [x] Usage of Creational, Structural, and Behavioral Design Patterns

---

## 🛠 Tech Stack

- **Language**: Kotlin
- **Clean Architecture**: MVVM + Use Cases + Repository + Datasource
- **UI**: Jetpack Compose
- **DI**: Hilt
- **Local Storage**: Room Database
- **Networking**: Retrofit + OkHttp (with Logging Interceptor)
- **Async**: Coroutines + Suspend functions + Flow
- **Testing**: JUnit, Coroutine Test, MockK
- **Location**: FusedLocationProviderClient

---

## 🧱 Design Patterns Used

- **Creational**: Singleton (e.g., Dependency injection)
- **Structural**: Adapter (e.g., mapping DTOs to domain models), Facade (local/remote data sources)
- **Behavioral**: Observer (via Kotlin Flow for data updates), State (UI state handling in Compose)

---

## 🔄 Offline Capabilities

- On every app launch, the app tries to fetch fresh data from the weather API.
- If the fetch is successful, previously saved local data is deleted and new data is stored in the Room database.
- If the device is offline, the app displays the most recently saved weather data from local storage.
- The sync cycle repeats when internet becomes available again — fresh data is fetched and replaces the old data in local storage.

---

## 🧪 Testing

Includes:
- ViewModel unit tests
- Repository mock tests

---

## 🤖 AI Assistance

-Gemini AI (Android Studio): Utilized for intelligent code completion, error detection, and rapid generation of boilerplate code, while offering suggestions aligned with the project’s architecture and structure.
-Assisted in validating architectural decisions, resolving errors, and implementing test cases more efficiently.
-Helped accelerate development by providing real-time guidance and optimization suggestions aligned with best practices.

---

## 🚀 Future Improvements

- Add support for searching weather by city name.
- Allow users to mark cities as favorites for quick access.
- Implement background sync using WorkManager to keep data fresh periodically.
- Add a UI for viewing hourly forecasts in addition to daily data.
- Introduce theming (light/dark mode) based on system preferences.
- Add animations or transitions to enhance user experience.
