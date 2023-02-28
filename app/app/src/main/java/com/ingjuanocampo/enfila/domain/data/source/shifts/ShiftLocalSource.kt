package com.ingjuanocampo.enfila.domain.data.source.shifts

import com.ingjuanocampo.enfila.domain.data.source.LocalSource
import com.ingjuanocampo.enfila.domain.entity.Shift

interface ShiftLocalSource : LocalSource<Shift> {
    suspend fun getClosestShift(): Shift?

    suspend fun getLastShift(): Shift?

    suspend fun getCallingShift(): Shift?
}
