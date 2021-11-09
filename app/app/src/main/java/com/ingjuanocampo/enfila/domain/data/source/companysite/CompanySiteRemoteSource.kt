package com.ingjuanocampo.enfila.domain.data.source.companysite

import com.ingjuanocampo.enfila.data.source.companysite.CompanyInfoRemoteSource
import com.ingjuanocampo.enfila.domain.data.source.RemoteSource
import com.ingjuanocampo.enfila.domain.entity.CompanySite
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull

class CompanySiteRemoteSource(private val companySiteLocalSource: CompanyInfoRemoteSource): RemoteSource<CompanySite> {

    override fun uploadData(data: CompanySite): Flow<CompanySite?> {
        return companySiteLocalSource.updateData(data = data)
    }

    override suspend fun fetchDataAll(id: String): List<CompanySite>? {
        return  listOf(companySiteLocalSource.fetchData(id).firstOrNull()!!)
    }

    override fun uploadData(data: List<CompanySite>): Flow<List<CompanySite>?> {
        TODO("Not yet implemented")
    }

    override suspend fun fetchData(id: String): CompanySite? {
        return fetchDataAll(id)?.firstOrNull()
    }

}