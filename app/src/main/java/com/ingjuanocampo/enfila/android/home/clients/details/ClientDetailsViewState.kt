package com.ingjuanocampo.enfila.android.home.clients.details

import com.ingjuanocampo.enfila.domain.usecases.model.ClientDetails

sealed class ClientDetailsViewState {
    object Loading : ClientDetailsViewState()

    data class Success(
        val clientDetails: ClientDetails,
    ) : ClientDetailsViewState()

    data class Error(
        val message: String,
    ) : ClientDetailsViewState()

    object NotFound : ClientDetailsViewState()
}
