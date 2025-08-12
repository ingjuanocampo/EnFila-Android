package com.ingjuanocampo.enfila.android.home.clients.details

import com.ingjuanocampo.common.composable.MviBaseViewModel
import com.ingjuanocampo.enfila.android.navigation.NavigationDestinations
import com.ingjuanocampo.enfila.android.utils.launchGeneral
import com.ingjuanocampo.enfila.domain.usecases.LoadClientDetailsUC
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ClientDetailsViewModel
    @Inject
    constructor(
        private val loadClientDetailsUC: LoadClientDetailsUC,
        private val navigationDestinations: NavigationDestinations,
    ) : MviBaseViewModel<ClientDetailsViewState>(ClientDetailsViewState.Loading) {
        private var currentClientId: String? = null

        fun loadClientDetails(clientId: String) {
            currentClientId = clientId
            _state.value = ClientDetailsViewState.Loading

            launchGeneral {
                try {
                    loadClientDetailsUC(clientId).collect { clientDetails ->
                        if (clientDetails != null) {
                            _state.value = ClientDetailsViewState.Success(clientDetails)
                        } else {
                            _state.value = ClientDetailsViewState.NotFound
                        }
                    }
                } catch (e: Exception) {
                    _state.value =
                        ClientDetailsViewState.Error(
                            e.message ?: "Unknown error occurred",
                        )
                }
            }
        }

        fun onShiftClicked(shiftId: String) {
            launchGeneral {
                _event.emit(navigationDestinations.navigateToShiftDetails(shiftId))
            }
        }

        fun onRefresh() {
            currentClientId?.let { loadClientDetails(it) }
        }
    }
