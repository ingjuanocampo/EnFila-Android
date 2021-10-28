package com.ingjuanocampo.enfila.domain.usecases.repository.base

import kotlinx.coroutines.flow.Flow

interface Repository<Data> {

    var id: String

    suspend fun createOrUpdate(data: Data)

    suspend fun createOrUpdateFlow(data: Data): Flow<Data?>

    suspend fun refresh(): Flow<Data?>

    fun getAllObserveData(): Flow<Data?>

    suspend fun getAllData(): Data?

    suspend fun getById(id: String): Data?

    suspend fun delete(listOf: Data)

    suspend fun deleteById(id: String)

}

