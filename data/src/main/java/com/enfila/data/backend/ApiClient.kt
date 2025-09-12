package com.enfila.data.backend

import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ApiClient @Inject constructor() {

    val httpClient = HttpClient(Android) {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                isLenient = true
                prettyPrint = true
            })
        }

        install(Logging) {
            level = LogLevel.INFO
        }
    }

    companion object {
        // Configure this based on your environment
        // TODO Create enviroments and understand how to publish this
        const val BASE_URL = "http://localhost:8080" // Android emulator localhost
        // For real device, use your computer's IP: "http://192.168.x.x:8080"
        // For production: "https://your-domain.com"

        const val API_V1 = "$BASE_URL/api/v1"
    }
}
