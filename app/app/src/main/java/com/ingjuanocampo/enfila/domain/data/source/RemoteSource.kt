package com.ingjuanocampo.enfila.domain.data.source

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

interface RemoteSource<Data> {
    fun fetchInfoFlow(id: String): Flow<Data?> = flow { fetchData(id) }
    fun createOrUpdateFlow(data: Data): Flow<Data?> = flow {
        createOrUpdate(data)
        emit(data)
    }
    suspend fun fetchData(id: String): Data?
    suspend fun createOrUpdate(data: Data)
}