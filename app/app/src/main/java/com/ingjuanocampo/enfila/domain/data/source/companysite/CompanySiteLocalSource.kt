package com.ingjuanocampo.enfila.domain.data.source.companysite

import com.ingjuanocampo.enfila.domain.data.source.LocalSource
import com.ingjuanocampo.enfila.domain.entity.CompanySite
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

var companySite: CompanySite? = null

class CompanySiteLocalSource: LocalSource<CompanySite> {

    override suspend fun createOrUpdate(data: CompanySite) {
        companySite = data
    }

    override suspend fun delete(dataToDelete: CompanySite) {
        companySite = null
    }

    override fun getAllObserveData(): Flow<CompanySite?> {
        return flow { emit(companySite) }
    }

    override suspend fun getAllData(): CompanySite? {
        return companySite
    }

    override suspend fun getById(id: String): CompanySite? {
        return companySite
    }

    override suspend fun delete(id: String) {

    }

}