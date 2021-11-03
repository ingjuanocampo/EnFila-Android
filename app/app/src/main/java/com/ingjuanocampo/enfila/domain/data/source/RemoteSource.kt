package com.ingjuanocampo.enfila.domain.data.source

interface RemoteSource<Data> {
    suspend fun fetchData(id: String): Data?
    suspend fun createOrUpdate(data: Data)
}