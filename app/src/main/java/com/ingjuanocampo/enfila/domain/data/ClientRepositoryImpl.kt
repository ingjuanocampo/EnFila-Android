package com.ingjuanocampo.enfila.domain.data

import com.ingjuanocampo.enfila.data.backend.source.BackendClientSource
import com.ingjuanocampo.enfila.domain.data.source.LocalSource
import com.ingjuanocampo.enfila.domain.data.source.RemoteSource
import com.ingjuanocampo.enfila.domain.entity.Client
import com.ingjuanocampo.enfila.domain.usecases.repository.ClientRepository

class ClientRepositoryImpl(
    private val remoteSource: RemoteSource<Client>,
    localSource: LocalSource<Client>,
    private val backendClientSource: BackendClientSource? = null, // Optional for backend operations
) : RepositoryImp<Client>(remoteSource, localSource), ClientRepository {
    
    override suspend fun createClient(client: Client): Client? {
        return backendClientSource?.createClient(client)?.also { createdClient ->
            // Cache the created client locally
            createOrUpdate(createdClient)
        }
    }
}
