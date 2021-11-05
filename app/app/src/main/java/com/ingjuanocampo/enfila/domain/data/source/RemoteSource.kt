package com.ingjuanocampo.enfila.domain.data.source

import kotlinx.coroutines.flow.Flow

interface RemoteSource<Data> {
    suspend fun fetchData(id: String): Data?
    fun uploadData(data: Data): Flow<Data?>
}