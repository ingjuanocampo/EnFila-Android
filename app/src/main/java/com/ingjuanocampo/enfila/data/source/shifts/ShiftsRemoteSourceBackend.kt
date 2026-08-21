package com.ingjuanocampo.enfila.data.source.shifts

import com.ingjuanocampo.enfila.data.backend.source.BackendShiftSource
import com.ingjuanocampo.enfila.domain.data.source.RemoteSource
import com.ingjuanocampo.enfila.domain.entity.Shift
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ShiftsRemoteSourceBackend @Inject constructor(
    private val backendShiftSource: BackendShiftSource
) : RemoteSource<Shift> {
    
    override suspend fun fetchDataAll(id: String): List<Shift>? {
        return backendShiftSource.fetchDataAll(id)
    }
    
    override suspend fun fetchData(id: String): Shift? {
        return backendShiftSource.fetchData(id)
    }
    
    override suspend fun uploadData(data: Shift): Shift? {
        return backendShiftSource.uploadData(data)
    }
    
    override suspend fun uploadData(data: List<Shift>): List<Shift>? {
        return backendShiftSource.uploadData(data)
    }
    
    suspend fun fetchShiftsByCompanySite(companySiteId: String): List<Shift>? {
        return backendShiftSource.fetchShiftsByCompanySite(companySiteId)
    }
    
    suspend fun fetchShiftsByContact(contactId: String): List<Shift>? {
        return backendShiftSource.fetchShiftsByContact(contactId)
    }
    
    suspend fun createShift(shift: Shift): Shift? {
        return backendShiftSource.createShift(shift)
    }
    
    suspend fun assignShift(companySiteId: String, contactId: String, notes: String?): Shift? {
        return backendShiftSource.assignShift(companySiteId, contactId, notes)
    }
    
    suspend fun deleteShift(id: String): Boolean {
        return backendShiftSource.deleteShift(id)
    }
}
