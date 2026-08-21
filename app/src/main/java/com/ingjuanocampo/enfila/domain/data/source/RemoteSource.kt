package com.ingjuanocampo.enfila.domain.data.source

interface RemoteSource<Data> {
    suspend fun fetchDataAll(id: String): List<Data>?

    suspend fun fetchData(id: String): Data?

    suspend fun uploadData(data: List<Data>): List<Data>?

    suspend fun uploadData(data: Data): Data?
}
