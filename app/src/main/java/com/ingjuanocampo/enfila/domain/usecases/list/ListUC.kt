package com.ingjuanocampo.enfila.domain.usecases.list

import com.ingjuanocampo.enfila.domain.entity.Shift
import com.ingjuanocampo.enfila.domain.entity.ShiftState
import com.ingjuanocampo.enfila.domain.usecases.ShiftInteractions
import com.ingjuanocampo.enfila.domain.usecases.repository.ClientRepository
import com.ingjuanocampo.enfila.domain.usecases.repository.ShiftRepository
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ListUC
    @Inject
    constructor(
        private val shiftRepository: ShiftRepository,
        private val shiftInteractions: ShiftInteractions,
        private val clientRepository: ClientRepository,
    ) {
        fun loadByClientId(clientId: String) =
            shiftRepository
                .getAllObserveData().map {
                    clientRepository.refresh()
                    it
                }.map { shifts ->
                    shifts!!.filter {
                        it.contactId == clientId
                    }.map {
                        shiftInteractions.loadShiftWithClient(it)
                    }.sortedBy { it.shift.number }
                }

        fun loadActiveShift() =
            shiftRepository
                .getAllObserveData().map {
                    clientRepository.refresh()
                    it
                }.map { shifts ->
                    shifts!!.filter {
                        it.isActive()
                    }.map {
                        shiftInteractions.loadShiftWithClient(it)
                    }.sortedBy { it.shift.number }
                }

        fun loadInactiveShift() =
            shiftRepository
                .getAllObserveData().map {
                    clientRepository.refresh()
                    it
                }.map { shifts ->
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
