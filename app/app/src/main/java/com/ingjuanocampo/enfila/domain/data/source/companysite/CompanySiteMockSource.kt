package com.ingjuanocampo.enfila.domain.data.source.companysite

import com.ingjuanocampo.enfila.domain.data.source.LocalSource
import com.ingjuanocampo.enfila.domain.entity.CompanySite
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

private val listOfMockCompanies = arrayListOf(CompanySite(
    id = "companyid",
    name = "Sanduches Cuba"
))

class CompanySiteMockSource: LocalSource<List<CompanySite>> {

    override suspend fun createOrUpdate(data: List<CompanySite>) {
        listOfMockCompanies.addAll(data)
    }


    override suspend fun delete(dataToDelete: List<CompanySite>) {
    }

    override fun getAllObserveData(): Flow<List<CompanySite>> {
        return flow { emit(listOfMockCompanies) }
    }

    override suspend fun getAllData(): List<CompanySite> {
        return listOfMockCompanies
    }

    override suspend fun getById(id: String): List<CompanySite> {
        return listOfMockCompanies.filter { it.id == id }
    }

    override suspend fun delete(id: String) {
        TODO("Not yet implemented")
    }
}