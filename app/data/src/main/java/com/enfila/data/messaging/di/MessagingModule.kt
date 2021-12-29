package com.enfila.data.messaging.di

import com.enfila.data.messaging.MessageRepository
import com.enfila.data.messaging.source.MessageRepositoryImpl
import com.enfila.data.messaging.source.RemoteMessageSource

class MessagingModule {

    private fun provideRemoteMessageSource() = RemoteMessageSource()

    val repository: MessageRepository by lazy { MessageRepositoryImpl(provideRemoteMessageSource()) }

}