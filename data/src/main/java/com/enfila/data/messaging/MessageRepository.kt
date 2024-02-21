package com.enfila.data.messaging

interface MessageRepository {
    suspend fun sendMessage(to: String, from: String, body: String)
}
