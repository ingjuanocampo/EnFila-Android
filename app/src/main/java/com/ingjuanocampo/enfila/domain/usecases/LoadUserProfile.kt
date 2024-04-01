package com.ingjuanocampo.enfila.domain.usecases

import com.ingjuanocampo.enfila.commons.toYearMonthDayFormat
import com.ingjuanocampo.enfila.domain.usecases.model.UserProfile
import com.ingjuanocampo.enfila.domain.usecases.repository.ClientRepository
import com.ingjuanocampo.enfila.domain.usecases.repository.CompanyRepository
import com.ingjuanocampo.enfila.domain.usecases.repository.ShiftRepository
import com.ingjuanocampo.enfila.domain.usecases.repository.UserRepository
import com.ingjuanocampo.enfila.domain.util.EMPTY_STRING
import javax.inject.Inject

class LoadUserProfile
    @Inject
    constructor(
        private val companyRepo: CompanyRepository,
        private val userRepository: UserRepository,
        private val shiftRepository: ShiftRepository,
        private val clientRepository: ClientRepository,
        private val calculateShiftAverageWaitTimes: CalculateShiftAverageWaitTimes,
    ) {
        suspend operator fun invoke(): UserProfile {
            val user = userRepository.getCurrent()!!
            companyRepo.id = user.companyIds?.first() ?: EMPTY_STRING
            shiftRepository.id = companyRepo.id
            val currentCompany = companyRepo.loadAllData()?.firstOrNull()!!

            val shifts = shiftRepository.loadAllData()!!

            val waitingTimeAverage = calculateShiftAverageWaitTimes(shifts)
            val attentionTimeAverage = calculateShiftAverageWaitTimes.attentionTime(shifts)

            val totalShifts = shifts.count()
            val totalClients = clientRepository.loadAllData()!!.count()

            var counterOfShiftsByDay = 0
            var counterOfClientsByDay = 0
            val shiftByDay =
                shifts.groupBy {
                    it.date.toYearMonthDayFormat()
                }
            val totalDays = shiftByDay.count()

            shiftByDay.forEach { day, shifts ->
                counterOfShiftsByDay = counterOfClientsByDay + shifts.size
                counterOfClientsByDay = counterOfShiftsByDay + shifts.groupBy { it.contactId }.count()
            }

            // TODO review how to get the attentionTime

            return UserProfile(
                companyName = currentCompany.name!!,
                userName = user.name!!,
                email = user.id,
                phone = user.phone,
                totalNumberClients = totalClients,
                totalShiftHistory = totalShifts,
                shiftByDay = "${counterOfShiftsByDay / totalDays}",
                clientsByDay = "${counterOfShiftsByDay / totalDays}",
                waitingTime = waitingTimeAverage,
                attentionTime = attentionTimeAverage, // Not ready yet
            )
        }
    }
