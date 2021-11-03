package com.ingjuanocampo.enfila.domain.usecases

import com.ingjuanocampo.enfila.domain.usecases.repository.CompanyRepository
import com.ingjuanocampo.enfila.domain.usecases.repository.ShiftRepository
import com.ingjuanocampo.enfila.domain.usecases.repository.UserRepository
import com.ingjuanocampo.enfila.domain.util.EMPTY_STRING
import kotlinx.coroutines.flow.*

class LoadInitialInfoUC(private val userRepository: UserRepository,
                        private val shiftRepository: ShiftRepository,
                        private val companyRepo: CompanyRepository) {

    suspend operator fun invoke() = userRepository.getCurrent().let { user ->
            if (user?.id != null && user.id.isNotEmpty()) {
                companyRepo.id = user?.companyIds?.first()?: EMPTY_STRING
                shiftRepository.id = companyRepo.id
                companyRepo.refresh()
                true
            } else false


    }
}