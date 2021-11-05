package com.ingjuanocampo.enfila.domain.usecases

import com.ingjuanocampo.enfila.domain.entity.CompanySite
import com.ingjuanocampo.enfila.domain.entity.Shift
import com.ingjuanocampo.enfila.domain.entity.ShiftState
import com.ingjuanocampo.enfila.domain.usecases.repository.base.Repository

class CompanySiteInteractions(
    private val shiftsRepo: Repository<List<Shift>>,
    private val companyRepo: Repository<List<CompanySite>>
) {

    /*private var cacheCompany: CompanySite? = null

    suspend fun load() =
        companyRepo.getById().map {
            cacheCompany = it.firstOrNull()
            cacheCompany?.apply {
                shifts = shiftsRepo.getData(GetShiftById(id)) as ArrayList<Shift>
            }
            cacheCompany
        }


    suspend fun next(): Shift? {
        return cacheCompany?.shifts?.let { shifts ->
            val current = getCurrentTurn()
            current?.state = ShiftState.FINISHED
            val possibleNextShift = cacheCompany?.shifts?.sorted()?.firstOrNull {
                it.state == ShiftState.WAITING
            }
            if (possibleNextShift?.state == ShiftState.WAITING) {
                possibleNextShift.state = ShiftState.CALLING
                return possibleNextShift
            } else null
        }
    }

    fun add(shift: Shift) {
        cacheCompany?.shifts?.add(shift)
    }

    fun getActiveShifts(): List<Shift>? {
        return cacheCompany?.shifts?.filter { it.state == ShiftState.WAITING }?.sorted()
    }

    fun getHistory(): List<Shift>? {
        return cacheCompany?.shifts?.filter { it.state != ShiftState.WAITING }?.sorted()
    }

    fun getCurrentTurn(): Shift? {
        return cacheCompany?.shifts?.firstOrNull { it.state == ShiftState.CALLING }
    }*/

}