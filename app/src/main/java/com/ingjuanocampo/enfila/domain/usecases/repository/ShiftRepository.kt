package com.ingjuanocampo.enfila.domain.usecases.repository

import com.ingjuanocampo.enfila.domain.entity.Shift
import com.ingjuanocampo.enfila.domain.usecases.repository.base.Repository

interface ShiftRepository : Repository<Shift> {
    suspend fun getClosestShift(): Shift?

    suspend fun getLastShift(): Shift?

    suspend fun getCallingShift(): Shift?
}
