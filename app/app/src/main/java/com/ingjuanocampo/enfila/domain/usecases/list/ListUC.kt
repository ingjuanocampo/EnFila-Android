package com.ingjuanocampo.enfila.domain.usecases.list

import com.ingjuanocampo.enfila.domain.entity.Shift
import com.ingjuanocampo.enfila.domain.entity.ShiftState
import com.ingjuanocampo.enfila.domain.usecases.ShiftInteractions
import com.ingjuanocampo.enfila.domain.usecases.repository.ShiftRepository
import kotlinx.coroutines.flow.map

class ListUC(
    private val shiftRepository: ShiftRepository,
    private val shiftInteractions: ShiftInteractions
) {

    fun loadActiveShift() = shiftRepository
        .getAllObserveData().map { shifts ->
            shifts!!.filter {
                it.isActive()
            }.map {
                shiftInteractions.loadShiftWithClient(it)
            }
        }


    fun loadInactiveShift() = shiftRepository
        .getAllObserveData().map { shifts ->
            shifts!!.filter {
                it.state != ShiftState.WAITING && it.state != ShiftState.CALLING
            }.map {
                shiftInteractions.loadShiftWithClient(it)
            }
        }


}

fun Shift.isActive(): Boolean {
    return this.state == ShiftState.WAITING || this.state == ShiftState.CALLING
}
