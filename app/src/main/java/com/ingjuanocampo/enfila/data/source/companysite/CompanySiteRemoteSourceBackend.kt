package com.ingjuanocampo.enfila.data.source.companysite

import com.ingjuanocampo.enfila.data.backend.source.BackendCompanySiteSource
import com.ingjuanocampo.enfila.domain.data.source.RemoteSource
import com.ingjuanocampo.enfila.domain.entity.CompanySite
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CompanySiteRemoteSourceBackend @Inject constructor(
    private val backendCompanySiteSource: BackendCompanySiteSource
) : RemoteSource<CompanySite> {
    
    override suspend fun fetchDataAll(id: String): List<CompanySite>? {
        return backendCompanySiteSource.fetchDataAll(id)
    }
    
    override suspend fun fetchData(id: String): CompanySite? {
        return backendCompanySiteSource.fetchData(id)
    }
    
    override suspend fun uploadData(data: CompanySite): CompanySite? {
        return backendCompanySiteSource.uploadData(data)
    }
    
    override suspend fun uploadData(data: List<CompanySite>): List<CompanySite>? {
        return backendCompanySiteSource.uploadData(data)
    }
    
    suspend fun createCompanySite(companySite: CompanySite): CompanySite? {
        return backendCompanySiteSource.createCompanySite(companySite)
    }
    
    suspend fun deleteCompanySite(id: String): Boolean {
        return backendCompanySiteSource.deleteCompanySite(id)
    }
}
