package com.ingjuanocampo.enfila.data.backend.source

import com.enfila.data.backend.ApiClient
import com.enfila.data.backend.models.*
import com.ingjuanocampo.enfila.data.backend.mappers.*
import com.ingjuanocampo.enfila.domain.data.source.RemoteSource
import com.ingjuanocampo.enfila.domain.entity.Client
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BackendClientSource @Inject constructor(
    private val apiClient: ApiClient
) : RemoteSource<Client> {
    
    private val client = apiClient.httpClient
    
    override suspend fun fetchDataAll(id: String): List<Client>? {
        return try {
            val response = client.get("${ApiClient.API_V1}/clients")
            val apiResponse: ApiResponse<List<BackendClient>> = response.body()
            
            if (apiResponse.success) {
                apiResponse.data?.map { it.toDomainClient() }
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }
    
    override suspend fun fetchData(id: String): Client? {
        return try {
            val response = client.get("${ApiClient.API_V1}/clients/$id")
            val apiResponse: ApiResponse<BackendClient> = response.body()
            
            if (apiResponse.success) {
                apiResponse.data?.toDomainClient()
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }
    
    override fun uploadData(data: Client): Flow<Client?> = flow {
        try {
            val response = client.put("${ApiClient.API_V1}/clients/${data.id}") {
                contentType(ContentType.Application.Json)
                setBody(UpdateClientRequest(name = data.name))
            }
            
            val apiResponse: ApiResponse<BackendClient> = response.body()
            
            if (apiResponse.success) {
                emit(apiResponse.data?.toDomainClient())
            } else {
                emit(null)
            }
        } catch (e: Exception) {
            emit(null)
        }
    }
    
    override fun uploadData(data: List<Client>): Flow<List<Client>?> = flow {
        try {
            val results = mutableListOf<Client>()
            
            data.forEach { client ->
                val response = this@BackendClientSource.client.put("${ApiClient.API_V1}/clients/${client.id}") {
                    contentType(ContentType.Application.Json)
                    setBody(UpdateClientRequest(name = client.name))
                }
                
                val apiResponse: ApiResponse<BackendClient> = response.body()
                if (apiResponse.success) {
                    apiResponse.data?.let { results.add(it.toDomainClient()) }
                }
            }
            
            emit(results)
        } catch (e: Exception) {
            emit(null)
        }
    }
    
    suspend fun createClient(client: Client): Client? {
        return try {
            val response = this.client.post("${ApiClient.API_V1}/clients") {
                contentType(ContentType.Application.Json)
                setBody(client.toCreateClientRequest())
            }
            
            val apiResponse: ApiResponse<BackendClient> = response.body()
            
            if (apiResponse.success) {
                apiResponse.data?.toDomainClient()
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }
    
    suspend fun deleteClient(id: String): Boolean {
        return try {
            val response = client.delete("${ApiClient.API_V1}/clients/$id")
            response.status == HttpStatusCode.NoContent
        } catch (e: Exception) {
            false
        }
    }
}
