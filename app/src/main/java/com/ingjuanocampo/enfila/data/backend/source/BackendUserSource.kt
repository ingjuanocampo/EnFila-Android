package com.ingjuanocampo.enfila.data.backend.source

import com.enfila.data.backend.ApiClient
import com.enfila.data.backend.models.*
import com.ingjuanocampo.enfila.data.backend.mappers.*
import com.ingjuanocampo.enfila.domain.data.source.RemoteSource
import com.ingjuanocampo.enfila.domain.entity.User
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BackendUserSource @Inject constructor(
    private val apiClient: ApiClient
) : RemoteSource<User> {
    
    private val client = apiClient.httpClient
    
    override suspend fun fetchDataAll(id: String): List<User>? {
        return try {
            val response = client.get("${ApiClient.API_V1}/users")
            val apiResponse: ApiResponse<List<BackendUser>> = response.body()
            
            if (apiResponse.success) {
                apiResponse.data?.map { it.toDomainUser() }
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }
    
    override suspend fun fetchData(id: String): User? {
        return try {
            val response = client.get("${ApiClient.API_V1}/users/$id")
            val apiResponse: ApiResponse<BackendUser> = response.body()
            
            if (apiResponse.success) {
                apiResponse.data?.toDomainUser()
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }
    
    suspend fun fetchUserByPhone(phone: String): User? {
        return try {
            val response = client.get("${ApiClient.API_V1}/users/by-phone/$phone")
            val apiResponse: ApiResponse<BackendUser> = response.body()
            
            if (apiResponse.success) {
                apiResponse.data?.toDomainUser()
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }
    
    override fun uploadData(data: User): Flow<User?> = flow {
        try {
            val response = client.put("${ApiClient.API_V1}/users/${data.id}") {
                contentType(ContentType.Application.Json)
                setBody(UpdateUserRequest(
                    name = data.name,
                    companyIds = data.companyIds
                ))
            }
            
            val apiResponse: ApiResponse<BackendUser> = response.body()
            
            if (apiResponse.success) {
                emit(apiResponse.data?.toDomainUser())
            } else {
                emit(null)
            }
        } catch (e: Exception) {
            emit(null)
        }
    }
    
    override fun uploadData(data: List<User>): Flow<List<User>?> = flow {
        try {
            val results = mutableListOf<User>()
            
            data.forEach { user ->
                val response = client.put("${ApiClient.API_V1}/users/${user.id}") {
                    contentType(ContentType.Application.Json)
                    setBody(UpdateUserRequest(
                        name = user.name,
                        companyIds = user.companyIds
                    ))
                }
                
                val apiResponse: ApiResponse<BackendUser> = response.body()
                if (apiResponse.success) {
                    apiResponse.data?.let { results.add(it.toDomainUser()) }
                }
            }
            
            emit(results)
        } catch (e: Exception) {
            emit(null)
        }
    }
    
    suspend fun createUser(user: User): User? {
        return try {
            val response = client.post("${ApiClient.API_V1}/users") {
                contentType(ContentType.Application.Json)
                setBody(user.toCreateUserRequest())
            }
            
            val apiResponse: ApiResponse<BackendUser> = response.body()
            
            if (apiResponse.success) {
                apiResponse.data?.toDomainUser()
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }
    
    suspend fun deleteUser(id: String): Boolean {
        return try {
            val response = client.delete("${ApiClient.API_V1}/users/$id")
            response.status == HttpStatusCode.NoContent
        } catch (e: Exception) {
            false
        }
    }
}
