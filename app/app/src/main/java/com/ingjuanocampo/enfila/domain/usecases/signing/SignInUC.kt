package com.ingjuanocampo.enfila.domain.usecases.signing

import com.ingjuanocampo.enfila.domain.entity.CompanySite
import com.ingjuanocampo.enfila.domain.entity.User
import com.ingjuanocampo.enfila.domain.entity.getNow
import com.ingjuanocampo.enfila.domain.state.AppStateProvider
import com.ingjuanocampo.enfila.domain.usecases.repository.CompanyRepository
import com.ingjuanocampo.enfila.domain.usecases.repository.ShiftRepository
import com.ingjuanocampo.enfila.domain.usecases.repository.UserRepository
import com.ingjuanocampo.enfila.domain.util.EMPTY_STRING
import kotlinx.coroutines.flow.*

class SignInUC(
    private val userRepository: UserRepository,
    private val companySiteRepository: CompanyRepository,
    private val appStateProvider: AppStateProvider,
    private val shiftRepository: ShiftRepository
) {

    operator fun invoke(id: String): Flow<AuthState> {
        userRepository.id = id
        return userRepository.getAllObserveData().map { user ->
            companySiteRepository.id = user?.companyIds?.firstOrNull() ?: EMPTY_STRING
            shiftRepository.id = user?.companyIds?.firstOrNull() ?: EMPTY_STRING
            user
        }.flatMapLatest {
            if (it != null){
                companySiteRepository.getAllObserveData()
            } else flowOf(null)
        }.flatMapLatest { companyData ->
            if (companyData != null) {
                shiftRepository.refresh().map { companyData }.catch { companyData }
            } else flowOf(null)
        }.map { data ->
            data != null
        }.map {
            if (it) {
                appStateProvider.toLoggedState()
                AuthState.Authenticated
            } else {
                AuthState.NewAccount(id)
            }
        }
    }

    suspend fun createUserAndSignIn(user: User, companyName: String): Flow<AuthState> {
        return companySiteRepository.createOrUpdateFlow(
            CompanySite(
                id = getNow().toString() + "CompanyId",
                name = companyName
            )
        ).flatMapLatest { company ->
            user.companyIds = listOf(company?.id.orEmpty())
            userRepository.createOrUpdateFlow(user).map {
                appStateProvider.toLoggedState()
                AuthState.Authenticated as AuthState
            }.catch {
                    emit(AuthState.AuthError(it))
                }
        }

    }
}