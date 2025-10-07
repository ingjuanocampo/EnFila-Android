package com.ingjuanocampo.enfila.data.source.companysite

import com.ingjuanocampo.enfila.data.backend.source.BackendCompanySiteSource
import com.ingjuanocampo.enfila.domain.data.source.RemoteSource
import com.ingjuanocampo.enfila.domain.entity.CompanySite
import kotlinx.coroutines.flow.Flow
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
    
    override fun uploadData(data: CompanySite): Flow<CompanySite?> {
        return backendCompanySiteSource.uploadData(data)
    }
    
    override fun uploadData(data: List<CompanySite>): Flow<List<CompanySite>?> {
        return backendCompanySiteSource.uploadData(data)
    }
    
    suspend fun createCompanySite(companySite: CompanySite): CompanySite? {
        return backendCompanySiteSource.createCompanySite(companySite)
    }
    
    suspend fun deleteCompanySite(id: String): Boolean {
        return backendCompanySiteSource.deleteCompanySite(id)
    }
}
