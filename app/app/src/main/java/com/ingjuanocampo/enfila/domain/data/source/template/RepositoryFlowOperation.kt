package com.ingjuanocampo.enfila.domain.data.source.template

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow

interface RepositoryFlowOperation<ResultType, RequestType> {

    suspend fun fetchAndUpdateInformationIfNeeded(initData: ResultType?) {
        if (shouldFetch(initData)) {
            refresh()
        }
    }

    fun asFlow(): Flow<ResultType> = flow {
        val initData = getInitFromDb()
        if (isValidInitData(initData)) {
            initData?.let { emit(it) }
        }
        fetchAndUpdateInformationIfNeeded(initData)
        subscribeDbUpdates().collect {
            emit(it)
        }
    }

    suspend fun refresh() {
        val remoteData = createCall()
        saveResult(mapCallResult(remoteData))
    }

    fun subscribeDbUpdates(): Flow<ResultType>

    fun shouldFetch(result: ResultType?): Boolean

    suspend fun createCall(): RequestType

    fun mapCallResult(result: RequestType): ResultType

    suspend fun saveResult(result: ResultType)

    suspend fun getInitFromDb(): ResultType? {
        return subscribeDbUpdates().firstOrNull()
    }

    fun isValidInitData(initData: ResultType?): Boolean {
        return initData != null && (initData as? List<*>)?.isNotEmpty() ?: true
    }
}