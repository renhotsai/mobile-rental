# Mobile Rental App

An Android application for rental property management and discovery, built with Kotlin and Firebase. This app allows users to browse, search, and manage rental properties with interactive map functionality and user authentication.

## Features

### Core Functionality
- **Property Discovery**: Browse rental properties with detailed information
- **Interactive Map**: View properties on Google Maps with custom markers
- **Search & Filter**: Find properties by location, price range, type, bedrooms, bathrooms
- **User Authentication**: Firebase-based login and registration system
- **Property Management**: Add, update, and manage property listings
- **Favorites**: Save preferred properties to a favorites list
- **User Profiles**: Manage account information and passwords

### Property Types Supported
- Condo
- House
- Basement
- Apartment

### User Roles
- **Tenants**: Browse and search for rental properties
- **Landlords**: List and manage rental properties
- **Guests**: Limited access without authentication

## Technology Stack

- **Language**: Kotlin
- **Platform**: Android (API 24+)
- **Backend**: Firebase (Firestore, Authentication)
- **Maps**: Google Maps SDK for Android
- **Architecture**: MVVM pattern with Repository pattern
- **UI**: Material Design components with View Binding
- **Location Services**: Google Play Services Location API

## Project Structure

```
app/src/main/java/com/hy/group3_project/
├── MainActivity.kt                 # Main activity with map and list views
├── BaseActivity.kt                # Base activity class
├── controllers/
│   ├── properties/
│   │   └── PropertyRepository.kt   # Property data management
│   └── users/
│       └── UserRepository.kt       # User data management
├── models/
│   ├── adapters/
│   │   ├── PropertyAdapter.kt      # RecyclerView adapter for properties
│   │   └── UserAdapter.kt          # User list adapter
│   ├── enums/
│   │   └── [Various status enums]  # App state enumerations
│   ├── properties/
│   │   ├── Property.kt             # Property data model
│   │   ├── FilterData.kt           # Filter parameters model
│   │   └── GeocodingHandler.kt     # Location handling utilities
│   └── users/
│       └── User.kt                 # User data model
└── views/
    ├── properties/
    │   ├── AddPropertyActivity.kt       # Add new property
    │   ├── PropertyDetailActivity.kt    # Property details view
    │   ├── ShowPropertyActivity.kt      # Property listing view
    │   └── UpdatePropertyActivity.kt    # Edit existing property
    └── users/
        ├── LoginActivity.kt             # User authentication
        ├── SignUpActivity.kt            # User registration
        ├── FavoriteActivity.kt          # Favorites management
        ├── EditAcctInfoActivity.kt      # Account settings
        ├── EditPasswordActivity.kt      # Password management
        └── ForgotPasswordActivity.kt    # Password recovery
```

## Prerequisites

- Android Studio Arctic Fox or later
- Android SDK 24 (Android 7.0) or higher
- Google Play Services
- Firebase project with Authentication and Firestore enabled
- Google Maps API key

## Setup Instructions

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd mobile-rental
   ```

2. **Firebase Configuration**
   - Create a new Firebase project at [Firebase Console](https://console.firebase.google.com)
   - Enable Authentication (Email/Password provider)
   - Enable Cloud Firestore
   - Download `google-services.json` and place it in `app/` directory

3. **Google Maps Setup**
   - Obtain a Google Maps API key from [Google Cloud Console](https://console.cloud.google.com)
   - Enable Maps SDK for Android and Places API
   - Replace the API key in `AndroidManifest.xml`

4. **Build and Run**
   ```bash
   ./gradlew build
   ./gradlew installDebug
   ```

## Key Dependencies

- **UI/UX**: Material Design Components, ConstraintLayout
- **Firebase**: Authentication, Firestore, Analytics
- **Maps**: Google Play Services Maps & Location
- **Data**: Gson for JSON parsing
- **Testing**: JUnit, Espresso

## Permissions Required

- `ACCESS_FINE_LOCATION`: For precise location services
- `ACCESS_COARSE_LOCATION`: For approximate location services
- `INTERNET`: For network communication

## App Navigation

### Main Screen
- **Map View**: Interactive map showing property markers
- **List View**: Scrollable list of properties
- **Search Bar**: Location-based property search
- **Filter Button**: Advanced filtering options

### Property Management
- Landlords can add, edit, and manage their property listings
- Tenants can view detailed property information and add to favorites

### User Management
- Account registration and authentication
- Profile management and password updates
- Favorites list for saved properties

## Contributing

This project follows Android development best practices:
- MVVM architecture pattern
- Repository pattern for data management
- Material Design guidelines
- Kotlin coding conventions

## Version Information

- **Version**: 1.0
- **Target SDK**: 34 (Android 14)
- **Minimum SDK**: 24 (Android 7.0)
- **Kotlin**: 1.9.0
- **Gradle**: 8.1.2

## License

This project is developed as part of an academic assignment for Humber College.