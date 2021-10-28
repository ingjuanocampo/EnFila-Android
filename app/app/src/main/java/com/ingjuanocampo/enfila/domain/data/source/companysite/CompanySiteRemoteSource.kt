package com.ingjuanocampo.enfila.domain.data.source.companysite

import com.ingjuanocampo.enfila.domain.data.source.RemoteSource
import com.ingjuanocampo.enfila.domain.entity.CompanySite
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull

class CompanySiteRemoteSource(private val companySiteLocalSource: CompanyInfoRemoteSource): RemoteSource<CompanySite> {

    override fun fetchInfoFlow(id: String): Flow<CompanySite?> {
        return companySiteLocalSource.fetchData(id)
    }

    override suspend fun createOrUpdate(data: CompanySite) {
        companySiteLocalSource.updateData(data = data).firstOrNull()
    }

    override suspend fun fetchData(id: String): CompanySite? {
        return fetchInfoFlow(id).firstOrNull()
    }

}

expect class CompanyInfoRemoteSource() {
    fun fetchData(id: String): Flow<CompanySite?>
    fun updateData(data: CompanySite): Flow<CompanySite?>

}