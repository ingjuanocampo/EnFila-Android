package com.ingjuanocampo.enfila.domain.data.source.contact

import com.ingjuanocampo.enfila.domain.data.source.RemoteSource
import com.ingjuanocampo.enfila.domain.entity.Client

class ContactRemoteSource: RemoteSource<List<Client>> {


    override suspend fun fetchData(id: String): List<Client> {
        return emptyList()
    }

    override suspend fun createOrUpdate(data: List<Client>) {
    }
}