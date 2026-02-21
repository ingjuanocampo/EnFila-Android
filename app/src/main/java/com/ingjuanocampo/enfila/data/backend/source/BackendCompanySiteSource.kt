package com.ingjuanocampo.enfila.data.backend.source

import android.util.Log
import com.enfila.data.backend.ApiClient
import com.enfila.data.backend.models.*
import com.ingjuanocampo.enfila.data.backend.mappers.toDomainCompanySite
import com.ingjuanocampo.enfila.domain.data.source.RemoteSource
import com.ingjuanocampo.enfila.domain.entity.CompanySite
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.http.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BackendCompanySiteSource @Inject constructor(
    private val apiClient: ApiClient
) : RemoteSource<CompanySite> {

    private val client = apiClient.httpClient

    companion object {
        private const val TAG = "BackendCompanySiteSource"
    }

    override suspend fun fetchDataAll(id: String): List<CompanySite>? {
        val url = "${ApiClient.API_V1}/company-sites"
        Log.d(TAG, "fetchDataAll - Starting request to: $url")

        return try {
            val response = client.get(url)
            Log.d(TAG, "fetchDataAll - HTTP Status: ${response.status}")

            if (response.status.value in 200..299) {
                val apiResponse: ApiResponse<List<BackendCompanySite>> = response.body()

                if (apiResponse.success) {
                    val companySites = apiResponse.data?.map { it.toDomainCompanySite() }
                    Log.d(TAG, "fetchDataAll - Success: Retrieved ${companySites?.size ?: 0} company sites")
                    companySites
                } else {
                    Log.w(TAG, "fetchDataAll - API Error: ${apiResponse.error}")
                    null
                }
            } else {
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

    override suspend fun fetchData(id: String): CompanySite? {
        val url = "${ApiClient.API_V1}/company-sites/$id"
        Log.d(TAG, "fetchData - Starting request to: $url")

        return try {
            val response = client.get(url)
            Log.d(TAG, "fetchData - HTTP Status: ${response.status}")

            if (response.status.value in 200..299) {
                val apiResponse: ApiResponse<BackendCompanySite> = response.body()

                if (apiResponse.success) {
                    val companySite = apiResponse.data?.toDomainCompanySite()
                    Log.d(TAG, "fetchData - Success: Retrieved company site with ID: $id")
                    companySite
                } else {
                    Log.w(TAG, "fetchData - API Error: ${apiResponse.error}")
                    null
                }
            } else {
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

    override suspend fun uploadData(data: CompanySite): CompanySite? {
        val url = "${ApiClient.API_V1}/company-sites/${data.id}"
        Log.d(TAG, "uploadData - Starting PUT request to: $url for company site: ${data.name}")

        return try {
            val response = client.put(url) {
                contentType(ContentType.Application.Json)
                setBody(UpdateCompanySiteRequest(name = data.name))
            }

            Log.d(TAG, "uploadData - HTTP Status: ${response.status}")
            val apiResponse: ApiResponse<BackendCompanySite> = response.body()

            if (apiResponse.success) {
                val companySite = apiResponse.data?.toDomainCompanySite()
                Log.d(TAG, "uploadData - Success: Updated company site ${companySite?.id}")
                companySite
            } else {
                Log.w(TAG, "uploadData - API Error: ${apiResponse.error}")
                null
            }
        } catch (e: ClientRequestException) {
            Log.e(TAG, "uploadData - Client Error (${e.response.status}): ${e.message}")
            null
        } catch (e: ServerResponseException) {
            Log.e(TAG, "uploadData - Server Error (${e.response.status}): ${e.message}")
            null
        } catch (e: Exception) {
            Log.e(TAG, "uploadData - Unexpected error: ${e.javaClass.simpleName}: ${e.message}", e)
            null
        }
    }

    override suspend fun uploadData(data: List<CompanySite>): List<CompanySite>? {
        Log.d(TAG, "uploadData (list) - Starting bulk upload for ${data.size} company sites")

        return try {
            val results = mutableListOf<CompanySite>()
            var successCount = 0
            var failureCount = 0

            data.forEach { companySite ->
                val url = "${ApiClient.API_V1}/company-sites/${companySite.id}"
                try {
                    val response = client.put(url) {
                        contentType(ContentType.Application.Json)
                        setBody(UpdateCompanySiteRequest(name = companySite.name))
                    }

                    val apiResponse: ApiResponse<BackendCompanySite> = response.body()
                    if (apiResponse.success) {
                        apiResponse.data?.let {
                            results.add(it.toDomainCompanySite())
                            successCount++
                        }
                    } else {
                        Log.w(TAG, "uploadData (list) - API Error for company site ${companySite.id}: ${apiResponse.error}")
                        failureCount++
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "uploadData (list) - Error updating company site ${companySite.id}: ${e.message}")
                    failureCount++
                }
            }

            Log.d(TAG, "uploadData (list) - Completed: $successCount successful, $failureCount failed")
            results
        } catch (e: Exception) {
            Log.e(TAG, "uploadData (list) - Unexpected error: ${e.javaClass.simpleName}: ${e.message}", e)
            null
        }
    }

    suspend fun createCompanySite(companySite: CompanySite): CompanySite? {
        val url = "${ApiClient.API_V1}/company-sites"
        Log.d(TAG, "createCompanySite - Starting POST request to: $url for company site: ${companySite.name}")

        return try {
            val response = client.post(url) {
                contentType(ContentType.Application.Json)
                setBody(CreateCompanySiteRequest(name = companySite.name ?: ""))
            }

            Log.d(TAG, "createCompanySite - HTTP Status: ${response.status}")
            val apiResponse: ApiResponse<BackendCompanySite> = response.body()

            if (apiResponse.success) {
                val createdCompanySite = apiResponse.data?.toDomainCompanySite()
                Log.d(TAG, "createCompanySite - Success: Created company site with ID: ${createdCompanySite?.id}")
                createdCompanySite
            } else {
                Log.w(TAG, "createCompanySite - API Error: ${apiResponse.error}")
                null
            }
        } catch (e: ClientRequestException) {
            Log.e(TAG, "createCompanySite - Client Error (${e.response.status}): ${e.message}")
            null
        } catch (e: ServerResponseException) {
            Log.e(TAG, "createCompanySite - Server Error (${e.response.status}): ${e.message}")
            null
        } catch (e: Exception) {
            Log.e(TAG, "createCompanySite - Unexpected error: ${e.javaClass.simpleName}: ${e.message}", e)
            null
        }
    }

    suspend fun deleteCompanySite(id: String): Boolean {
        val url = "${ApiClient.API_V1}/company-sites/$id"
        Log.d(TAG, "deleteCompanySite - Starting DELETE request to: $url")

        return try {
            val response = client.delete(url)
            Log.d(TAG, "deleteCompanySite - HTTP Status: ${response.status}")

            response.status.value in 200..299
        } catch (e: ClientRequestException) {
            Log.e(TAG, "deleteCompanySite - Client Error (${e.response.status}): ${e.message}")
            false
        } catch (e: ServerResponseException) {
            Log.e(TAG, "deleteCompanySite - Server Error (${e.response.status}): ${e.message}")
            false
        } catch (e: Exception) {
            Log.e(TAG, "deleteCompanySite - Unexpected error: ${e.javaClass.simpleName}: ${e.message}", e)
            false
        }
    }
}
