package com.ingjuanocampo.enfila.data.backend.source

import android.util.Log
import com.enfila.data.backend.ApiClient
import com.enfila.data.backend.models.ApiResponse
import com.enfila.data.backend.models.ErrorResponse
import com.enfila.data.backend.models.MessageResponse
import com.enfila.data.backend.models.SendMessageRequest
import com.enfila.data.messaging.MessageRepository
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.http.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BackendMessageSource @Inject constructor(
    private val apiClient: ApiClient
) : MessageRepository {

    private val client = apiClient.httpClient

    companion object {
        private const val TAG = "BackendMessageSource"
    }

    override suspend fun sendMessage(to: String, from: String, body: String) {
        val url = "${ApiClient.API_V1}/messages/send"
        Log.d(TAG, "sendMessage - Starting POST request to: $url (to: $to, from: $from)")

        try {
            val response = client.post(url) {
                contentType(ContentType.Application.Json)
                setBody(SendMessageRequest(
                    to = to,
                    from = from,
                    body = body
                ))
            }

            Log.d(TAG, "sendMessage - HTTP Status: ${response.status}")

            // Check if response is successful (2xx status codes)
            if (response.status.value in 200..299) {
                val apiResponse: ApiResponse<MessageResponse> = response.body()

                if (apiResponse.success) {
                    Log.d(TAG, "sendMessage - Success: Message sent successfully")
                } else {
                    Log.w(TAG, "sendMessage - API Error: ${apiResponse.error}")
                    throw Exception(apiResponse.error ?: "Failed to send message")
                }
            } else {
                // For non-2xx status codes, try to parse as ErrorResponse
                try {
                    val errorResponse: ErrorResponse = response.body()
                    Log.w(TAG, "sendMessage - HTTP Error (${response.status}): ${errorResponse.error}")
                    throw Exception(errorResponse.error ?: "Failed to send message")
                } catch (ex: Exception) {
                    Log.w(TAG, "sendMessage - HTTP Error (${response.status}): Could not parse error response")
                    throw Exception("Failed to send message: ${response.status}")
                }
            }
        } catch (e: ClientRequestException) {
            Log.e(TAG, "sendMessage - Client Error (${e.response.status}): ${e.message}")
            throw e
        } catch (e: ServerResponseException) {
            Log.e(TAG, "sendMessage - Server Error (${e.response.status}): ${e.message}")
            throw e
        } catch (e: Exception) {
            Log.e(TAG, "sendMessage - Unexpected error: ${e.javaClass.simpleName}: ${e.message}", e)
            throw e
        }
    }
}
