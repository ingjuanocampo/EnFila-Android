package com.ingjuanocampo.enfila.domain.data

import com.ingjuanocampo.enfila.domain.data.source.LocalSource
import com.ingjuanocampo.enfila.domain.data.source.RemoteSource
import com.ingjuanocampo.enfila.domain.data.util.RateLimiter
import com.ingjuanocampo.enfila.domain.usecases.repository.base.Repository
import com.ingjuanocampo.enfila.domain.util.EMPTY_STRING
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext
import java.util.*

open class RepositoryImp<Data>(
    private val remoteSource: RemoteSource<Data>,
    private val localSource: LocalSource<Data>
) : Repository<Data> {

    private val keyId = Date().time.toString()
    private val rateLimiter = RateLimiter<String>(15)

    override suspend fun createOrUpdate(data: Data) = withContext(Dispatchers.Default) {
        localSource.createOrUpdate(data)
        //remoteSource.createOrUpdate(data)
    }

    override suspend fun refresh(): Flow<Data?> {
        return remoteSource.fetchInfoFlow(id).map { data ->
            data?.let { localSource.createOrUpdate(it) }
            data
        }.flowOn(Dispatchers.Default)
    }

    private fun shouldFetch(initialData: Data?): Boolean {
        return if (initialData != null) {
            rateLimiter.shouldFetch(key = keyId)
        } else true
    }

    override suspend fun delete(dataToDelete: Data) = withContext(Dispatchers.Default) {
        localSource.delete(dataToDelete)
    }

    override fun getAllObserveData(): Flow<Data?> {
        return flow {
            val initialData = localSource.getAllData()
            if (initialData != null) {
                emit(initialData) // First value from Local
            }
            if (shouldFetch(initialData)) {
                remoteSource.fetchInfoFlow(id).flowOn(Dispatchers.Default).collect { dataToLocal ->
                    if (dataToLocal != null) {
                        kotlinx.coroutines.withContext(Dispatchers.Default) {
                            localSource.createOrUpdate(dataToLocal)
                        }
                    }
                    emit(dataToLocal) // TODO This might be not required.
                }
            }
            localSource.getAllObserveData().collect {
                emit(it)
            }
        }.flowOn(Dispatchers.Default)
    }

    override suspend fun getAllData(): Data? = withContext(Dispatchers.Default) {
        return@withContext localSource.getAllData()
    }

    override suspend fun getById(id: String): Data? = withContext(Dispatchers.Default) {
        return@withContext localSource.getById(id)
    }

    override suspend fun deleteById(id: String) = withContext(Dispatchers.Default) {
        return@withContext localSource.delete(id)
    }

    override var id: String = EMPTY_STRING

    override suspend fun createOrUpdateFlow(data: Data): Flow<Data?> {
        return remoteSource.createOrUpdateFlow(data).map {
            localSource.createOrUpdate(data)
            data
        }.flowOn(Dispatchers.Default)
    }
}