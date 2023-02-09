package com.ingjuanocampo.enfila.domain.usecases

import com.ingjuanocampo.enfila.domain.usecases.model.UserProfile
import com.ingjuanocampo.enfila.domain.usecases.repository.ClientRepository
import com.ingjuanocampo.enfila.domain.usecases.repository.CompanyRepository
import com.ingjuanocampo.enfila.domain.usecases.repository.ShiftRepository
import com.ingjuanocampo.enfila.domain.usecases.repository.UserRepository
import com.ingjuanocampo.enfila.domain.util.EMPTY_STRING
import javax.inject.Inject

class LoadUserProfile @Inject constructor(
    private val companyRepo: CompanyRepository,
    private val userRepository: UserRepository,
    private val shiftRepository: ShiftRepository,
    private val clientRepository: ClientRepository,
) {


    suspend operator fun invoke(): UserProfile {
        val user = userRepository.getCurrent()!!
        companyRepo.id = user.companyIds?.first() ?: EMPTY_STRING
        shiftRepository.id = companyRepo.id
        val currentCompany = companyRepo.loadAllData()?.firstOrNull()!!

        val totalShifts = shiftRepository.loadAllData()!!.count()
        val totalClients = clientRepository.loadAllData()!!.count()

        return UserProfile(
            companyName = currentCompany.name!!,
            userName = user.name!!,
            email = user.id,
            phone = user.phone,
            totalNumberClients = totalClients,
            totalShiftHistory = totalShifts


        )
    }


}