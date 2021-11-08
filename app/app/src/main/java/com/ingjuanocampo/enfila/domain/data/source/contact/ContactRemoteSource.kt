package com.ingjuanocampo.enfila.domain.data.source.contact

import com.ingjuanocampo.enfila.domain.data.source.RemoteSource
import com.ingjuanocampo.enfila.domain.entity.Client
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class ContactRemoteSource: RemoteSource<List<Client>> {


    override suspend fun fetchData(id: String): List<Client> {
        return emptyList()
    }

    override fun uploadData(data: List<Client>): Flow<List<Client>?> {
        return flowOf(emptyList())
    }
}