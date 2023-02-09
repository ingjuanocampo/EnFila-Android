package com.enfila.data.messaging.source

import com.enfila.data.messaging.MessageRepository
import javax.inject.Inject

class MessageRepositoryImpl @Inject constructor(private val remoteMessageSource: RemoteMessageSource):
    MessageRepository {
    override suspend fun sendMessage(to: String, from: String, body: String) {
        remoteMessageSource.sendMessage("whatsapp:+$to", "whatsapp:+$from", body)
    }
}