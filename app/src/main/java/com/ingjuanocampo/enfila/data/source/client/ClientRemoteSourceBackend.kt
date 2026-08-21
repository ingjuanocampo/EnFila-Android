package com.ingjuanocampo.enfila.data.source.client

import com.ingjuanocampo.enfila.data.backend.source.BackendClientSource
import com.ingjuanocampo.enfila.domain.data.source.RemoteSource
import com.ingjuanocampo.enfila.domain.entity.Client
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ClientRemoteSourceBackend @Inject constructor(
    private val backendClientSource: BackendClientSource
) : RemoteSource<Client> {
    
    override suspend fun fetchDataAll(id: String): List<Client>? {
        return backendClientSource.fetchDataAll(id)
    }
    
    override suspend fun fetchData(id: String): Client? {
        return backendClientSource.fetchData(id)
    }
    
    override suspend fun uploadData(data: Client): Client? {
        return backendClientSource.uploadData(data)
    }
    
    override suspend fun uploadData(data: List<Client>): List<Client>? {
        return backendClientSource.uploadData(data)
    }
    
    suspend fun createClient(client: Client): Client? {
        return backendClientSource.createClient(client)
    }
    
    suspend fun deleteClient(id: String): Boolean {
        return backendClientSource.deleteClient(id)
    }
}
