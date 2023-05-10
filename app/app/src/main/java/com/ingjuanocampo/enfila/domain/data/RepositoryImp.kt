package com.ingjuanocampo.enfila.domain.data

import com.ingjuanocampo.enfila.domain.data.source.LocalSource
import com.ingjuanocampo.enfila.domain.data.source.RemoteSource
import com.ingjuanocampo.enfila.domain.data.util.RateLimiter
import com.ingjuanocampo.enfila.domain.usecases.repository.base.Repository
import com.ingjuanocampo.enfila.domain.usecases.repository.base.RepositoryFlowOperation
import com.ingjuanocampo.enfila.domain.util.EMPTY_STRING
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext
import java.util.*

open class RepositoryImp<Data>(
    private val remoteSource: RemoteSource<Data>,
    private val localSource: LocalSource<Data>,
) : Repository<Data> {

    override var id: String = EMPTY_STRING

    private val rateLimiter = RateLimiter<String>(60)

    private val operation = object : RepositoryFlowOperation<List<Data>, List<Data>> {
        override fun subscribeDbUpdates(): Flow<List<Data>?> {
            return localSource.getAllObserveData()
        }

        override fun shouldFetch(result: List<Data>?): Boolean {
            return result.isNullOrEmpty() || rateLimiter.shouldFetch(
                RepositoryImp::class.simpleName + id,
            )
        }

        override suspend fun createCall(): List<Data> {
            return remoteSource.fetchDataAll(id)!!
        }

        override fun mapCallResult(result: List<Data>): List<Data> {
            return result
        }

        override suspend fun saveResult(result: List<Data>) {
            localSource.createOrUpdate(result)
        }
    }

    override suspend fun createOrUpdate(data: Data) = withContext(Dispatchers.Default) {
        localSource.createOrUpdate(data)
    }

    override suspend fun refresh() {
        operation.refresh()
    }

    override suspend fun delete(listOf: Data) = withContext(Dispatchers.Default) {
        localSource.delete(listOf)
    }

    override fun getAllObserveData(): Flow<List<Data>?> {
        return operation.asFlow()
    }

    override suspend fun loadAllData(): List<Data>? = withContext(Dispatchers.Default) {
        return@withContext localSource.getAllData()
    }

    override suspend fun loadById(id: String): Data? = withContext(Dispatchers.Default) {
        return@withContext localSource.getById(id)
    }

    override suspend fun deleteById(id: String) = withContext(Dispatchers.Default) {
        return@withContext localSource.delete(id)
    }

    override suspend fun deleteAll() {
        localSource.deleteAll()
    }

    override fun updateData(data: Data): Flow<Data?> {
        return remoteSource.uploadData(data).map {
            localSource.createOrUpdate(data)
            it
        }
    }

    override suspend fun createOrUpdate(data: List<Data>) {
        localSource.createOrUpdate(data)
    }

    override suspend fun getById(id: String): Data? {
        val localValue = localSource.getById(id)

        return if (localValue == null) {
            val remoteVal = remoteSource.fetchData(id)
            remoteVal?.let {
                localSource.createOrUpdate(it)
            }
            remoteVal
        } else {
            localValue
        }
    }
}
