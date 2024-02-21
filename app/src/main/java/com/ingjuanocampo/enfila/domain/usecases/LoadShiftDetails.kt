package com.ingjuanocampo.enfila.domain.usecases

import com.ingjuanocampo.enfila.domain.usecases.model.ShiftWithClient
import com.ingjuanocampo.enfila.domain.usecases.repository.ShiftRepository
import javax.inject.Inject

class LoadShiftDetails @Inject constructor(
    private val shiftInteractions: ShiftInteractions,
    private val shiftRepository: ShiftRepository,
    ) {

    suspend operator fun invoke(shiftId: String): ShiftWithClient {
        val shift = shiftRepository.loadById(shiftId)
        return shiftInteractions.loadShiftWithClient(shift!!)
    }
}