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
    private val localSource: LocalSource<Data>
) : Repository<Data> {

    override var id: String = EMPTY_STRING

    private val keyId = Date().time.toString()
    private val rateLimiter = RateLimiter<String>(15)

    private val operation = object: RepositoryFlowOperation<Data, Data> {
        override fun subscribeDbUpdates(): Flow<Data?> {
            return localSource.getAllObserveData()
        }

        override fun shouldFetch(result: Data?): Boolean {
            return result != null || rateLimiter.shouldFetch(
                RepositoryImp::class.simpleName + id)
        }

        override suspend fun createCall(): Data {
            return remoteSource.fetchData(id)!!
        }

        override fun mapCallResult(result: Data): Data {
            return result
        }

        override suspend fun saveResult(result: Data) {
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

    override fun getAllObserveData(): Flow<Data?> {
        return operation.asFlow()
    }

    override suspend fun loadAllData(): Data? = withContext(Dispatchers.Default) {
        return@withContext localSource.getAllData()
    }

    override suspend fun loadById(id: String): Data? = withContext(Dispatchers.Default) {
        return@withContext localSource.getById(id)
    }

    override suspend fun deleteById(id: String) = withContext(Dispatchers.Default) {
        return@withContext localSource.delete(id)
    }



}