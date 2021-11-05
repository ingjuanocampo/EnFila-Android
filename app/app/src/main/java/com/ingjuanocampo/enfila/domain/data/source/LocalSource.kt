package com.ingjuanocampo.enfila.domain.data.source

import kotlinx.coroutines.flow.Flow

interface LocalSource<Data> {
    suspend fun createOrUpdate(data: Data)
    suspend fun delete(dataToDelete: Data)
    suspend fun delete(id: String)
    fun getAllObserveData(): Flow<Data?>
    suspend fun getAllData(): Data?
    suspend fun getById(id: String): Data?

}

