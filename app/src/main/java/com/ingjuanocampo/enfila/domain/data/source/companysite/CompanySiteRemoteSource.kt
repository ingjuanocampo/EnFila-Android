package com.ingjuanocampo.enfila.domain.data.source.companysite

import com.ingjuanocampo.enfila.data.source.companysite.CompanyInfoRemoteSource
import com.ingjuanocampo.enfila.domain.data.source.RemoteSource
import com.ingjuanocampo.enfila.domain.entity.CompanySite
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

class CompanySiteRemoteSource
    @Inject
    constructor(private val companySiteLocalSource: CompanyInfoRemoteSource) : RemoteSource<CompanySite> {
        override suspend fun uploadData(data: CompanySite): CompanySite? {
            return companySiteLocalSource.updateData(data = data).firstOrNull()
        }

        override suspend fun fetchDataAll(id: String): List<CompanySite>? {
            return companySiteLocalSource.fetchData(id).firstOrNull()?.let {
                listOf(it)
            }
        }

        override suspend fun uploadData(data: List<CompanySite>): List<CompanySite>? {
            TODO("Not yet implemented")
        }

        override suspend fun fetchData(id: String): CompanySite? {
            return fetchDataAll(id)?.firstOrNull()
        }
    }
