package com.ingjuanocampo.enfila.domain.data.source.shifts

import com.ingjuanocampo.enfila.domain.data.source.LocalSource
import com.ingjuanocampo.enfila.domain.entity.Shift
import kotlinx.coroutines.flow.Flow

interface ShiftLocalSource: LocalSource<List<Shift>> {
    suspend fun getClosestShift(): Shift?

    suspend fun getLastShift(): Shift?

    suspend fun getCallingShift(): Shift?
}