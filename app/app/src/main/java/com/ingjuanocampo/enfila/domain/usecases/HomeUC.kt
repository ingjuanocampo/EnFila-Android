package com.ingjuanocampo.enfila.domain.usecases

import com.ingjuanocampo.enfila.domain.entity.CompanySite
import com.ingjuanocampo.enfila.domain.entity.Client
import com.ingjuanocampo.enfila.domain.entity.Shift
import com.ingjuanocampo.enfila.domain.entity.ShiftState
import com.ingjuanocampo.enfila.domain.usecases.list.isActive
import com.ingjuanocampo.enfila.domain.usecases.model.Home
import com.ingjuanocampo.enfila.domain.usecases.model.ShiftWithClient
import com.ingjuanocampo.enfila.domain.usecases.repository.CompanyRepository
import com.ingjuanocampo.enfila.domain.usecases.repository.ShiftRepository
import com.ingjuanocampo.enfila.domain.usecases.repository.UserRepository
import com.ingjuanocampo.enfila.domain.usecases.repository.base.Repository
import com.ingjuanocampo.enfila.domain.util.EMPTY_STRING
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*

class HomeUC(private val companyRepo: CompanyRepository,
             val userRepository: UserRepository,
             val shiftRepository: ShiftRepository,
             val shiftInteractions: ShiftInteractions
) {

    private var homeCache : Home? = null

    fun load(): Flow<Home> {
        return shiftRepository.getAllObserveData().map {

            val user = userRepository.getCurrent()
            companyRepo.id = user?.companyIds?.first() ?: EMPTY_STRING
            shiftRepository.id = companyRepo.id
            val currentCompany = companyRepo.getAllData()

            val home = Home(
                selectedCompany = currentCompany ?: CompanySite(),
                totalTurns = shiftRepository.getAllData()!!.filter { it.isActive() }.count(),
                avrTime = 306
            )
            homeCache = home

            it?.lastOrNull {sh -> sh.state == ShiftState.CALLING }?.let { shift1 ->
                home.currentTurn = shiftInteractions.loadShiftWithClient(shift1)

            }
            home.totalTurns = it!!.size
            home
        }.flowOn(Dispatchers.Default)
    }

    suspend fun next(): ShiftWithClient? {
           return shiftInteractions.next(homeCache?.currentTurn?.shift)
    }

    fun delete() {
    }


}