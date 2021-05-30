# My Weather

it is a mobile application that allows users to view the weather status in everyone and the current city weather according to the user's gps position.

- [Download apk](https://github.com/Ahlem-fahem/MobileProjectApplication/tree/main/apk)
- [screenshots](https://github.com/Ahlem-fahem/MobileProjectApplication/tree/main/screenshots)

## Built With 🛠
- [Kotlin](https://kotlinlang.org/) - First class and official programming language for Android development.
- [Flow](https://kotlin.github.io/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines.flow/-flow/) - A cold asynchronous data stream that sequentially emits values and completes normally or with an exception.
- [Android Architecture Components](https://developer.android.com/topic/libraries/architecture) - Collection of libraries that help you design robust, testable, and maintainable apps.
  - [LiveData](https://developer.android.com/topic/libraries/architecture/livedata) - Data objects that notify views when the underlying database changes.
  - [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel) - Stores UI-related data that isn't destroyed on UI changes. 
  - [Room](https://developer.android.com/topic/libraries/architecture/room) - SQLite object mapping library.
- [Retrofit](https://square.github.io/retrofit/) - A type-safe HTTP client for Android and Java.
- [Picasso](https://square.github.io/picasso/) - A powerful image downloading and caching library for Android.
- [Lottie](https://airbnb.io/lottie/#/) - Lottie is a library for Android, iOS, Web, and Windows that parses Adobe After Effects animations exported as json with Bodymovin and renders them natively on mobile and on the web!
- [Navigation component](https://developer.android.com/guide/navigation/navigation-getting-started) - Handle the fragments transactions.
- [Material Components for Android](https://github.com/material-components/material-components-android) - Modular and customizable Material Design UI components for Android.
- [Biometric](https://developer.android.com/jetpack/androidx/releases/biometric) - Authenticate with biometrics or device credentials, and perform cryptographic operations.
- [Firebase](https://firebase.google.com/?hl=FR) - Firebase is Google's mobile platform that helps you quickly develop high-quality apps and grow your business.
- [Open weather map](https://openweathermap.org) - Weather data in a fast and elegant way.


# Package Structure

    com.example.mobileprojectapplication    # Root Package
    .
    ├── data                # For data handling.
    │   ├── api             # Retrofit API for remote end point.
    │   ├── localdb          # Local Persistence Database. Room (SQLite) database
    |   │   ├── dao         # Data Access Object for Room
    │   └── repository      # Single source of data.
    ├── models               # Models classes
    ├── ui                  # Activity/View layer
        ├── authentification            # Authentification activity and login , register, reset password fragment
        |   ├── viewModel     # bridge between repository and ui
        ├── home            # A home page contains the view to display weather data and allow to user to handle cities
        │   ├── adapter     # Adapter for RecyclerView
        │   └── viewmodel   # bridge between repository and ui  
        ├── splashscreen            # A start page used to check if user is authentificated and handle redirection

    └── utils               # Utility Classes / Kotlin extensions



## Architecture
This app uses [***MVVM (Model View View-Model)***](https://developer.android.com/jetpack/docs/guide#recommended-app-arch) architecture.

![](https://developer.android.com/topic/libraries/architecture/images/final-architecture.png)


## Authors
- [Lamya Rayess](https://github.com/lamya-rey)
- [Ahlem Fahem](https://github.com/Ahlem-fahem)
- [Abdellah abouhanifa](https://github.com/abouhanifa)



