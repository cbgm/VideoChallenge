# Exercise Recorder App

This document provides an overview of the Exercise Recorder App, a full-featured Android application
for recording, storing, and viewing short exercise videos.

## How to Run the Project

1. Clone the repository to your local machine.
2. Open the project in Android Studio.
3. Build and run the app on an Android device or emulator (please see limitations and assumptions
   below).

## Architecture Overview

The app follows the principles of Clean Architecture, separating concerns into three main layers:

* **Presentation Layer:** This layer is responsible for the UI and user interaction. It uses Jetpack
  Compose for building the UI and follows the MVVM (Model-View-ViewModel) pattern. This resides in
  the `:app` module.
* **Domain Layer:** This layer contains the business logic of the application. It consists of use
  cases that encapsulate specific business rules. This also resides in the `:app` module.
* **Data Layer:** This layer is responsible for data management. It includes a repository that
  provides a single source of truth for data. The persistence logic (Room database, entities, and
  DAOs) is encapsulated in its own `:persistence` Gradle module to promote modularity and separation
  of concerns.

## Key Decisions

* **Clean Architecture & Modularization:** This architectural pattern was chosen to create a
  separation of concerns. The project is divided into an `:app` module and a `:persistence` module.
  This separates the database logic from the application logic, improving build times, testability,
  and code organization.
* **Jetpack Compose:** This modern UI toolkit was used to build the app's UI, providing a more
  declarative and efficient way to create user interfaces.
* **Koin:** This dependency injection framework was chosen for its simplicity and ease of use.
* **Room:** This persistence library was used to store data locally, providing a robust and
  efficient way to manage data.
* **Camera Intent:** This Intent was used to simplify camera development instead the more complex CameraX.
* **ExoPlayer:** This media player was used to provide a flexible and extensible way to play videos.
* **WorkManager:** This library is used for scheduling and executing background tasks, such as
  uploading videos. It ensures that the upload process continues even if the app is in the
  background or the device is restarted.

## Libraries/Tools Used

* **UI:** Jetpack Compose
* **Architecture:** Clean Architecture, MVVM
* **Dependency Injection:** Koin
* **Navigation:** Jetpack Navigation
* **Camera:** Camera Intent
* **Database:** Room
* **Video Playback:** ExoPlayer
* **Asynchronous Programming:** Kotlin Coroutines
* **Image Loading:** Coil
* **Paging:** Jetpack Paging
* **Permissions:** Accompanist Permissions
* **Background Processing:** WorkManager

## Limitations & Assumptions

* The app is designed for Android devices running API level 30 or higher.
* The app assumes that the user has granted the necessary permissions to access the camera and
  storage.
* The app does not include any cloud storage functionality or server side functionality.
* The app has to be run on a real Android device to use the camera. But there is a fallback (
  highlighted in gray) for emulator usage.
* The app does currently not allow the deletion of exercises

## Approximate Time Spent

The approximate time spent on developing this project was 24 hours.
