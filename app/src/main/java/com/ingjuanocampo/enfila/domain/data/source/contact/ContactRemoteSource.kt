package com.ingjuanocampo.enfila.domain.data.source.contact

import com.ingjuanocampo.enfila.domain.data.source.RemoteSource
import com.ingjuanocampo.enfila.domain.entity.Client
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

@Deprecated("")
class ContactRemoteSource : RemoteSource<Client> {
    override suspend fun fetchDataAll(id: String): List<Client> {
        return emptyList()
    }

    override fun uploadData(data: List<Client>): Flow<List<Client>?> {
        return flowOf(emptyList())
    }

    override suspend fun fetchData(id: String): Client? {
        TODO("Not yet implemented")
    }

    override fun uploadData(data: Client): Flow<Client?> {
        TODO("Not yet implemented")
    }
}
