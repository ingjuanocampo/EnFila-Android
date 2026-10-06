package com.ingjuanocampo.enfila.domain.data.source.contact

import com.ingjuanocampo.enfila.domain.data.source.RemoteSource
import com.ingjuanocampo.enfila.domain.entity.Client

@Deprecated("")
class ContactRemoteSource : RemoteSource<Client> {
    override suspend fun fetchDataAll(id: String): List<Client> {
        return emptyList()
    }

    override suspend fun uploadData(data: List<Client>): List<Client>? {
        return emptyList()
    }

    override suspend fun fetchData(id: String): Client? {
        TODO("Not yet implemented")
    }

    override suspend fun uploadData(data: Client): Client? {
        TODO("Not yet implemented")
    }
}
