package com.ingjuanocampo.enfila.data.backend.source

import android.util.Log
import com.enfila.data.backend.ApiClient
import com.enfila.data.backend.models.*
import com.ingjuanocampo.enfila.data.backend.mappers.*
import com.ingjuanocampo.enfila.domain.data.source.RemoteSource
import com.ingjuanocampo.enfila.domain.entity.User
import io.ktor.client.call.*
import io.ktor.client.plugins.*
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

    companion object {
        private const val TAG = "BackendUserSource"
    }

    override suspend fun fetchDataAll(id: String): List<User>? {
        val url = "${ApiClient.API_V1}/users"
        Log.d(TAG, "fetchDataAll - Starting request to: $url")

        return try {
            val response = client.get(url)
            Log.d(TAG, "fetchDataAll - HTTP Status: ${response.status}")

            // Check if response is successful (2xx status codes)
            if (response.status.value in 200..299) {
                val apiResponse: ApiResponse<List<BackendUser>> = response.body()

                if (apiResponse.success) {
                    val users = apiResponse.data?.map { it.toDomainUser() }
                    Log.d(TAG, "fetchDataAll - Success: Retrieved ${users?.size ?: 0} users")
                    users
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
            try {
                val errorResponse: ErrorResponse = e.response.body()
                Log.e(TAG, "fetchDataAll - Error details: ${errorResponse.error}")
            } catch (ex: Exception) {
                Log.e(TAG, "fetchDataAll - Could not parse error response: ${ex.message}")
            }
            null
        } catch (e: ServerResponseException) {
            Log.e(TAG, "fetchDataAll - Server Error (${e.response.status}): ${e.message}")
            try {
                val errorResponse: ErrorResponse = e.response.body()
                Log.e(TAG, "fetchDataAll - Error details: ${errorResponse.error}")
            } catch (ex: Exception) {
                Log.e(TAG, "fetchDataAll - Could not parse error response: ${ex.message}")
            }
            null
        } catch (e: Exception) {
            Log.e(TAG, "fetchDataAll - Unexpected error: ${e.javaClass.simpleName}: ${e.message}", e)
            null
        }
    }

    override suspend fun fetchData(id: String): User? {
        val url = "${ApiClient.API_V1}/users/$id"
        Log.d(TAG, "fetchData - Starting request to: $url")

        return try {
            val response = client.get(url)
            Log.d(TAG, "fetchData - HTTP Status: ${response.status}")

            // Check if response is successful (2xx status codes)
            if (response.status.value in 200..299) {
                val apiResponse: ApiResponse<BackendUser> = response.body()

                if (apiResponse.success) {
                    val user = apiResponse.data?.toDomainUser()
                    Log.d(TAG, "fetchData - Success: Retrieved user with ID: ${user?.id}")
                    user
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
            // Try to get error details from response body
            try {
                val errorResponse: ErrorResponse = e.response.body()
                Log.e(TAG, "fetchData - Error details: ${errorResponse.error}")
            } catch (ex: Exception) {
                Log.e(TAG, "fetchData - Could not parse error response: ${ex.message}")
            }
            null
        } catch (e: ServerResponseException) {
            Log.e(TAG, "fetchData - Server Error (${e.response.status}): ${e.message}")
            // Try to get error details from response body
            try {
                val errorResponse: ErrorResponse = e.response.body()
                Log.e(TAG, "fetchData - Error details: ${errorResponse.error}")
            } catch (ex: Exception) {
                Log.e(TAG, "fetchData - Could not parse error response: ${ex.message}")
            }
            null
        } catch (e: Exception) {
            Log.e(TAG, "fetchData - Unexpected error: ${e.javaClass.simpleName}: ${e.message}", e)
            null
        }
    }

    suspend fun fetchUserByPhone(phone: String): User? {
        val url = "${ApiClient.API_V1}/users/by-phone/$phone"
        Log.d(TAG, "fetchUserByPhone - Starting request to: $url")

        return try {
            val response = client.get(url)
            Log.d(TAG, "fetchUserByPhone - HTTP Status: ${response.status}")

            // Check if response is successful (2xx status codes)
            if (response.status.value in 200..299) {
                val apiResponse: ApiResponse<BackendUser> = response.body()

                if (apiResponse.success) {
                    val user = apiResponse.data?.toDomainUser()
                    Log.d(TAG, "fetchUserByPhone - Success: Retrieved user with phone: $phone, ID: ${user?.id}")
                    user
                } else {
                    Log.w(TAG, "fetchUserByPhone - API Error: ${apiResponse.error}")
                    null
                }
            } else {
                // For non-2xx status codes, try to parse as ErrorResponse
                try {
                    val errorResponse: ErrorResponse = response.body()
                    Log.w(TAG, "fetchUserByPhone - HTTP Error (${response.status}): ${errorResponse.error}")
                } catch (ex: Exception) {
                    Log.w(TAG, "fetchUserByPhone - HTTP Error (${response.status}): Could not parse error response")
                }
                null
            }
        } catch (e: ClientRequestException) {
            Log.e(TAG, "fetchUserByPhone - Client Error (${e.response.status}): ${e.message}")
            try {
                val errorResponse: ErrorResponse = e.response.body()
                Log.e(TAG, "fetchUserByPhone - Error details: ${errorResponse.error}")
            } catch (ex: Exception) {
                Log.e(TAG, "fetchUserByPhone - Could not parse error response: ${ex.message}")
            }
            null
        } catch (e: ServerResponseException) {
            Log.e(TAG, "fetchUserByPhone - Server Error (${e.response.status}): ${e.message}")
            try {
                val errorResponse: ErrorResponse = e.response.body()
                Log.e(TAG, "fetchUserByPhone - Error details: ${errorResponse.error}")
            } catch (ex: Exception) {
                Log.e(TAG, "fetchUserByPhone - Could not parse error response: ${ex.message}")
            }
            null
        } catch (e: Exception) {
            Log.e(TAG, "fetchUserByPhone - Unexpected error: ${e.javaClass.simpleName}: ${e.message}", e)
            null
        }
    }

    override fun uploadData(data: User): Flow<User?> = flow {
        val url = "${ApiClient.API_V1}/users/${data.id}"
        Log.d(TAG, "uploadData (single) - Starting PUT request to: $url for user: ${data.name}")

        try {
            val response = client.put(url) {
                contentType(ContentType.Application.Json)
                setBody(UpdateUserRequest(
                    name = data.name,
                    companyIds = data.companyIds
                ))
            }

            val apiResponse: ApiResponse<BackendUser> = response.body()

            if (apiResponse.success) {
                val user = apiResponse.data?.toDomainUser()
                Log.d(TAG, "uploadData (single) - Success: Updated user ${user?.id}")
                emit(user)
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

    override fun uploadData(data: List<User>): Flow<List<User>?> = flow {
        Log.d(TAG, "uploadData (list) - Starting bulk upload for ${data.size} users")

        try {
            val results = mutableListOf<User>()
            var successCount = 0
            var failureCount = 0

            data.forEach { user ->
                val url = "${ApiClient.API_V1}/users/${user.id}"
                try {
                    val response = client.put(url) {
                        contentType(ContentType.Application.Json)
                        setBody(UpdateUserRequest(
                            name = user.name,
                            companyIds = user.companyIds
                        ))
                    }

                    val apiResponse: ApiResponse<BackendUser> = response.body()
                    if (apiResponse.success) {
                        apiResponse.data?.let {
                            results.add(it.toDomainUser())
                            successCount++
                        }
                    } else {
                        Log.w(TAG, "uploadData (list) - API Error for user ${user.id}: ${apiResponse.error}")
                        failureCount++
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "uploadData (list) - Error updating user ${user.id}: ${e.message}")
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

    suspend fun createUser(user: User): User? {
        val url = "${ApiClient.API_V1}/users"
        Log.d(TAG, "createUser - Starting POST request to: $url for user: ${user.name}")

        return try {
            val response = client.post(url) {
                contentType(ContentType.Application.Json)
                setBody(user.toCreateUserRequest())
            }

            Log.d(TAG, "createUser - HTTP Status: ${response.status}")
            val apiResponse: ApiResponse<BackendUser> = response.body()

            if (apiResponse.success) {
                val createdUser = apiResponse.data?.toDomainUser()
                Log.d(TAG, "createUser - Success: Created user with ID: ${createdUser?.id}")
                createdUser
            } else {
                Log.w(TAG, "createUser - API Error: ${apiResponse.error}")
                null
            }
        } catch (e: ClientRequestException) {
            Log.e(TAG, "createUser - Client Error (${e.response.status}): ${e.message}")
            null
        } catch (e: ServerResponseException) {
            Log.e(TAG, "createUser - Server Error (${e.response.status}): ${e.message}")
            null
        } catch (e: Exception) {
            Log.e(TAG, "createUser - Unexpected error: ${e.javaClass.simpleName}: ${e.message}", e)
            null
        }
    }

    suspend fun deleteUser(id: String): Boolean {
        val url = "${ApiClient.API_V1}/users/$id"
        Log.d(TAG, "deleteUser - Starting DELETE request to: $url")

        return try {
            val response = client.delete(url)
            Log.d(TAG, "deleteUser - HTTP Status: ${response.status}")

            val success = response.status == HttpStatusCode.NoContent
            if (success) {
                Log.d(TAG, "deleteUser - Success: Deleted user with ID: $id")
            } else {
                Log.w(TAG, "deleteUser - Unexpected status code: ${response.status}")
            }
            success
        } catch (e: ClientRequestException) {
            Log.e(TAG, "deleteUser - Client Error (${e.response.status}): ${e.message}")
            false
        } catch (e: ServerResponseException) {
            Log.e(TAG, "deleteUser - Server Error (${e.response.status}): ${e.message}")
            false
        } catch (e: Exception) {
            Log.e(TAG, "deleteUser - Unexpected error: ${e.javaClass.simpleName}: ${e.message}", e)
            false
        }
    }
}
