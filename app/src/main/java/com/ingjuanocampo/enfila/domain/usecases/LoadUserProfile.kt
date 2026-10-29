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
            companyRepo.refresh() // Add refresh call to ensure company data is loaded
            val currentCompany = companyRepo.loadAllData()?.firstOrNull()

            val shifts = shiftRepository.loadAllData()

            val waitingTimeAverage = shifts?.let { calculateShiftAverageWaitTimes(it) } ?: "0"
            val attentionTimeAverage = shifts?.let { calculateShiftAverageWaitTimes.attentionTime(it) } ?: "0"

            val totalShifts = shifts?.count() ?: 0
            val totalClients = clientRepository.loadAllData()?.count() ?: 0 // Handle null case

            var counterOfShiftsByDay = 0
            var counterOfClientsByDay = 0
            val shiftByDay =
                shifts?.groupBy {
                    it.date.toYearMonthDayFormat()
                }
            val totalDays = shiftByDay?.count() ?: 0

            shiftByDay?.forEach { day, shifts ->
                counterOfShiftsByDay = counterOfClientsByDay + shifts.size
                counterOfClientsByDay = counterOfShiftsByDay + shifts.groupBy { it.contactId }.count()
            }

            // Calculate safe averages to prevent division by zero
            val avgShiftsByDay = if (totalDays > 0) counterOfShiftsByDay / totalDays else 0
            val avgClientsByDay = if (totalDays > 0) counterOfClientsByDay / totalDays else 0
            
            // TODO review how to get the attentionTime

            return UserProfile(
                companyName = currentCompany?.name ?: "No Company", // Handle null company
                userName = user.name ?: "Unknown User", // Handle null user name
                email = user.id,
                phone = user.phone ?: "", // Handle null phone
                totalNumberClients = totalClients,
                totalShiftHistory = totalShifts,
                shiftByDay = avgShiftsByDay.toString(),
                clientsByDay = avgClientsByDay.toString(),
                waitingTime = waitingTimeAverage,
                attentionTime = attentionTimeAverage, // Not ready yet
            )
        }
    }
