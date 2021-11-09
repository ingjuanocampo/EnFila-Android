package com.ingjuanocampo.enfila.domain.data.source.shifts

import com.ingjuanocampo.enfila.domain.data.source.template.GenericLocalStoreImp
import com.ingjuanocampo.enfila.domain.entity.Shift
import com.ingjuanocampo.enfila.domain.entity.ShiftState

class ShiftLocalSourceGenericCache: ShiftLocalSource, GenericLocalStoreImp<Shift>() {

    override suspend fun  getClosestShift(): Shift? {
        return getAllData()?.firstOrNull{ it.state == ShiftState.WAITING }
    }

    override suspend fun getLastShift(): Shift? {
        return getAllData()?.lastOrNull()
    }

    override suspend fun getCallingShift(): Shift? {
        return getAllData()?.firstOrNull { it.state == ShiftState.CALLING }

    }
}