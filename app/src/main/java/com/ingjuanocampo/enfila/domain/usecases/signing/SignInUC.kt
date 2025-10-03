package com.ingjuanocampo.enfila.domain.usecases.signing

import com.ingjuanocampo.enfila.domain.entity.CompanySite
import com.ingjuanocampo.enfila.domain.entity.User
import com.ingjuanocampo.enfila.domain.entity.getNow
import com.ingjuanocampo.enfila.domain.state.AppStateProvider
import com.ingjuanocampo.enfila.domain.usecases.repository.ClientRepository
import com.ingjuanocampo.enfila.domain.usecases.repository.CompanyRepository
import com.ingjuanocampo.enfila.domain.usecases.repository.ShiftRepository
import com.ingjuanocampo.enfila.domain.usecases.repository.UserRepository
import com.ingjuanocampo.enfila.domain.util.EMPTY_STRING
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class SignInUC
    @Inject
    constructor(
        private val userRepository: UserRepository,
        private val companySiteRepository: CompanyRepository,
        private val appStateProvider: AppStateProvider,
        private val shiftRepository: ShiftRepository,
        private val clientRepository: ClientRepository,
    ) {
        operator fun invoke(id: String): Flow<AuthState> {
            userRepository.id = id
            return flowOf(id).map {
                userRepository.refresh()
                // this enpooint is not working, at backend it seems to be ok
                // It get stuck on nothing
                val user = userRepository.getCurrent()
                return@map if (user?.id.isNullOrBlank()) {
                    AuthState.NewAccount(id)
                } else {
                    companySiteRepository.id = user?.companyIds?.firstOrNull() ?: EMPTY_STRING
                    shiftRepository.id = user?.companyIds?.firstOrNull() ?: EMPTY_STRING
                    // These 2 endpoints should be done only once the logic is success, for createing accoun tis different
                    companySiteRepository.refresh()
                    clientRepository.refresh()
                    val companyData = companySiteRepository.loadAllData()
                    if (companyData != null) {
                        shiftRepository.refresh()
                        appStateProvider.toLoggedState()
                        AuthState.Authenticated
                    } else {
                        AuthState.NewAccount(id)
                    }
                }

            }
        }

        suspend fun createUserAndSignIn(
            user: User,
            companyName: String,
        ): AuthState {
            val company =
                CompanySite(
                    id = getNow().toString() + "CompanyId",
                    name = companyName,
                )
            // this should create the profile, instead of updating
            companySiteRepository.updateData(company)
            user.companyIds = listOf(company?.id.orEmpty())
            userRepository.updateData(user).firstOrNull()
            appStateProvider.toLoggedState()
            return AuthState.Authenticated
        }
    }
