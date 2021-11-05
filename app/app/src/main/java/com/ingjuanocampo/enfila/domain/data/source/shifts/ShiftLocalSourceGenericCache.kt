package com.ingjuanocampo.enfila.domain.data.source.shifts

import com.ingjuanocampo.enfila.domain.data.source.template.GenericCache
import com.ingjuanocampo.enfila.domain.entity.Shift
import com.ingjuanocampo.enfila.domain.entity.ShiftState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ShiftLocalSourceGenericCache: ShiftLocalSource {

    val genericCache = GenericCache<Shift>()


    override suspend fun  getClosestShift(): Shift? {
        return getAllData()?.firstOrNull{ it.state == ShiftState.WAITING }
    }

    override suspend fun getLastShift(): Shift? {
        return getAllData()?.lastOrNull()
    }

    override suspend fun getCallingShift(): Shift? {
        return getAllData()?.firstOrNull { it.state == ShiftState.CALLING }

    }


    override suspend fun createOrUpdate(data: List<Shift>) {
        genericCache.save(data)
    }

    override suspend fun delete(dataToDelete: List<Shift>) {
        genericCache.deleteAll()
    }

    override suspend fun delete(id: String) {
        genericCache.deleteById(id)
    }

    override fun getAllObserveData(): Flow<List<Shift>?> {
       return  genericCache.observeData()
    }

    override suspend fun getAllData(): List<Shift>? {
        return genericCache.getData()
    }

    override suspend fun getById(id: String): List<Shift>? {
        return genericCache.getData().filter { it.id == id  }
    }
}