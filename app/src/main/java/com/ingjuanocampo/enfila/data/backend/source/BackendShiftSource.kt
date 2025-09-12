package com.ingjuanocampo.enfila.data.backend.source

import com.enfila.data.backend.ApiClient
import com.enfila.data.backend.models.*
import com.ingjuanocampo.enfila.data.backend.mappers.*
import com.ingjuanocampo.enfila.domain.data.source.RemoteSource
import com.ingjuanocampo.enfila.domain.entity.Shift
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BackendShiftSource @Inject constructor(
    private val apiClient: ApiClient
) : RemoteSource<Shift> {
    
    private val client = apiClient.httpClient
    
    override suspend fun fetchDataAll(id: String): List<Shift>? {
        return try {
            // Use companySiteId as filter if provided
            val url = if (id.isNotEmpty()) {
                "${ApiClient.API_V1}/shifts?companySiteId=$id"
            } else {
                "${ApiClient.API_V1}/shifts"
            }
            
            val response = client.get(url)
            val apiResponse: ApiResponse<List<BackendShift>> = response.body()
            
            if (apiResponse.success) {
                apiResponse.data?.map { it.toDomainShift() }
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }
    
    override suspend fun fetchData(id: String): Shift? {
        return try {
            val response = client.get("${ApiClient.API_V1}/shifts/$id")
            val apiResponse: ApiResponse<BackendShift> = response.body()
            
            if (apiResponse.success) {
                apiResponse.data?.toDomainShift()
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }
    
    suspend fun fetchShiftsByCompanySite(companySiteId: String): List<Shift>? {
        return fetchDataAll(companySiteId)
    }
    
    suspend fun fetchShiftsByContact(contactId: String): List<Shift>? {
        return try {
            val response = client.get("${ApiClient.API_V1}/shifts?contactId=$contactId")
            val apiResponse: ApiResponse<List<BackendShift>> = response.body()
            
            if (apiResponse.success) {
                apiResponse.data?.map { it.toDomainShift() }
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }
    
    override fun uploadData(data: Shift): Flow<Shift?> = flow {
        try {
            val response = client.put("${ApiClient.API_V1}/shifts/${data.id}") {
                contentType(ContentType.Application.Json)
                setBody(UpdateShiftRequest(
                    number = data.number,
                    notes = data.notes,
                    state = data.state.toBackendShiftState(),
                    attentionStartDate = data.attentionStartDate,
                    endDate = data.endDate
                ))
            }
            
            val apiResponse: ApiResponse<BackendShift> = response.body()
            
            if (apiResponse.success) {
                emit(apiResponse.data?.toDomainShift())
            } else {
                emit(null)
            }
        } catch (e: Exception) {
            emit(null)
        }
    }
    
    override fun uploadData(data: List<Shift>): Flow<List<Shift>?> = flow {
        try {
            val results = mutableListOf<Shift>()
            
            data.forEach { shift ->
                val response = client.put("${ApiClient.API_V1}/shifts/${shift.id}") {
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
                    apiResponse.data?.let { results.add(it.toDomainShift()) }
                }
            }
            
            emit(results)
        } catch (e: Exception) {
            emit(null)
        }
    }
    
    suspend fun createShift(shift: Shift): Shift? {
        return try {
            val response = client.post("${ApiClient.API_V1}/shifts") {
                contentType(ContentType.Application.Json)
                setBody(CreateShiftRequest(
                    parentCompanySite = shift.parentCompanySite,
                    number = shift.number,
                    contactId = shift.contactId,
                    notes = shift.notes
                ))
            }
            
            val apiResponse: ApiResponse<BackendShift> = response.body()
            
            if (apiResponse.success) {
                apiResponse.data?.toDomainShift()
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }
    
    suspend fun assignShift(companySiteId: String, contactId: String, notes: String?): Shift? {
        return try {
            val response = client.post("${ApiClient.API_V1}/shifts/assign") {
                contentType(ContentType.Application.Json)
                setBody(AssignShiftRequest(
                    companySiteId = companySiteId,
                    contactId = contactId,
                    notes = notes
                ))
            }
            
            val apiResponse: ApiResponse<BackendShift> = response.body()
            
            if (apiResponse.success) {
                apiResponse.data?.toDomainShift()
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }
    
    suspend fun deleteShift(id: String): Boolean {
        return try {
            val response = client.delete("${ApiClient.API_V1}/shifts/$id")
            response.status == HttpStatusCode.NoContent
        } catch (e: Exception) {
            false
        }
    }
}
