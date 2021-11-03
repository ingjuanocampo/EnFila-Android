package com.ingjuanocampo.enfila.domain.usecases.repository.base

import kotlinx.coroutines.flow.Flow

interface Repository<Data> {

    var id: String

    suspend fun createOrUpdate(data: Data)

    suspend fun refresh()

    fun getAllObserveData(): Flow<Data?>

    suspend fun loadAllData(): Data?

    suspend fun loadById(id: String): Data?

    suspend fun delete(listOf: Data)

    suspend fun deleteById(id: String)

}

