package com.ingjuanocampo.enfila.domain.usecases

import com.ingjuanocampo.enfila.domain.entity.*
import com.ingjuanocampo.enfila.domain.usecases.model.ShiftWithClient
import com.ingjuanocampo.enfila.domain.usecases.repository.ShiftRepository
import com.ingjuanocampo.enfila.domain.usecases.repository.base.Repository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

class ShiftInteractions(
    private val shiftRepository: ShiftRepository
    , private val clientRepository: Repository<List<Client>>) {

    fun next(current: Shift?): Flow<ShiftWithClient?> {
        return flowOf(current).flatMapLatest { current ->
            current?.let {
                updateShift(it.apply {
                    endDate = getNow()
                }, ShiftState.FINISHED)
            } ?: flowOf(null)
        }.map {
            val closestShift = shiftRepository.loadAllData()?.sortedBy { it.number }?.firstOrNull { it.state == ShiftState.WAITING }
            closestShift?.state = ShiftState.CALLING

            closestShift
        }.flatMapLatest { closestShift ->
            closestShift?.let {
                shiftRepository.updateData(listOf(it)).map { closestShift }
            } ?: flowOf(null)
        }.map { closestShift ->
            closestShift?.let {
                ShiftWithClient(
                    it,
                    clientRepository.loadById(it.contactId)?.firstOrNull()!!
                )
            }
        }
    }
    
    suspend fun getClosestNewShiftTurn(): Int {
       val lastTurn =  shiftRepository.loadAllData()?.lastOrNull()?.number
        return if (lastTurn == null) {
            1
        } else {
           lastTurn + 1
        }
    }

     private fun updateShift(shift: Shift, state: ShiftState): Flow<List<Shift>?> {
        shift.state = state
        return shiftRepository.updateData(listOf(shift))
    }

    suspend fun loadShiftWithClient(shift: Shift): ShiftWithClient {
        val client = clientRepository.loadById(shift.contactId)?.firstOrNull()
        return ShiftWithClient(shift, client!!)
    }

    fun addNewTurn(tunr: Int, phoneNumber: String, name: String?, note: String?): Flow<List<Shift>?> {
        val client = Client(id = phoneNumber, name = name)
        return clientRepository.updateData(listOf(client)).flatMapLatest {
            shiftRepository.updateData(listOf(ShiftFactory.createWaiting(tunr, client.id, note?: "", shiftRepository.id)))
        }
    }
}