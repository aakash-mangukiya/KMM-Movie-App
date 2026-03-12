# MoviesApp - Kotlin Multiplatform (KMP)

A modern, cross-platform Movie application built with **Kotlin Multiplatform (KMP)**. This project demonstrates a shared business logic architecture across **Android (Jetpack Compose)** and **iOS (SwiftUI)**.

## 🚀 Features
- **Shared Business Logic**: 100% of the networking, data mapping, and domain logic is shared.
- **Android App**: Built using Jetpack Compose and Navigation3.
- **iOS App**: Built using native SwiftUI, consuming shared ViewModels via Koin.
- **Clean Architecture**: Strictly follows Clean Architecture principles (Data, Domain, Presentation layers).
- **SOLID Principles**: Refactored to ensure high maintainability and testability.

## 🛠 Tech Stack
- **Kotlin Multiplatform**: For sharing code between Android and iOS.
- **Compose Multiplatform**: Used for the Android UI.
- **SwiftUI**: Used for the native iOS UI.
- **Koin**: Dependency Injection for both platforms.
- **Ktor**: Multiplatform networking client.
- **Kotlinx Serialization**: For JSON parsing and navigation.
- **Coroutines & Flow**: Reactive state management.
- **SKIE**: Enhanced Swift/Kotlin interoperability for Flows and Selections.
- **Coil3**: Image loading for Compose.

## 🏗 Project Architecture

The project is divided into three main layers in the `:shared` module:

### 1. Data Layer
- **Repositories**: Implementation of data fetching from TMDB API using Ktor.
- **Mappers**: Converts raw API responses (`ResponseDTO`) into clean Domain models.
- **Models**: Serializable data classes for network requests.

### 2. Domain Layer
- **Models**: Pure Kotlin data classes representing the business entities (e.g., `Movie`, `MovieDetails`).
- **Repositories (Interfaces)**: Defines the contracts for data operations.
- **UseCases**: Encapsulates specific business logic, such as combining movie details with credit information.

### 3. Presentation Layer
- **ViewModels**: Shared ViewModels using `androidx.lifecycle.ViewModel` for managing UI state.
- **State Handling**: Centralized state management using `BaseStateHandler`.
- **Interactors**: Middle-layer to bridge UseCases and ViewModels.

## 📱 Platform Implementation

### Android (:composeApp)
- Uses **Jetpack Compose** for building the UI.
- Implements **Navigation3** for type-safe routing.
- Uses **Koin-Android** for dependency injection at the application level.

### iOS (:iosApp)
- A native **SwiftUI** application.
- Uses a `ViewModelWrapper` pattern to observe shared Kotlin `StateFlow`s as `@Published` properties.
- Initializes Koin via a `getKoinInstance()` helper from the shared module.

## 🛠 Setup & Installation

### Requirements
- **Android Studio Ladybug** or newer.
- **Xcode 15+** (for iOS).
- **JDK 17+**.

### Build Instructions
1. Clone the repository.
2. Open the project in Android Studio.
3. **Sync Gradle**: `File > Sync Project with Gradle Files`.
4. **Run Android**: Select the `composeApp` configuration and run on an emulator or device.
5. **Run iOS**: Open `iosApp/iosApp.xcworkspace` in Xcode or run the `iosApp` configuration from Android Studio (requires a Mac).
