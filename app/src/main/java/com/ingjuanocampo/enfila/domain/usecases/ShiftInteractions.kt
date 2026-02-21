package com.ingjuanocampo.enfila.domain.usecases

import com.enfila.data.messaging.MessageRepository
import com.ingjuanocampo.enfila.data.backend.source.BackendMessageSource
import com.ingjuanocampo.enfila.domain.entity.Client
import com.ingjuanocampo.enfila.domain.entity.Shift
import com.ingjuanocampo.enfila.domain.entity.ShiftFactory
import com.ingjuanocampo.enfila.domain.entity.ShiftState
import com.ingjuanocampo.enfila.domain.entity.defaultClient
import com.ingjuanocampo.enfila.domain.entity.getNow
import com.ingjuanocampo.enfila.domain.usecases.model.ShiftWithClient
import com.ingjuanocampo.enfila.domain.usecases.repository.ClientRepository
import com.ingjuanocampo.enfila.domain.usecases.repository.ShiftRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ShiftInteractions
@Inject
constructor(
    private val shiftRepository: ShiftRepository,
    private val clientRepository: ClientRepository,
    private val messagingRepository: BackendMessageSource,
) {
    fun active(current: Shift?): Flow<Boolean> {
        return updateShiftTo(
            current?.apply {
                this.attentionStartDate = getNow()
            },
            ShiftState.CALLING,
        ).map {
            messagingRepository.sendMessage("573137550993", "14155238886", "Es tu turno ahora, acercate por favor")
            true
        }
    }

    private fun updateShiftTo(
        shift: Shift?,
        state: ShiftState,
    ): Flow<Boolean> {
        return flow {
            shift?.copy()?.let {
                updateShift(
                    it,
                    state,
                )
                true
            } ?: false
        }
    }

    suspend fun getClosestNewShiftTurn(): Int {
        val lastTurn = shiftRepository.loadAllData()?.lastOrNull()?.number
        return if (lastTurn == null) {
            1
        } else {
            lastTurn + 1
        }
    }

    private suspend fun updateShift(
        shift: Shift,
        state: ShiftState,
    ): Shift? {
        shift.state = state
        return shiftRepository.updateData(shift)
    }

    suspend fun loadShiftWithClient(shift: Shift): ShiftWithClient {
        val client = clientRepository.loadById(shift.contactId)
        return ShiftWithClient(shift, client ?: defaultClient)
    }

    fun addNewTurn(
        tunr: Int,
        client: Client,
        note: String?,
    ): Flow<Shift?> {
        return flow {
            // First, check if client exists locally
            val existingClient = clientRepository.getById(client.id)

            val savedClient = if (existingClient != null) {
                // Client exists, update it
                clientRepository.updateData(client)
            } else {
                // Client doesn't exist, create it
                clientRepository.createClient(client)
            }

            if (savedClient != null) {
                // Create the shift
                val newShift = ShiftFactory.createWaiting(
                    tunr,
                    client.id,
                    note ?: "",
                    shiftRepository.id,
                )

                // Since the shift is new, always create it
                val createdShift = shiftRepository.createShift(newShift)
                if (createdShift != null) {
                    messagingRepository.sendMessage("573137550993", "14155238886", "Hola ${client.name} Fuiste anadido al turno $tunr")
                    emit(createdShift)
                } else {
                    emit(null)
                }
            } else {
                emit(null)
            }
        }
    }

    fun cancel(shiftToCancel: Shift?): Flow<Boolean> {
        shiftToCancel?.endDate = getNow()
        return updateShiftTo(shiftToCancel, ShiftState.CANCELLED)
    }

    fun finish(shiftToFinish: Shift?): Flow<Boolean> {
        shiftToFinish?.endDate = getNow()
        return updateShiftTo(shiftToFinish, ShiftState.FINISHED)
    }
}
