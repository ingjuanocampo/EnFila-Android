package com.ingjuanocampo.enfila.domain.data.source.shifts

import com.ingjuanocampo.enfila.domain.data.source.RemoteSource
import com.ingjuanocampo.enfila.domain.entity.Shift
import kotlinx.coroutines.flow.Flow

interface ShiftRemoteSource: RemoteSource<List<Shift>> {
    suspend fun updateSingleData(shift: Shift): Flow<Shift?>

}