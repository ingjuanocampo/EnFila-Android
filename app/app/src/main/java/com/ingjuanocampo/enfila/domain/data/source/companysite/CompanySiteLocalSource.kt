package com.ingjuanocampo.enfila.domain.data.source.companysite

import com.ingjuanocampo.enfila.domain.data.source.LocalSource
import com.ingjuanocampo.enfila.domain.entity.CompanySite
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

var companySite: CompanySite? = null

class CompanySiteLocalSource: LocalSource<CompanySite> {

    override suspend fun createOrUpdate(data: CompanySite) {
        companySite = data
    }

    override suspend fun delete(dataToDelete: CompanySite) {
        companySite = null
    }

    override fun getAllObserveData(): Flow<List<CompanySite>?> {
        return companySite?.let {
            flowOf(listOf(it))
        } ?: flowOf(null)
    }

    override suspend fun getAllData(): List<CompanySite>? {
        return (companySite.let { listOf(it) }?: listOf<CompanySite>()) as List<CompanySite>?
    }

    override suspend fun getById(id: String): CompanySite? {
        return companySite
    }

    override suspend fun delete(id: String) {

    }

    override suspend fun createOrUpdate(data: List<CompanySite>) {
        companySite = data.firstOrNull()
    }

}