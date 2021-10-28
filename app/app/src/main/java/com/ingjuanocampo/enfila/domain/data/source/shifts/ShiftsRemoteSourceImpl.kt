package com.ingjuanocampo.enfila.domain.data.source.shifts

import com.ingjuanocampo.enfila.domain.entity.Shift
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.firstOrNull

internal class ShiftsRemoteSourceImpl(val shiftsRemoteSourceFirebase: ShiftsRemoteSourceFirebase): ShiftRemoteSource {

    override suspend fun fetchData(id: String): List<Shift>? {
        return shiftsRemoteSourceFirebase.fetchData(id).firstOrNull()
    }

    override suspend fun createOrUpdate(data: List<Shift>) {
        shiftsRemoteSourceFirebase.updateData(data).collect {  }
    }

    override suspend fun updateSingleData(shift: Shift): Flow<Shift?> {
        return shiftsRemoteSourceFirebase.updateData(shift)
    }
    override fun fetchInfoFlow(id: String): Flow<List<Shift>?> {
        return shiftsRemoteSourceFirebase.fetchData(id)
    }

    override fun createOrUpdateFlow(data: List<Shift>): Flow<List<Shift>?> {
        return shiftsRemoteSourceFirebase.updateData(data)
    }

}

expect class ShiftsRemoteSourceFirebase() {
    fun fetchData(id: String): Flow<List<Shift>?>
    fun updateData(data: List<Shift>): Flow<List<Shift>?>
    fun updateData(data: Shift): Flow<Shift?>

}