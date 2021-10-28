package com.ingjuanocampo.enfila.domain.usecases

import com.ingjuanocampo.enfila.domain.entity.*
import com.ingjuanocampo.enfila.domain.usecases.model.ShiftWithClient
import com.ingjuanocampo.enfila.domain.usecases.repository.ShiftRepository
import com.ingjuanocampo.enfila.domain.usecases.repository.base.Repository
import kotlinx.coroutines.flow.Flow

class ShiftInteractions(
    private val shiftRepository: ShiftRepository
    , private val clientRepository: Repository<List<Client>>) {

    suspend fun next(current: Shift?): ShiftWithClient? {

        current?.let { updateShift(it.apply {
            endDate = getNow()
        }, ShiftState.FINISHED) }
        val closestShift = shiftRepository.getClosestShift()
        closestShift?.state = ShiftState.CALLING

        closestShift?.let {
            shiftRepository.createOrUpdate(listOf(it))
        }
        return closestShift?.let {
            ShiftWithClient(
                it,
                clientRepository.getById(it.contactId)?.firstOrNull()!!
            )
        }
    }
    
    suspend fun getClosestNewShiftTurn(): Int {
       val lastTurn =  shiftRepository.getLastShift()?.number
        return if (lastTurn == null) {
            1
        } else {
           lastTurn + 1
        }
    }

    suspend fun updateShift(shift: Shift, state: ShiftState) {
        shift.state = state
        shiftRepository.createOrUpdate(listOf(shift))
    }

    suspend fun loadShiftWithClient(shift: Shift): ShiftWithClient {
        val client = clientRepository.getById(shift.contactId)?.firstOrNull()
        return ShiftWithClient(shift, client!!)
    }

    suspend fun addNewTurn(tunr: Int, phoneNumber: String, name: String?, note: String?): Flow<List<Shift>?> {
        val client = Client(id = phoneNumber, name = name)
        clientRepository.createOrUpdate(listOf(client))
        return shiftRepository.createOrUpdateFlow(listOf(ShiftFactory.createWaiting(tunr, client.id, note?: "", shiftRepository.id)))
    }
}