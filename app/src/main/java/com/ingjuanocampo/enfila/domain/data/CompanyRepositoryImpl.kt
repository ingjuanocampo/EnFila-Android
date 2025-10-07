package com.ingjuanocampo.enfila.domain.data

import com.ingjuanocampo.enfila.data.source.companysite.CompanySiteRemoteSourceBackend
import com.ingjuanocampo.enfila.domain.data.source.companysite.CompanySiteLocalSource
import com.ingjuanocampo.enfila.domain.data.source.companysite.CompanySiteRemoteSource
import com.ingjuanocampo.enfila.domain.entity.CompanySite
import com.ingjuanocampo.enfila.domain.usecases.repository.CompanyRepository

class CompanyRepositoryImpl(
    val remoteSource: CompanySiteRemoteSource, 
    val localSource: CompanySiteLocalSource,
    private val companySiteRemoteSourceBackend: CompanySiteRemoteSourceBackend? = null, // Optional for backend operations
) : CompanyRepository, RepositoryImp<CompanySite>(remoteSource, localSource) {
    
    override suspend fun createCompanySite(companySite: CompanySite): CompanySite? {
        return companySiteRemoteSourceBackend?.createCompanySite(companySite)?.also { createdCompanySite ->
            // Cache the created company site locally
            localSource.createOrUpdate(createdCompanySite)
        }
    }
}
