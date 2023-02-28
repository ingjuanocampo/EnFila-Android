package com.enfila.data.messaging.di

import com.enfila.data.messaging.MessageRepository
import com.enfila.data.messaging.TwillioCredentials
import com.enfila.data.messaging.source.MessageRepositoryImpl
import com.enfila.data.messaging.source.RemoteMessageSource
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class MessagingModule {

    @Singleton
    @Provides
    fun provideRemoteMessageSource(twillioCredentials: TwillioCredentials) =
        RemoteMessageSource(twillioCredentials)
}

@InstallIn(SingletonComponent::class)
@Module
interface MessagingModuleBinds {
    @Singleton
    @Binds
    fun binds(repositoryImpl: MessageRepositoryImpl): MessageRepository
}
