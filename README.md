# LecMan - Lecture Manager Android App

LecMan is a sophisticated Android application designed to streamline lecture management and timetable tracking for students and educators. Built with modern Android development practices, it offers a robust, scalable, and user-friendly experience.

## 🚀 Key Features

- **Dynamic Dashboard**: View your daily schedule at a glance with real-time updates.
- **Interactive Timetable**: Manage and browse your weekly lecture sessions seamlessly.
- **Request Management**: Handle lecture-related requests or swaps directly within the app.
- **User Profiles**: Personalize your experience and manage your academic identity.
- **Real-time Sync**: Stay updated with Firebase Cloud Messaging (FCM) integration.

## 🛠 Tech Stack & Architecture

The project follows **Clean Architecture** principles combined with the **MVVM (Model-View-ViewModel)** pattern to ensure maintainability and testability.

- **Language**: Kotlin 1.9+
- **Dependency Injection**: Hilt (Dagger)
- **Local Database**: Room Persistence Library with Flow support.
- **Navigation**: Jetpack Navigation Component.
- **Networking/Communication**: Firebase Cloud Messaging (FCM).
- **UI Components**: Material Design 3, ViewBinding, Fragments.
- **Asynchronous Programming**: Kotlin Coroutines & Flow.

## 📂 Project Structure

- `data/`: Contains local database (Room), entities, and repository implementations.
- `di/`: Dependency injection modules using Hilt.
- `fcm/`: Firebase Cloud Messaging service for push notifications and data sync.
- `ui/`: UI components including Fragments and ViewModels organized by feature.
- `util/`: Helper classes and extension functions.

## 📦 Getting Started

1. Clone the repository.
2. Open the project in **Android Studio Hedgehog** or later.
3. Ensure you have the latest **Google Services JSON** if testing FCM.
4. Build and run on an Android emulator or physical device.

---

#Android #Kotlin #MVVM #CleanArchitecture #Hilt #RoomDB #JetpackNavigation #FCM #MobileDev #LectureManager #LecMan
