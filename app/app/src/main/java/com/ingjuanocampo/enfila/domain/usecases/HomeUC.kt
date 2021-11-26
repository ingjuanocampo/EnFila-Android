package com.ingjuanocampo.enfila.domain.usecases

import com.ingjuanocampo.cdapter.RecyclerViewType
import com.ingjuanocampo.enfila.android.lobby.list.model.HeaderItem
import com.ingjuanocampo.enfila.android.lobby.list.model.mapToUI
import com.ingjuanocampo.enfila.android.utils.ViewTypes
import com.ingjuanocampo.enfila.domain.entity.CompanySite
import com.ingjuanocampo.enfila.domain.entity.Shift
import com.ingjuanocampo.enfila.domain.entity.ShiftState
import com.ingjuanocampo.enfila.domain.state.home.HomeState
import com.ingjuanocampo.enfila.domain.usecases.list.isActive
import com.ingjuanocampo.enfila.domain.usecases.model.HomeResume
import com.ingjuanocampo.enfila.domain.usecases.repository.CompanyRepository
import com.ingjuanocampo.enfila.domain.usecases.repository.ShiftRepository
import com.ingjuanocampo.enfila.domain.usecases.repository.UserRepository
import com.ingjuanocampo.enfila.domain.util.EMPTY_STRING
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

class HomeUC(
    private val companyRepo: CompanyRepository,
    private val userRepository: UserRepository,
    private val shiftRepository: ShiftRepository,
    private val shiftInteractions: ShiftInteractions
) {


    fun load(): Flow<HomeState> {
        return shiftRepository.getAllObserveData().map { shifts ->

            if (shifts.isNullOrEmpty().not()) {

                val items = ArrayList<RecyclerViewType>()

                val user = userRepository.getCurrent()
                companyRepo.id = user?.companyIds?.first() ?: EMPTY_STRING
                shiftRepository.id = companyRepo.id
                val currentCompany = companyRepo.loadAllData()?.firstOrNull()

                val home = HomeResume(
                    selectedCompany = currentCompany ?: CompanySite(),
                    totalTurns = shiftRepository.loadAllData()!!.filter { it.isActive() }.count(),
                    avrTime = 388
                )
                home.totalTurns = shifts!!.size

                items.add(home)



                getNextTurn(shifts)?.let { next ->
                    items.add(HeaderItem("Siguiente turno"))
                    items.add(
                        shiftInteractions.loadShiftWithClient(next).mapToUI(ViewTypes.NEXT_SHIFT)
                    )
                }

                getActiveShifts(shifts)?.let { activeShifts ->

                    items.add(HeaderItem("Turnos activos"))
                    activeShifts.forEach { active ->
                        items.add(
                            shiftInteractions.loadShiftWithClient(active)
                                .mapToUI(ViewTypes.ACTIVE_SHIFT)
                        )
                    }
                }

                HomeState.HomeLoaded(items)
            } else {
                HomeState.Empty
            }


        }.flowOn(Dispatchers.Default)
    }


    private fun getNextTurn(shifts: List<Shift>): Shift? {
        return shifts.lastOrNull { sh -> sh.state == ShiftState.WAITING }
    }


    private fun getActiveShifts(shifts: List<Shift>): List<Shift>? {
        return shifts.filter { sh -> sh.state == ShiftState.CALLING }.take(5)
    }


    suspend fun next() {
        val allShifts = shiftRepository.loadAllData()
        val next = allShifts?.let { getNextTurn(shifts = it) }
        shiftInteractions.next(next)
    }


    fun delete() {
    }


}