package com.ingjuanocampo.enfila.data.backend.source

import com.enfila.data.backend.ApiClient
import com.enfila.data.backend.models.ApiResponse
import com.enfila.data.backend.models.MessageResponse
import com.enfila.data.backend.models.SendMessageRequest
import com.enfila.data.messaging.MessageRepository
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BackendMessageSource @Inject constructor(
    private val apiClient: ApiClient
) : MessageRepository {
    
    private val client = apiClient.httpClient
    
    override suspend fun sendMessage(to: String, from: String, body: String) {
        try {
            val response = client.post("${ApiClient.API_V1}/messages/send") {
                contentType(ContentType.Application.Json)
                setBody(SendMessageRequest(
                    to = to,
                    from = from,
                    body = body
                ))
            }
            
            val apiResponse: ApiResponse<MessageResponse> = response.body()
            
            if (!apiResponse.success) {
                throw Exception(apiResponse.error ?: "Failed to send message")
            }
        } catch (e: Exception) {
            // Log error or handle as needed
            throw e
        }
    }
}
