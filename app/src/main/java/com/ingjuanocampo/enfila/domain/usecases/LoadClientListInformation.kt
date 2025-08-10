package com.ingjuanocampo.enfila.domain.usecases

import com.ingjuanocampo.enfila.domain.entity.Client
import com.ingjuanocampo.enfila.domain.usecases.repository.ClientRepository
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@ViewModelScoped
class LoadClientListInformation
@Inject
constructor(
    private val clientRepository: ClientRepository
) {
    operator fun invoke(): Flow<List<Client>> {
        return clientRepository.getAllObserveData().filterNotNull().map {
            it.sortedBy { client ->
                client.name
            }.sortedByDescending { client ->
                client.shifts?.size
            }
        }
    }
}
