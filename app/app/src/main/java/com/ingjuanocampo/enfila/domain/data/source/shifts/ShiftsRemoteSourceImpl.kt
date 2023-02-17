package com.ingjuanocampo.enfila.domain.data.source.shifts

import com.ingjuanocampo.enfila.data.source.shifts.ShiftsRemoteSourceFirebase
import com.ingjuanocampo.enfila.domain.entity.Shift
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

class ShiftsRemoteSourceImpl @Inject constructor(private val shiftsRemoteSourceFirebase: ShiftsRemoteSourceFirebase): ShiftRemoteSource {


    override suspend fun fetchDataAll(id: String): List<Shift>? {
        return shiftsRemoteSourceFirebase.fetchShiftsByCompanyId(id).firstOrNull()
    }

    override suspend fun fetchData(id: String): Shift? {
        val ids = id.split(",")
        return shiftsRemoteSourceFirebase.fetchByShiftId(ids.firstOrNull()!!, ids.lastOrNull()!!).firstOrNull()
    }

    override fun uploadData(data: List<Shift>): Flow<List<Shift>?> {
        return shiftsRemoteSourceFirebase.updateData(data)
    }

    override fun uploadData(data: Shift): Flow<Shift?> {
        return shiftsRemoteSourceFirebase.updateData(data)
    }

}
