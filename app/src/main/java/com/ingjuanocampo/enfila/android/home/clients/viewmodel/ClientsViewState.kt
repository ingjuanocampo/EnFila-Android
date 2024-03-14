package com.ingjuanocampo.enfila.android.home.clients.viewmodel

import com.ingjuanocampo.enfila.domain.entity.Client

sealed class ClientsViewState {
    data class DataLoaded(val clients: List<Client>) : ClientsViewState()

    object Loading : ClientsViewState()

    object Empty : ClientsViewState()
}
