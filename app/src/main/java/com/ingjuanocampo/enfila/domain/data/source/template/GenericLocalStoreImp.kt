package com.ingjuanocampo.enfila.domain.data.source.template

import com.ingjuanocampo.enfila.domain.data.source.LocalSource
import com.ingjuanocampo.enfila.domain.entity.IdentifyObject
import kotlinx.coroutines.flow.Flow

open class GenericLocalStoreImp<Data : IdentifyObject> : LocalSource<Data> {
    protected val genericCache = GenericCache<Data>()

    override suspend fun createOrUpdate(data: Data) {
        genericCache.save(data)
    }

    override suspend fun createOrUpdate(data: List<Data>) {
        genericCache.save(data)
    }

    override suspend fun delete(dataToDelete: Data) {
        genericCache.deleteAll()
    }

    override suspend fun delete(id: String) {
        genericCache.deleteById(id)
    }

    override fun getAllObserveData(): Flow<List<Data>?> {
        return genericCache.observeData()
    }

    override suspend fun getAllData(): List<Data>? {
        return genericCache.getData()
    }

    override suspend fun deleteAll() {
        genericCache.deleteAll()
    }

    override suspend fun getById(id: String): Data? {
        return genericCache.getData().firstOrNull { it.id == id }
    }
}
