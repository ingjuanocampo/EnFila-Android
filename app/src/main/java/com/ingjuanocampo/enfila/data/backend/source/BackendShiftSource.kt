package com.ingjuanocampo.enfila.data.backend.source

import android.util.Log
import com.enfila.data.backend.ApiClient
import com.enfila.data.backend.models.*
import com.ingjuanocampo.enfila.data.backend.mappers.*
import com.ingjuanocampo.enfila.domain.data.source.RemoteSource
import com.ingjuanocampo.enfila.domain.entity.Shift
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BackendShiftSource @Inject constructor(
    private val apiClient: ApiClient
) : RemoteSource<Shift> {
    
    private val client = apiClient.httpClient
    
    companion object {
        private const val TAG = "BackendShiftSource"
    }
    
    override suspend fun fetchDataAll(id: String): List<Shift>? {
        // Use companySiteId as filter if provided
        val url = if (id.isNotEmpty()) {
            "${ApiClient.API_V1}/shifts?companySiteId=$id"
        } else {
            "${ApiClient.API_V1}/shifts"
        }
        Log.d(TAG, "fetchDataAll - Starting request to: $url")
        
        return try {
            val response = client.get(url)
            Log.d(TAG, "fetchDataAll - HTTP Status: ${response.status}")
            
            // Check if response is successful (2xx status codes)
            if (response.status.value in 200..299) {
                val apiResponse: ApiResponse<List<BackendShift>> = response.body()
                
                if (apiResponse.success) {
                    val shifts = apiResponse.data?.map { it.toDomainShift() }
                    Log.d(TAG, "fetchDataAll - Success: Retrieved ${shifts?.size ?: 0} shifts")
                    shifts
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
    
    override suspend fun fetchData(id: String): Shift? {
        val url = "${ApiClient.API_V1}/shifts/$id"
        Log.d(TAG, "fetchData - Starting request to: $url")
        
        return try {
            val response = client.get(url)
            Log.d(TAG, "fetchData - HTTP Status: ${response.status}")
            
            // Check if response is successful (2xx status codes)
            if (response.status.value in 200..299) {
                val apiResponse: ApiResponse<BackendShift> = response.body()
                
                if (apiResponse.success) {
                    val shift = apiResponse.data?.toDomainShift()
                    Log.d(TAG, "fetchData - Success: Retrieved shift with ID: ${shift?.id}")
                    shift
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
    
    suspend fun fetchShiftsByCompanySite(companySiteId: String): List<Shift>? {
        return fetchDataAll(companySiteId)
    }
    
    suspend fun fetchShiftsByContact(contactId: String): List<Shift>? {
        val url = "${ApiClient.API_V1}/shifts?contactId=$contactId"
        Log.d(TAG, "fetchShiftsByContact - Starting request to: $url")
        
        return try {
            val response = client.get(url)
            Log.d(TAG, "fetchShiftsByContact - HTTP Status: ${response.status}")
            
            // Check if response is successful (2xx status codes)
            if (response.status.value in 200..299) {
                val apiResponse: ApiResponse<List<BackendShift>> = response.body()
                
                if (apiResponse.success) {
                    val shifts = apiResponse.data?.map { it.toDomainShift() }
                    Log.d(TAG, "fetchShiftsByContact - Success: Retrieved ${shifts?.size ?: 0} shifts for contact $contactId")
                    shifts
                } else {
                    Log.w(TAG, "fetchShiftsByContact - API Error: ${apiResponse.error}")
                    null
                }
            } else {
                // For non-2xx status codes, try to parse as ErrorResponse
                try {
                    val errorResponse: ErrorResponse = response.body()
                    Log.w(TAG, "fetchShiftsByContact - HTTP Error (${response.status}): ${errorResponse.error}")
                } catch (ex: Exception) {
                    Log.w(TAG, "fetchShiftsByContact - HTTP Error (${response.status}): Could not parse error response")
                }
                null
            }
        } catch (e: ClientRequestException) {
            Log.e(TAG, "fetchShiftsByContact - Client Error (${e.response.status}): ${e.message}")
            null
        } catch (e: ServerResponseException) {
            Log.e(TAG, "fetchShiftsByContact - Server Error (${e.response.status}): ${e.message}")
            null
        } catch (e: Exception) {
            Log.e(TAG, "fetchShiftsByContact - Unexpected error: ${e.javaClass.simpleName}: ${e.message}", e)
            null
        }
    }
    
    override fun uploadData(data: Shift): Flow<Shift?> = flow {
        val url = "${ApiClient.API_V1}/shifts/${data.id}"
        Log.d(TAG, "uploadData (single) - Starting PUT request to: $url for shift: ${data.number}")
        
        try {
            val response = client.put(url) {
                contentType(ContentType.Application.Json)
                setBody(UpdateShiftRequest(
                    number = data.number,
                    notes = data.notes,
                    state = data.state.toBackendShiftState(),
                    attentionStartDate = data.attentionStartDate,
                    endDate = data.endDate
                ))
            }
            
            Log.d(TAG, "uploadData (single) - HTTP Status: ${response.status}")
            val apiResponse: ApiResponse<BackendShift> = response.body()
            
            if (apiResponse.success) {
                val shift = apiResponse.data?.toDomainShift()
                Log.d(TAG, "uploadData (single) - Success: Updated shift ${shift?.id}")
                emit(shift)
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
        } catch (e: JsonConvertException) {
            Log.e(TAG, "uploadData (single) - JSON parsing error, likely HTML error page: ${e.message}")
            emit(null)
        } catch (e: Exception) {
            Log.e(TAG, "uploadData (single) - Unexpected error: ${e.javaClass.simpleName}: ${e.message}", e)
            emit(null)
        }
    }
    
    override fun uploadData(data: List<Shift>): Flow<List<Shift>?> = flow {
        Log.d(TAG, "uploadData (list) - Starting bulk upload for ${data.size} shifts")
        
        try {
            val results = mutableListOf<Shift>()
            var successCount = 0
            var failureCount = 0
            
            data.forEach { shift ->
                val url = "${ApiClient.API_V1}/shifts/${shift.id}"
                try {
                    val response = client.put(url) {
                        contentType(ContentType.Application.Json)
                        setBody(UpdateShiftRequest(
                            number = shift.number,
                            notes = shift.notes,
                            state = shift.state.toBackendShiftState(),
                            attentionStartDate = shift.attentionStartDate,
                            endDate = shift.endDate
                        ))
                    }
                    
                    val apiResponse: ApiResponse<BackendShift> = response.body()
                    if (apiResponse.success) {
                        apiResponse.data?.let { 
                            results.add(it.toDomainShift()) 
                            successCount++
                        }
                    } else {
                        Log.w(TAG, "uploadData (list) - API Error for shift ${shift.id}: ${apiResponse.error}")
                        failureCount++
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "uploadData (list) - Error updating shift ${shift.id}: ${e.message}")
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
    
    suspend fun createShift(shift: Shift): Shift? {
        val url = "${ApiClient.API_V1}/shifts"
        Log.d(TAG, "createShift - Starting POST request to: $url for shift: ${shift.number}")
        
        return try {
            val response = client.post(url) {
                contentType(ContentType.Application.Json)
                setBody(CreateShiftRequest(
                    parentCompanySite = shift.parentCompanySite,
                    number = shift.number,
                    contactId = shift.contactId,
                    notes = shift.notes
                ))
            }
            
            Log.d(TAG, "createShift - HTTP Status: ${response.status}")
            val apiResponse: ApiResponse<BackendShift> = response.body()
            
            if (apiResponse.success) {
                val createdShift = apiResponse.data?.toDomainShift()
                Log.d(TAG, "createShift - Success: Created shift with ID: ${createdShift?.id}")
                createdShift
            } else {
                Log.w(TAG, "createShift - API Error: ${apiResponse.error}")
                null
            }
        } catch (e: ClientRequestException) {
            Log.e(TAG, "createShift - Client Error (${e.response.status}): ${e.message}")
            null
        } catch (e: ServerResponseException) {
            Log.e(TAG, "createShift - Server Error (${e.response.status}): ${e.message}")
            null
        } catch (e: Exception) {
            Log.e(TAG, "createShift - Unexpected error: ${e.javaClass.simpleName}: ${e.message}", e)
            null
        }
    }
    
    suspend fun assignShift(companySiteId: String, contactId: String, notes: String?): Shift? {
        val url = "${ApiClient.API_V1}/shifts/assign"
        Log.d(TAG, "assignShift - Starting POST request to: $url for companySite: $companySiteId, contact: $contactId")
        
        return try {
            val response = client.post(url) {
                contentType(ContentType.Application.Json)
                setBody(AssignShiftRequest(
                    companySiteId = companySiteId,
                    contactId = contactId,
                    notes = notes
                ))
            }
            
            Log.d(TAG, "assignShift - HTTP Status: ${response.status}")
            val apiResponse: ApiResponse<BackendShift> = response.body()
            
            if (apiResponse.success) {
                val assignedShift = apiResponse.data?.toDomainShift()
                Log.d(TAG, "assignShift - Success: Assigned shift with ID: ${assignedShift?.id}")
                assignedShift
            } else {
                Log.w(TAG, "assignShift - API Error: ${apiResponse.error}")
                null
            }
        } catch (e: ClientRequestException) {
            Log.e(TAG, "assignShift - Client Error (${e.response.status}): ${e.message}")
            null
        } catch (e: ServerResponseException) {
            Log.e(TAG, "assignShift - Server Error (${e.response.status}): ${e.message}")
            null
        } catch (e: Exception) {
            Log.e(TAG, "assignShift - Unexpected error: ${e.javaClass.simpleName}: ${e.message}", e)
            null
        }
    }
    
    suspend fun deleteShift(id: String): Boolean {
        val url = "${ApiClient.API_V1}/shifts/$id"
        Log.d(TAG, "deleteShift - Starting DELETE request to: $url")
        
        return try {
            val response = client.delete(url)
            Log.d(TAG, "deleteShift - HTTP Status: ${response.status}")
            
            val success = response.status == HttpStatusCode.NoContent
            if (success) {
                Log.d(TAG, "deleteShift - Success: Deleted shift with ID: $id")
            } else {
                Log.w(TAG, "deleteShift - Unexpected status code: ${response.status}")
            }
            success
        } catch (e: ClientRequestException) {
            Log.e(TAG, "deleteShift - Client Error (${e.response.status}): ${e.message}")
            false
        } catch (e: ServerResponseException) {
            Log.e(TAG, "deleteShift - Server Error (${e.response.status}): ${e.message}")
            false
        } catch (e: Exception) {
            Log.e(TAG, "deleteShift - Unexpected error: ${e.javaClass.simpleName}: ${e.message}", e)
            false
        }
    }
}
