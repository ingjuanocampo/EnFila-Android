package com.ingjuanocampo.enfila.data.backend.source

import android.util.Log
import com.enfila.data.backend.ApiClient
import com.enfila.data.backend.models.*
import com.ingjuanocampo.enfila.data.backend.mappers.*
import com.ingjuanocampo.enfila.domain.data.source.RemoteSource
import com.ingjuanocampo.enfila.domain.entity.Client
import io.ktor.client.call.*
import io.ktor.client.plugins.*
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
    
    companion object {
        private const val TAG = "BackendClientSource"
    }
    
    override suspend fun fetchDataAll(id: String): List<Client>? {
        val url = "${ApiClient.API_V1}/clients"
        Log.d(TAG, "fetchDataAll - Starting request to: $url")
        
        return try {
            val response = client.get(url)
            Log.d(TAG, "fetchDataAll - HTTP Status: ${response.status}")
            
            // Check if response is successful (2xx status codes)
            if (response.status.value in 200..299) {
                val apiResponse: ApiResponse<List<BackendClient>> = response.body()
                
                if (apiResponse.success) {
                    val clients = apiResponse.data?.map { it.toDomainClient() }
                    Log.d(TAG, "fetchDataAll - Success: Retrieved ${clients?.size ?: 0} clients")
                    clients
                } else {
                    Log.w(TAG, "fetchDataAll - API Error: ${apiResponse.error}")
                    null
                }
            } else {
                // For non-2xx status codes, try to parse as ErrorResponse
                try {
                    val errorResponse: ErrorResponse = response.body()
                    Log.w(TAG, "fetchDataAll - HTTP Error (${response.status}): ${errorResponse.error}")
                } catch (ex: Exception) {
                    Log.w(TAG, "fetchDataAll - HTTP Error (${response.status}): Could not parse error response")
                }
                null
            }
        } catch (e: ClientRequestException) {
            Log.e(TAG, "fetchDataAll - Client Error (${e.response.status}): ${e.message}")
            null
        } catch (e: ServerResponseException) {
            Log.e(TAG, "fetchDataAll - Server Error (${e.response.status}): ${e.message}")
            null
        } catch (e: Exception) {
            Log.e(TAG, "fetchDataAll - Unexpected error: ${e.javaClass.simpleName}: ${e.message}", e)
            null
        }
    }
    
    override suspend fun fetchData(id: String): Client? {
        val url = "${ApiClient.API_V1}/clients/$id"
        Log.d(TAG, "fetchData - Starting request to: $url")
        
        return try {
            val response = client.get(url)
            Log.d(TAG, "fetchData - HTTP Status: ${response.status}")
            
            // Check if response is successful (2xx status codes)
            if (response.status.value in 200..299) {
                val apiResponse: ApiResponse<BackendClient> = response.body()
                
                if (apiResponse.success) {
                    val client = apiResponse.data?.toDomainClient()
                    Log.d(TAG, "fetchData - Success: Retrieved client with ID: ${client?.id}")
                    client
                } else {
                    Log.w(TAG, "fetchData - API Error: ${apiResponse.error}")
                    null
                }
            } else {
                // For non-2xx status codes, try to parse as ErrorResponse
                try {
                    val errorResponse: ErrorResponse = response.body()
                    Log.w(TAG, "fetchData - HTTP Error (${response.status}): ${errorResponse.error}")
                } catch (ex: Exception) {
                    Log.w(TAG, "fetchData - HTTP Error (${response.status}): Could not parse error response")
                }
                null
            }
        } catch (e: ClientRequestException) {
            Log.e(TAG, "fetchData - Client Error (${e.response.status}): ${e.message}")
            null
        } catch (e: ServerResponseException) {
            Log.e(TAG, "fetchData - Server Error (${e.response.status}): ${e.message}")
            null
        } catch (e: Exception) {
            Log.e(TAG, "fetchData - Unexpected error: ${e.javaClass.simpleName}: ${e.message}", e)
            null
        }
    }
    
    override fun uploadData(data: Client): Flow<Client?> = flow {
        val url = "${ApiClient.API_V1}/clients/${data.id}"
        Log.d(TAG, "uploadData (single) - Starting PUT request to: $url for client: ${data.name}")
        
        try {
            val response = client.put(url) {
                contentType(ContentType.Application.Json)
                setBody(UpdateClientRequest(name = data.name))
            }
            
            Log.d(TAG, "uploadData (single) - HTTP Status: ${response.status}")
            val apiResponse: ApiResponse<BackendClient> = response.body()
            
            if (apiResponse.success) {
                val client = apiResponse.data?.toDomainClient()
                Log.d(TAG, "uploadData (single) - Success: Updated client ${client?.id}")
                emit(client)
            } else {
                Log.w(TAG, "uploadData (single) - API Error: ${apiResponse.error}")
                emit(null)
            }
        } catch (e: ClientRequestException) {
            Log.e(TAG, "uploadData (single) - Client Error (${e.response.status}): ${e.message}")
            emit(null)
        } catch (e: ServerResponseException) {
            Log.e(TAG, "uploadData (single) - Server Error (${e.response.status}): ${e.message}")
            emit(null)
        } catch (e: Exception) {
            Log.e(TAG, "uploadData (single) - Unexpected error: ${e.javaClass.simpleName}: ${e.message}", e)
            emit(null)
        }
    }
    
    override fun uploadData(data: List<Client>): Flow<List<Client>?> = flow {
        Log.d(TAG, "uploadData (list) - Starting bulk upload for ${data.size} clients")
        
        try {
            val results = mutableListOf<Client>()
            var successCount = 0
            var failureCount = 0
            
            data.forEach { client ->
                val url = "${ApiClient.API_V1}/clients/${client.id}"
                try {
                    val response = this@BackendClientSource.client.put(url) {
                        contentType(ContentType.Application.Json)
                        setBody(UpdateClientRequest(name = client.name))
                    }
                    
                    val apiResponse: ApiResponse<BackendClient> = response.body()
                    if (apiResponse.success) {
                        apiResponse.data?.let { 
                            results.add(it.toDomainClient()) 
                            successCount++
                        }
                    } else {
                        Log.w(TAG, "uploadData (list) - API Error for client ${client.id}: ${apiResponse.error}")
                        failureCount++
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "uploadData (list) - Error updating client ${client.id}: ${e.message}")
                    failureCount++
                }
            }
            
            Log.d(TAG, "uploadData (list) - Completed: $successCount successful, $failureCount failed")
            emit(results)
        } catch (e: Exception) {
            Log.e(TAG, "uploadData (list) - Unexpected error: ${e.javaClass.simpleName}: ${e.message}", e)
            emit(null)
        }
    }
    
    suspend fun createClient(client: Client): Client? {
        val url = "${ApiClient.API_V1}/clients"
        Log.d(TAG, "createClient - Starting POST request to: $url for client: ${client.name}")
        
        return try {
            val response = this.client.post(url) {
                contentType(ContentType.Application.Json)
                setBody(client.toCreateClientRequest())
            }
            
            Log.d(TAG, "createClient - HTTP Status: ${response.status}")
            val apiResponse: ApiResponse<BackendClient> = response.body()
            
            if (apiResponse.success) {
                val createdClient = apiResponse.data?.toDomainClient()
                Log.d(TAG, "createClient - Success: Created client with ID: ${createdClient?.id}")
                createdClient
            } else {
                Log.w(TAG, "createClient - API Error: ${apiResponse.error}")
                null
            }
        } catch (e: ClientRequestException) {
            Log.e(TAG, "createClient - Client Error (${e.response.status}): ${e.message}")
            null
        } catch (e: ServerResponseException) {
            Log.e(TAG, "createClient - Server Error (${e.response.status}): ${e.message}")
            null
        } catch (e: Exception) {
            Log.e(TAG, "createClient - Unexpected error: ${e.javaClass.simpleName}: ${e.message}", e)
            null
        }
    }
    
    suspend fun deleteClient(id: String): Boolean {
        val url = "${ApiClient.API_V1}/clients/$id"
        Log.d(TAG, "deleteClient - Starting DELETE request to: $url")
        
        return try {
            val response = client.delete(url)
            Log.d(TAG, "deleteClient - HTTP Status: ${response.status}")
            
            val success = response.status == HttpStatusCode.NoContent
            if (success) {
                Log.d(TAG, "deleteClient - Success: Deleted client with ID: $id")
            } else {
                Log.w(TAG, "deleteClient - Unexpected status code: ${response.status}")
            }
            success
        } catch (e: ClientRequestException) {
            Log.e(TAG, "deleteClient - Client Error (${e.response.status}): ${e.message}")
            false
        } catch (e: ServerResponseException) {
            Log.e(TAG, "deleteClient - Server Error (${e.response.status}): ${e.message}")
            false
        } catch (e: Exception) {
            Log.e(TAG, "deleteClient - Unexpected error: ${e.javaClass.simpleName}: ${e.message}", e)
            false
        }
    }
}
