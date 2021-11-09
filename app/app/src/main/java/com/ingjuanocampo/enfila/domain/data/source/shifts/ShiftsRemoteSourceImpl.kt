package com.ingjuanocampo.enfila.domain.data.source.shifts

import com.ingjuanocampo.enfila.data.source.shifts.ShiftsRemoteSourceFirebase
import com.ingjuanocampo.enfila.domain.entity.Shift
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.firstOrNull

internal class ShiftsRemoteSourceImpl(private val shiftsRemoteSourceFirebase: ShiftsRemoteSourceFirebase): ShiftRemoteSource {

    override suspend fun fetchDataAll(id: String): List<Shift>? {
        return shiftsRemoteSourceFirebase.fetchData(id).firstOrNull()
    }

    override suspend fun fetchData(id: String): Shift? {
        TODO("Not yet implemented")
    }

    override fun uploadData(data: List<Shift>): Flow<List<Shift>?> {
        return shiftsRemoteSourceFirebase.updateData(data)
    }

    override fun uploadData(data: Shift): Flow<Shift?> {
        TODO("Not yet implemented")
    }

    suspend fun updateSingleData(shift: Shift): Flow<Shift?> {
        return shiftsRemoteSourceFirebase.updateData(shift)
    }

}
