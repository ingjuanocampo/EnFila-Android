package com.ingjuanocampo.enfila.domain.usecases.repository.base

import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.*

interface RepositoryFlowOperation<ResultType, RequestType> {
    suspend fun fetchAndUpdateInformationIfNeeded(initData: ResultType?) {
        if (shouldFetch(initData)) {
            refresh()
        }
    }

    fun asFlow(): Flow<ResultType?> =
        flow {
            val initData = getInitFromDb()
            if (isValidInitData(initData)) {
                initData?.let { emit(it) }
            }
            fetchAndUpdateInformationIfNeeded(initData)
            subscribeDbUpdates().collect {
                emit(it)
            }
        }.distinctUntilChanged()

    suspend fun refresh() {
        val remoteData = createCall()
        saveResult(mapCallResult(remoteData))
    }

    @WorkerThread
    fun subscribeDbUpdates(): Flow<ResultType?>

    @WorkerThread
    fun shouldFetch(result: ResultType?): Boolean

    @WorkerThread
    suspend fun createCall(): RequestType

    @WorkerThread
    fun mapCallResult(result: RequestType): ResultType

    @WorkerThread
    suspend fun saveResult(result: ResultType)

    @WorkerThread
    suspend fun getInitFromDb(): ResultType? {
        return subscribeDbUpdates().firstOrNull()
    }

    fun isValidInitData(initData: ResultType?): Boolean {
        return initData != null && (initData as? List<*>)?.isNotEmpty() ?: true
    }
}
