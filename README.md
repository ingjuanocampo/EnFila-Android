# EnFila — Android Queue Management App

**EnFila** ("en fila" = in line) is an Android application for **business-side queue management**. Staff use the app to assign turns to clients, monitor the active queue in real time, manage client records, and trigger SMS notifications when a client's turn is called — all without requiring clients to install anything.

---

## Table of Contents

- [Features](#features)
- [Screenshots](#screenshots)
- [Architecture](#architecture)
- [Module Structure](#module-structure)
- [Tech Stack](#tech-stack)
- [Navigation & Screens](#navigation--screens)
- [Backend & API](#backend--api)
- [Data Models](#data-models)
- [Getting Started](#getting-started)
- [Contributing](#contributing)
- [License](#license)

---

## Features

### Authentication
- **Phone number sign-in** via Firebase PhoneAuth (OTP / SMS code, Colombia +57 prefix by default).
- **Google sign-in** via Firebase Google Auth.
- **New company onboarding**: after first login, users complete a profile form to create their company site in the backend.

### Home Dashboard
- Displays a live **resume card** (number of waiting, active, and finished turns for the day).
- Shows the **currently active shift** (the client currently being served).
- Shows the **next shift in queue** at a glance.
- Bottom sheet for **quick turn assignation** without leaving the home screen.

### Turn / Shift Management
- **Assign a turn**: enter the client's phone number, optionally add a name and a note, then confirm the assigned turn number.
- **Full shift list** with tab switcher between active/pending turns and **history** of past turns.
- **Shift detail**: view all metadata (turn number, state, date, notes, start/end times) and transition states.
- Turn states: `WAITING → CALLING → FINISHED / CANCELLED`.
- **SMS notification** sent automatically when a turn is activated (via backend messaging endpoint).

### Client Management
- **Client list**: all registered clients, identified by phone number.
- **Client detail**: full profile and all historical turns for that client.
- Search / filter shifts by client.

### Account
- View and manage the authenticated user's profile and company site information.

### Tips
- In-app tips / help content section.

---

## Screenshots

### Login
<img src="https://github.com/ingjuanocampo/EnFila-Android/blob/main/app/Screenshots/Screenshot_20231110_083915.png" height="600">

### Turn Assignation Flow
<img src="https://github.com/ingjuanocampo/EnFila-Android/blob/main/app/Screenshots/Screenshot_20231110_084035.png" height="600"> <img src="https://github.com/ingjuanocampo/EnFila-Android/blob/main/app/Screenshots/Screenshot_20231110_084117.png" height="600"> <img src="https://github.com/ingjuanocampo/EnFila-Android/blob/main/app/Screenshots/Screenshot_20231110_084137.png" height="600">

### Home Dashboard
<img src="https://github.com/ingjuanocampo/EnFila-Android/blob/main/app/Screenshots/Screenshot_20231110_084304.png" height="600">

### Turn List
<img src="https://github.com/ingjuanocampo/EnFila-Android/blob/main/app/Screenshots/Screenshot_20231110_084321.png" height="600">

### Client List
<img src="https://github.com/ingjuanocampo/EnFila-Android/blob/main/app/Screenshots/Screenshot_20231110_084411.png" height="600">

---

## Architecture

EnFila follows a **Clean Architecture–inspired layering** combined with **MVVM** and selective **MVI** patterns.

```
┌──────────────────────────────────────────────────┐
│  Presentation Layer                              │
│  Activities · Fragments · Jetpack Compose        │
│  ViewModels (MVVM) · MviBaseViewModel (MVI)      │
├──────────────────────────────────────────────────┤
│  Domain Layer                                    │
│  Entities · Repository interfaces · Use Cases   │
│  AppStateProvider (LoggedState / NotLoggedState) │
├──────────────────────────────────────────────────┤
│  Data Layer                                      │
│  Repository implementations · Remote Sources    │
│  Backend (Ktor) · Firebase (Auth / Firestore)    │
│  DataStore (local preferences)                   │
└──────────────────────────────────────────────────┘
```

### Key Patterns

| Pattern | Usage |
|---|---|
| **Repository** | Every domain concept (User, Client, Shift, CompanySite) has a `Repository<T>` interface with backend and Firebase implementations. |
| **Use Cases** | Business logic encapsulated in single-responsibility classes (`SignInUC`, `LoadShiftsUC`, etc.). |
| **MVI** | `MviBaseViewModel<STATE>` exposes a `StateFlow` for UI state and a `SharedFlow` for one-shot view effects (navigation, errors). |
| **MVVM** | Most fragments observe `LiveData` / `StateFlow` from their ViewModel. |
| **App State** | `AppStateProvider` evaluates login state at startup and provides `LoggedState` or `NotLoggedState`, driving the splash → login/lobby routing decision. |
| **CompositeDelegateAdapter** | Home list uses a heterogeneous `RecyclerView` adapter with typed delegates (resume, active shift, next shift, empty, headers). |

---

## Module Structure

```
EnFila-Android/
├── app/                    # Main application module
│   └── src/main/java/com/ingjuanocampo/enfila/android/
│       ├── domain/         # Entities, repository interfaces, use cases
│       ├── data/           # Repository implementations, backend sources, mappers
│       ├── ui/             # Fragments, Activities, ViewModels, Compose screens
│       │   ├── home/       # Home dashboard + bottom sheet assignation
│       │   ├── shifts/     # Shift list, history, detail
│       │   ├── clients/    # Client list, client detail
│       │   ├── login/      # Login, phone auth, new company
│       │   ├── account/    # User account screen
│       │   └── assignation/# Standalone assignation flow (Activity)
│       └── di/             # Hilt modules (DataModule, AppComponent, etc.)
├── data/                   # Shared data library module
│   └── src/main/java/com/enfila/data/
│       ├── backend/        # ApiClient (Ktor), BackendModels (DTOs)
│       ├── messaging/      # RemoteMessageSource (Twilio), MessagingModule
│       └── di/             # MessagingModule, MessagingModuleBinds
└── buildSrc/               # Build helpers (migrated to version catalog)
```

**App namespace**: `com.ingjuanocampo.enfila.android`  
**Data library namespace**: `com.enfila.data`

---

## Tech Stack

| Category | Library / Tool | Version |
|---|---|---|
| Language | Kotlin | 1.9.24 |
| Build | Android Gradle Plugin | 8.2.2 |
| Min / Target SDK | API 21 / API 34 | — |
| UI (Views) | AppCompat, Material, ConstraintLayout, RecyclerView | Material 1.12 |
| UI (Compose) | Jetpack Compose BOM, Material3, ViewModel Compose | BOM 1.6.8 / M3 1.2.1 |
| Architecture | Lifecycle ViewModel + LiveData, Navigation Component | Lifecycle 2.7.0 / Nav 2.7.4 |
| Async | Kotlin Coroutines | 1.8.1 |
| DI | Dagger Hilt | 2.48 |
| HTTP (backend) | Ktor Client (Android engine, JSON, logging) | 2.3.12 |
| HTTP (Twilio legacy) | Retrofit 2 + Gson | 2.11 |
| Firebase | Auth, Firestore, Remote Config | BOM 33.3.0 |
| Google Play | Sign-In, SafetyNet | Auth 21.2.0 |
| Local storage | DataStore Preferences | 1.1.1 |
| List adapter | CompositeDelegateAdapter | 1.0.3 |
| Code quality | ktlint Gradle plugin | 12.1.1 |

---

## Navigation & Screens

EnFila uses three independent Navigation Component graphs:

```
SplashActivity
├── (not logged) → ActivityLogin  [login_nav.xml]
│       ├── FragmentLoginLobby        — Google sign-in entry point
│       ├── FragmentLoginPhoneNumber  — Phone number entry
│       ├── FragmentVerificationCode  — OTP verification
│       └── FragmentNewCompany        — First-time company profile setup
│
└── (logged)    → ActivityLobby  [nav_home.xml]
        ├── FragmentHome              — Dashboard (active/next turn, quick assign bottom sheet)
        ├── FragmentShiftPager        — Tabs: active list | history
        │       ├── FragmentListItems (active)
        │       └── FragmentHistory
        ├── FragmentClientList        — All clients
        ├── FragmentTips              — In-app tips
        ├── FragmentAccount           — User & company account
        ├── FragmentShiftDetail       — Shift detail (nested graph)
        └── FragmentClientDetails     — Client detail + shift history (nested graph)

ActivityAssignation  [nav_graph.xml]  (standalone / legacy path)
        ├── FragmentPhoneNumber
        ├── FragmentNameNote
        └── FragmentTurn
```

Bottom navigation tabs map directly to fragment IDs in `nav_home.xml` for seamless `setupWithNavController` integration.

---

## Backend & API

The app communicates with a custom REST backend (Ktor server). The base URL is configured per build variant:

| Variant | Base URL |
|---|---|
| Debug | `http://10.0.2.2:8080` (emulator → localhost) |
| Release | `http://204.168.149.108` |

All requests go through `ApiClient` (Ktor) and return an `ApiResponse<T>` envelope:

```json
{ "success": true, "data": { ... }, "error": null, "timestamp": "..." }
```

### Endpoints

| Resource | Methods |
|---|---|
| **Users** | `GET/POST /api/v1/users`, `GET/PUT/DELETE /api/v1/users/{id}`, `GET /api/v1/users/by-phone/{phone}` |
| **Clients** | `GET/POST /api/v1/clients`, `GET/PUT/DELETE /api/v1/clients/{id}` |
| **Shifts** | `GET/POST /api/v1/shifts`, `GET/PUT/DELETE /api/v1/shifts/{id}`, `POST /api/v1/shifts/assign`, `GET /api/v1/shifts?companySiteId=`, `GET /api/v1/shifts?contactId=` |
| **Company Sites** | `GET/POST /api/v1/company-sites`, `GET/PUT/DELETE /api/v1/company-sites/{id}` |
| **Messages** | `POST /api/v1/messages/send` |

### Messaging

When a turn is activated, the app calls the backend `/messages/send` endpoint, which proxies an SMS to the client. A parallel Twilio-direct path (`RemoteMessageSource` via Retrofit) exists and reads credentials from Firebase Remote Config — this is used as a fallback or for testing purposes.

---

## Data Models

### Domain Entities

| Entity | Key Fields |
|---|---|
| `User` | `id`, `phone`, `name`, `companyIds` |
| `Client` | `id` (= phone), `name`, `shifts` |
| `Shift` | `id`, `number`, `state` (`WAITING/CALLING/CANCELLED/FINISHED`), `contactId`, `parentCompanySite`, `date`, `notes`, `attentionStartDate`, `endDate` |
| `CompanySite` | `id`, `name`, `shiftsIdList`, `shifts` |

### Backend DTOs (`:data` module)

`BackendUser`, `BackendClient`, `BackendShift`, `BackendCompanySite`, `BackendShiftState`, `SendMessageRequest`, `MessageResponse`, plus create/update/assign request types. Mapped to domain entities via `ModelMappers.kt`.

---

## Getting Started

### Prerequisites

- Android Studio Hedgehog (2023.1.1) or newer
- JDK 17
- Android SDK with API 34
- A running instance of the [EnFila backend](https://github.com/ingjuanocampo/enfila-backend) (or configure the release base URL)
- `google-services.json` placed in `app/` (Firebase project required for auth)

### Installation

```bash
git clone https://github.com/ingjuanocampo/EnFila-Android.git
cd EnFila-Android
```

Open the project in Android Studio and sync Gradle. Then run the `app` configuration on an emulator or physical device.

> **Note:** The debug build variant points to `http://10.0.2.2:8080`, which maps to `localhost` on the host machine when running on an Android emulator. Start the backend server locally before launching the debug build.

### Build Variants

| Variant | App ID suffix | Backend URL |
|---|---|---|
| `debug` | `.dev` | `http://10.0.2.2:8080` |
| `release` | — | `http://204.168.149.108` |

---

## Contributing

Contributions are welcome! Reach out directly via email for coordination, or open a pull request if you have a fix or feature ready.

Contact: **ing.juanocampo@gmail.com**

---

## License

This project is licensed under the **MIT License**.
