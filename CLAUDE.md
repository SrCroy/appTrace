# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

**appTrace** is an Android application (Java) for tracking physical activities and routes, with social features (groups, messaging, achievements, publications). It communicates with a backend REST API.

- Package: `com.example.apptrace`
- Min SDK: 24 / Target SDK: 36
- Build tool: Gradle (AGP 9.1.1), Java 11

## Build Commands

```bash
# Build debug APK
./gradlew assembleDebug

# Install on connected device/emulator
./gradlew installDebug

# Run unit tests
./gradlew test

# Run a single unit test class
./gradlew test --tests "com.example.apptrace.ExampleUnitTest"

# Run instrumented tests (requires connected device/emulator)
./gradlew connectedAndroidTest
```

## Architecture

The app follows a simple Activity-based architecture (no fragments/ViewModels yet). The entry point is `SplashActivity`, which checks `SessionManager` and redirects to `LoginActivity` or `MainActivity`.

### Key layers

**Network** (`network/`)
- `RetrofitClient` — singleton Retrofit instance. Base URL: `http://10.0.2.2:8080/apiRestTrace/apiRestTrace/public/api/` (Android emulator localhost pointing to a local backend on port 8080).
- `AuthInterceptor` — automatically attaches the Bearer JWT token from `SessionManager` to every request.
- `ApiService` — Retrofit interface defining API endpoints (`/login`, `/register`).

**Session** (`session/SessionManager`)
- Singleton backed by `SharedPreferences` (`apptrace_session`). Stores JWT token, user ID, name, and email. Use `isLoggedIn()` to gate access; `saveSession(LoginData)` / `logout()` to manage state.

**Local Database** (`dataBase/AppDataBase`)
- Room database (`db_trace`) with 19 entities covering the full domain: users, routes, GPS points, activities, groups, group members, publications, files, friendships, conversations, messages, reactions, comments, reports, and achievements (global and custom).
- Access via `AppDataBase.obtenerDatos(context)` (singleton with double-checked locking).
- DAOs live in `daos/`; only `UsuariosDao` exists so far.
- Room schema exports go to `app/schemas/`.

**Model** (`model/`)
- `model/auth/` — DTOs for login/register flows: `LoginRequest`, `RegisterRequest`, `LoginData`, `UserDto`, `ApiResponse<T>`.
- `model/Users.java` — standalone user model.

**Application class** (`AppTraceApplication`)
- Provides a static `getAppContext()` used by `AuthInterceptor` to access `SessionManager` without a `Context` parameter.

### Network security

`res/xml/network_security_config.xml` allows cleartext HTTP to the emulator host (`10.0.2.2`). When pointing to a real backend, update `RetrofitClient.BASE_URL` and review the network security config.

### UI

Layouts are in `res/layout/`. The app uses the `Theme.Trace` style (Material Design) with DM Sans and JetBrains Mono fonts loaded via Google Fonts. The bottom navigation menu is defined in `res/menu/bottom_nav_menu.xml`.
