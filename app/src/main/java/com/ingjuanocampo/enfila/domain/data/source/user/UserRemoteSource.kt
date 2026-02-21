package com.ingjuanocampo.enfila.domain.data.source.user

import com.ingjuanocampo.enfila.data.source.user.UserRemoteSource
import com.ingjuanocampo.enfila.domain.data.source.RemoteSource
import com.ingjuanocampo.enfila.domain.entity.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

class UserRemoteImpl
    @Inject
    constructor(private val userRemoteSource: UserRemoteSource) : RemoteSource<User> {
        override suspend fun fetchDataAll(id: String): List<User>? = listOf(fetchData(id)!!)

        override suspend fun uploadData(data: User): User? {
            return userRemoteSource.updateData(data).firstOrNull()
        }

        override suspend fun fetchData(id: String): User? {
            return userRemoteSource.fetchData(id).firstOrNull()
        }

        override suspend fun uploadData(data: List<User>): List<User>? {
            return data
        }
    }
