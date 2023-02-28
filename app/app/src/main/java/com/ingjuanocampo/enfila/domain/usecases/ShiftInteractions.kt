package com.ingjuanocampo.enfila.domain.usecases

import com.enfila.data.messaging.MessageRepository
import com.ingjuanocampo.enfila.domain.entity.*
import com.ingjuanocampo.enfila.domain.usecases.model.ShiftWithClient
import com.ingjuanocampo.enfila.domain.usecases.repository.ClientRepository
import com.ingjuanocampo.enfila.domain.usecases.repository.ShiftRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ShiftInteractions @Inject constructor(
    private val shiftRepository: ShiftRepository,
    private val clientRepository: ClientRepository,
    private val messagingRepository: MessageRepository,
) {

    fun active(current: Shift?): Flow<Boolean> {
        return updateShiftTo(current, ShiftState.CALLING).map {
            messagingRepository.sendMessage("573137550993", "14155238886", "Es tu turno ahora, acercate por favor")
            true
        }
    }

    private fun updateShiftTo(shift: Shift?, state: ShiftState): Flow<Boolean> {
        return flowOf(shift?.copy()).flatMapLatest { current ->
            current?.let {
                updateShift(
                    it.apply {
                        endDate = getNow()
                    },
                    state,
                ).map { true }
            } ?: flowOf(false)
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

    private fun updateShift(shift: Shift, state: ShiftState): Flow<Shift?> {
        shift.state = state
        return shiftRepository.updateData(shift)
    }

    suspend fun loadShiftWithClient(shift: Shift): ShiftWithClient {
        val client = clientRepository.loadById(shift.contactId)
        return ShiftWithClient(shift, client ?: defaultClient)
    }

    fun addNewTurn(tunr: Int, phoneNumber: String, name: String?, note: String?): Flow<Shift?> {
        val client = Client(id = phoneNumber, name = name)
        return clientRepository.updateData(client).flatMapLatest {
            shiftRepository.updateData(
                (
                    ShiftFactory.createWaiting(
                        tunr,
                        client.id,
                        note ?: "",
                        shiftRepository.id,
                    )
                    ),
            )
        }.map {
            messagingRepository.sendMessage("573137550993", "14155238886", "Hola $name Fuiste anadido al turno $tunr")
            it
        }
    }

    fun cancel(shiftToCancel: Shift?): Flow<Boolean> {
        return updateShiftTo(shiftToCancel, ShiftState.CANCELLED)
    }

    fun finish(shiftToFinish: Shift?): Flow<Boolean> {
        shiftToFinish?.endDate = getNow()
        return updateShiftTo(shiftToFinish, ShiftState.FINISHED)
    }
}
