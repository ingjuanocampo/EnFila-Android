package com.ingjuanocampo.enfila.domain.usecases

import com.ingjuanocampo.enfila.domain.usecases.repository.CompanyRepository
import com.ingjuanocampo.enfila.domain.usecases.repository.ShiftRepository
import com.ingjuanocampo.enfila.domain.usecases.repository.UserRepository
import com.ingjuanocampo.enfila.domain.util.EMPTY_STRING
import kotlinx.coroutines.flow.*

class LoadInitialInfoUC(private val userRepository: UserRepository,
                        private val shiftRepository: ShiftRepository,
                        private val companyRepo: CompanyRepository,) {

    suspend operator fun invoke()  = flow {
       emit(userRepository.getCurrent())
    }.flatMapLatest { user ->
        if (user?.id != null) {
            companyRepo.id = user?.companyIds?.first()?: EMPTY_STRING
            shiftRepository.id = companyRepo.id
            companyRepo.refresh().flatMapLatest {
                shiftRepository.refresh().map { true }
            }
        } else flowOf(false)
    }
}