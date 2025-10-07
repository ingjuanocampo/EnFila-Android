package com.ingjuanocampo.enfila.domain.usecases.signing

import com.ingjuanocampo.enfila.domain.entity.CompanySite
import com.ingjuanocampo.enfila.domain.entity.User
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
        return try {
            // Create the company first without ID (backend will generate it)
            val companyToCreate = CompanySite(
                id = "", // Empty ID for creation - backend will generate
                name = companyName,
            )

            // Create company using POST endpoint
            val createdCompany = companySiteRepository.createCompanySite(companyToCreate)

            if (createdCompany == null) {
                return AuthState.AuthError(Exception("Failed to create company. Please check your internet connection and try again."))
            }

            // Update user with company ID and create user using POST endpoint (without ID)
            val userToCreate = user.copy(
                companyIds = listOf(createdCompany.id),
                id = "" // Clear ID for creation - backend will use phone as ID
            )

            val createdUser = userRepository.createUser(userToCreate)

            if (createdUser == null) {
                // If user creation fails, we should ideally clean up the created company
                // but for now, just return error
                return AuthState.AuthError(Exception("Failed to create user account. Please verify your information and try again."))
            }

            // Update repository IDs for subsequent operations
            companySiteRepository.id = createdCompany.id
            shiftRepository.id = createdCompany.id
            userRepository.id = createdUser.id

            // Refresh data to ensure everything is in sync
            companySiteRepository.refresh()
            clientRepository.refresh()
            shiftRepository.refresh()

            appStateProvider.toLoggedState()
            AuthState.Authenticated

        } catch (e: Exception) {
            AuthState.AuthError(e)
        }
    }
}
