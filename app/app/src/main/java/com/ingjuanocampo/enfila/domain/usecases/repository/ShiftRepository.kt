package com.ingjuanocampo.enfila.domain.usecases.repository

import com.ingjuanocampo.enfila.domain.usecases.repository.base.Repository
import com.ingjuanocampo.enfila.domain.entity.Shift
import kotlinx.coroutines.flow.Flow

interface ShiftRepository: Repository<Shift> {

    suspend fun getClosestShift(): Shift?

    suspend fun getLastShift(): Shift?

    suspend fun getCallingShift(): Shift?
}