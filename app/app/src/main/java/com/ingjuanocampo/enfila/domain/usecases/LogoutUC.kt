package com.ingjuanocampo.enfila.domain.usecases

import com.ingjuanocampo.enfila.domain.state.AppState
import com.ingjuanocampo.enfila.domain.state.AppStateProvider
import com.ingjuanocampo.enfila.domain.usecases.repository.ClientRepository
import com.ingjuanocampo.enfila.domain.usecases.repository.CompanyRepository
import com.ingjuanocampo.enfila.domain.usecases.repository.ShiftRepository
import com.ingjuanocampo.enfila.domain.usecases.repository.UserRepository
import javax.inject.Inject

class LogoutUC @Inject constructor(
    private val userRepository: UserRepository,
    private val clientRepository: ClientRepository,
    private val shiftRepository: ShiftRepository,
    private val companyRepository: CompanyRepository,
    private val appState: AppStateProvider

) {

    suspend operator fun invoke() {
        userRepository.deleteAll()
        clientRepository.deleteAll()
        shiftRepository.deleteAll()
        companyRepository.deleteAll()
        appState.toNotLoggedState()
    }

}