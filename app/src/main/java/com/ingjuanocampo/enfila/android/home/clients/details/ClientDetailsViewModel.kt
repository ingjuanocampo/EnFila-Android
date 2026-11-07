package com.ingjuanocampo.enfila.android.home.clients.details

import com.ingjuanocampo.common.composable.MviBaseViewModel
import com.ingjuanocampo.enfila.android.navigation.NavigationDestinations
import com.ingjuanocampo.enfila.android.utils.launchGeneral
import com.ingjuanocampo.enfila.data.backend.source.BackendCompanySiteSource
import com.ingjuanocampo.enfila.domain.entity.Client
import com.ingjuanocampo.enfila.domain.entity.CompanySite
import com.ingjuanocampo.enfila.domain.usecases.LoadClientDetailsUC
import com.ingjuanocampo.enfila.domain.usecases.repository.ClientRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

data class ProfileEditorState(
    val isEditing: Boolean = false,
    val isSaving: Boolean = false,
    val name: String = "",
    val email: String = "",
    val birthDate: String = "",
    val sex: String = "",
    val city: String = "",
    val notes: String = "",
    val favoriteOrder: String = "",
    val favoriteStoreId: String = "",
    val error: String? = null,
    val companySites: List<CompanySite> = emptyList(),
)

@HiltViewModel
class ClientDetailsViewModel
    @Inject
    constructor(
        private val loadClientDetailsUC: LoadClientDetailsUC,
        private val navigationDestinations: NavigationDestinations,
        private val clientRepository: ClientRepository,
        private val companySiteSource: BackendCompanySiteSource,
    ) : MviBaseViewModel<ClientDetailsViewState>(ClientDetailsViewState.Loading) {
        private var currentClientId: String? = null
        private var savedClient: Client? = null

        private val _editor = MutableStateFlow(ProfileEditorState())
        val editor: StateFlow<ProfileEditorState> = _editor.asStateFlow()

        fun loadClientDetails(clientId: String) {
            currentClientId = clientId
            _state.value = ClientDetailsViewState.Loading

            launchGeneral {
                val sites = companySiteSource.fetchDataAll("") ?: emptyList()
                _editor.value = _editor.value.copy(companySites = sites)
                try {
                    loadClientDetailsUC(clientId).collect { clientDetails ->
                        if (clientDetails != null) {
                            val overlay = savedClient?.takeIf { it.id == clientDetails.client.id }
                            val details = if (overlay != null) clientDetails.copy(client = overlay) else clientDetails
                            _state.value = ClientDetailsViewState.Success(details)
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

        fun startEdit() {
            val client = (_state.value as? ClientDetailsViewState.Success)?.clientDetails?.client ?: return
            _editor.value =
                _editor.value.copy(
                    isEditing = true,
                    error = null,
                    name = client.name.orEmpty(),
                    email = client.email.orEmpty(),
                    birthDate = client.birthDate.orEmpty(),
                    sex = client.sex.orEmpty(),
                    city = client.city.orEmpty(),
                    notes = client.notes.orEmpty(),
                    favoriteOrder = client.favoriteOrder.orEmpty(),
                    favoriteStoreId = client.favoriteStoreId.orEmpty(),
                )
        }

        fun onEditorChange(editor: ProfileEditorState) {
            _editor.value = editor
        }

        fun cancelEdit() {
            _editor.value = _editor.value.copy(isEditing = false, isSaving = false, error = null)
        }

        fun saveProfile() {
            val details = (_state.value as? ClientDetailsViewState.Success)?.clientDetails ?: return
            val editor = _editor.value
            launchGeneral {
                _editor.value = editor.copy(isSaving = true, error = null)
                val updated =
                    clientRepository.updateProfile(
                        details.client.copy(
                            name = editor.name.trim(),
                            email = editor.email.trim(),
                            birthDate = editor.birthDate.trim(),
                            sex = editor.sex.trim(),
                            city = editor.city.trim(),
                            notes = editor.notes.trim(),
                            favoriteOrder = editor.favoriteOrder.trim(),
                            favoriteStoreId = editor.favoriteStoreId.trim(),
                        ),
                    )
                if (updated != null) {
                    savedClient = updated
                    _state.value = ClientDetailsViewState.Success(details.copy(client = updated))
                    _editor.value = _editor.value.copy(isEditing = false, isSaving = false, error = null)
                } else {
                    _editor.value = _editor.value.copy(isSaving = false, error = "Could not save profile")
                }
            }
        }
    }
