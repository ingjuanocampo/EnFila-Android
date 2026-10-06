# EnFila Data Layer Migration Guide

This guide outlines the migration from Firebase to a Kotlin backend using Ktor for the EnFila Android application.

## Overview

The data layer has been extracted from Firebase to a dedicated Kotlin backend to improve:
- **Scalability**: Better control over data operations
- **Performance**: Reduced client-side processing
- **Maintainability**: Single codebase for data logic
- **Cost**: Potential reduction in Firebase usage costs

## Architecture Changes

### Before (Firebase)
```
Android App → Firebase SDK → Firebase Cloud
     ↓
   Local Cache
```

### After (Backend + HTTP)
```
Android App → Ktor HTTP Client → Backend API → PostgreSQL
     ↓                              ↓
   Local Cache                 Twilio (for messaging)
```

## Backend Setup

### 1. Prerequisites

- Docker & Docker Compose
- Twilio Account (for messaging)
- PostgreSQL database (or use Docker)

### 2. Environment Configuration

Create `enfila-backend/.env`:
```bash
TWILIO_SID=your_twilio_account_sid
TWILIO_TOKEN=your_twilio_auth_token
DATABASE_URL=jdbc:postgresql://localhost:5432/enfila_db
DATABASE_USER=enfila_user  
DATABASE_PASSWORD=enfila_password
```

### 3. Start Backend Services

```bash
cd enfila-backend

# Start all services (PostgreSQL + Backend)
docker-compose up --build

# Or start just the database for local development
docker-compose up postgres -d

# Run backend locally
./gradlew run
```

The backend will be available at:
- **API**: `http://localhost:8080/api/v1`
- **Health**: `http://localhost:8080/health`

## Android App Changes

### 1. Dependencies Added

**Added to `data/build.gradle.kts`:**
```kotlin
// Ktor Client for backend communication
implementation(libs.ktor.client.core)
implementation(libs.ktor.client.android)
implementation(libs.ktor.client.content.negotiation)
implementation(libs.ktor.client.logging)
implementation(libs.ktor.serialization.kotlinx.json)
```

### 2. New Data Sources

**Backend HTTP Sources:**
- `BackendUserSource` - Replaces Firebase user operations
- `BackendClientSource` - Replaces Firebase client operations  
- `BackendShiftSource` - Replaces Firebase shift operations
- `BackendMessageSource` - Replaces direct Twilio integration

### 3. Configuration Changes

**Update `ApiClient.BASE_URL` based on environment:**
```kotlin
// For Android Emulator
const val BASE_URL = "http://10.0.2.2:8080"

// For Physical Device (replace with your IP)
const val BASE_URL = "http://192.168.x.x:8080"

// For Production
const val BASE_URL = "https://your-backend-domain.com"
```

### 4. Dependency Injection Updates

The `DataModule` now uses backend sources instead of Firebase:
```kotlin
// Before
fun bindsUserRepository(userRemoteImpl: UserRemoteImpl, ...)

// After  
fun bindsUserRepository(userRemoteBackend: UserRemoteSourceBackend, ...)
```

## API Endpoints

### Core Endpoints Available:

**Users:**
- `GET /api/v1/users` - Get all users
- `GET /api/v1/users/{id}` - Get user by ID
- `GET /api/v1/users/by-phone/{phone}` - Get user by phone
- `POST /api/v1/users` - Create user
- `PUT /api/v1/users/{id}` - Update user
- `DELETE /api/v1/users/{id}` - Delete user

**Clients:**
- `GET /api/v1/clients` - Get all clients
- `GET /api/v1/clients/{id}` - Get client by ID  
- `POST /api/v1/clients` - Create client
- `PUT /api/v1/clients/{id}` - Update client
- `DELETE /api/v1/clients/{id}` - Delete client

**Shifts:**
- `GET /api/v1/shifts` - Get all shifts (with filters)
- `GET /api/v1/shifts/{id}` - Get shift by ID
- `GET /api/v1/shifts/{id}/details` - Get shift with client details
- `POST /api/v1/shifts` - Create shift
- `POST /api/v1/shifts/assign` - Assign new shift (simplified)
- `PUT /api/v1/shifts/{id}` - Update shift
- `DELETE /api/v1/shifts/{id}` - Delete shift

**Messaging:**
- `POST /api/v1/messages/send` - Send WhatsApp message

**Migration:**
- `POST /api/v1/migration/from-firebase` - Migrate existing Firebase data
- `GET /api/v1/migration/status` - Check migration status

## Migration Process

### 1. Deploy Backend

```bash
cd enfila-backend
docker-compose up -d --build
```

### 2. Migrate Existing Data (Optional)

If you have existing Firebase data:
```bash
# Configure firebase-service-account.json in backend
curl -X POST http://localhost:8080/api/v1/migration/from-firebase

# Check status
curl http://localhost:8080/api/v1/migration/status
```

### 3. Update Android App Configuration

Update `ApiClient.BASE_URL` to point to your backend:
```kotlin
// In ApiClient.kt
const val BASE_URL = "http://your-backend-url:8080"
```

### 4. Build and Test Android App

```bash
# Build the app
./gradlew assembleDebug

# Run on device/emulator
# The app will now use HTTP calls instead of Firebase
```

## Testing the Migration

### 1. Backend Health Check
```bash
curl http://localhost:8080/health
# Expected: {"status":"OK","timestamp":...}
```

### 2. Test API Endpoints
```bash
# Create a client
curl -X POST http://localhost:8080/api/v1/clients \
  -H "Content-Type: application/json" \
  -d '{"id":"+1234567890","name":"Test Client"}'

# Get all clients  
curl http://localhost:8080/api/v1/clients

# Assign a shift
curl -X POST http://localhost:8080/api/v1/shifts/assign \
  -H "Content-Type: application/json" \
  -d '{"companySiteId":"site1","contactId":"+1234567890","notes":"Test shift"}'
```

### 3. Android App Testing
- Install and run the Android app
- Test core flows: user login, client management, shift assignment
- Verify messaging functionality works
- Check that data persists across app restarts

## Troubleshooting

### Common Issues:

**1. Backend Connection Failed**
- Verify backend is running: `curl http://localhost:8080/health`
- Check Android `ApiClient.BASE_URL` configuration
- For emulator, use `10.0.2.2` instead of `localhost`

**2. Database Connection Issues**
- Ensure PostgreSQL is running: `docker-compose ps`
- Check database credentials in `.env` file
- Verify database initialization: Check logs with `docker-compose logs postgres`

**3. Twilio Messages Not Sending**
- Verify `TWILIO_SID` and `TWILIO_TOKEN` in `.env`
- Check Twilio account has WhatsApp messaging enabled
- Test messaging endpoint directly

**4. Android Build Issues**
- Clean build: `./gradlew clean build`
- Verify all Ktor dependencies are resolved
- Check for version conflicts in `libs.versions.toml`

## Production Deployment

### Backend Considerations:
1. **Database**: Use managed PostgreSQL (AWS RDS, Google Cloud SQL)
2. **Hosting**: Deploy backend to cloud provider (AWS, GCP, Azure)
3. **Security**: Add authentication, rate limiting, HTTPS
4. **Monitoring**: Add logging, metrics, health checks
5. **Scaling**: Configure load balancing, auto-scaling

### Android Considerations:
1. **Configuration**: Update `BASE_URL` for production
2. **Security**: Add certificate pinning for HTTPS
3. **Performance**: Configure HTTP client timeouts
4. **Error Handling**: Improve error messages for network issues

## Rollback Plan

If needed to rollback to Firebase:
1. Revert dependency injection changes in `DataModule`
2. Switch back to Firebase remote sources
3. Deploy previous app version
4. Firebase data remains untouched during this migration

## Benefits Achieved

✅ **Improved Performance**: Direct HTTP calls vs Firebase SDK overhead  
✅ **Better Control**: Custom business logic in backend  
✅ **Enhanced Security**: Centralized data validation  
✅ **Cost Optimization**: Potential reduction in Firebase usage  
✅ **Scalability**: Independent scaling of backend services  
✅ **Maintainability**: Single source of truth for data logic  

The migration maintains the same app functionality while providing a more robust and scalable architecture for future enhancements.
