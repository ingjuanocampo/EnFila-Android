package com.ingjuanocampo.enfila.domain.usecases

import com.ingjuanocampo.enfila.domain.usecases.repository.ShiftRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow

class FinishShiftUC(private val shiftRepository: ShiftRepository,
                    private val shiftInteractions: ShiftInteractions) {


    fun invoke(id: String): Flow<Boolean> {
        return flow { emit(shiftRepository.loadById(id))}.flatMapLatest { shiftInteractions.finish(it) }
    }
}