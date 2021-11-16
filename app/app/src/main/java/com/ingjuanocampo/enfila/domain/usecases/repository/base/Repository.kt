package com.ingjuanocampo.enfila.domain.usecases.repository.base

import kotlinx.coroutines.flow.Flow

interface Repository<Data> {

    var id: String

    fun updateData(data: Data): Flow<Data?>

    suspend fun createOrUpdate(data: Data)

    suspend fun createOrUpdate(data: List<Data>)

    suspend fun refresh()

    fun getAllObserveData(): Flow<List<Data>?>

    suspend fun loadAllData(): List<Data>?

    suspend fun loadById(id: String): Data?

    suspend fun getById(id: String): Data?

    suspend fun delete(listOf: Data)

    suspend fun deleteById(id: String)

}

