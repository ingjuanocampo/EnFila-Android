package com.ingjuanocampo.enfila.android.assignation.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ingjuanocampo.enfila.android.utils.launchGeneral
import com.ingjuanocampo.enfila.di.AppComponent
import com.ingjuanocampo.enfila.domain.di.domain.DomainModule
import com.ingjuanocampo.enfila.domain.usecases.repository.ClientRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.withContext

class ViewModelAssignation : ViewModel() {

    var tunr: Int = 0

    var closestTurn = 0

    private val shiftInteractions = AppComponent.domainModule.providesShiftInteractions()
    private val clientRepository: ClientRepository = AppComponent.domainModule.provideClientRepository()
    val assignationState: MutableLiveData<AssignationState> = MutableLiveData(AssignationState.IDLE)

    var phoneNumber: String = ""
        set(value) {
            if (value.isNotEmpty() && value.count() == 10) {
                field = value
                launchGeneral {
                    val client = clientRepository.getById(value)
                    if (client != null) {
                        withContext(Dispatchers.Main) {
                            name = client.name!!
                        }
                    }
                    assignationState.postValue(AssignationState.NumberSet)
                }
            }
        }
    var name: String = ""
        set(value) {
            if (value.isNotEmpty()) {
                field = value
                assignationState.value = AssignationState.NameAndNoteSet
            }
        }
    var note: String = ""
        set(value) {
            if (value.isNotEmpty()) {
                field = value
                assignationState.value = AssignationState.NameAndNoteSet
            }
        }

    init {
        calculateNextTurn()
    }

    private fun calculateNextTurn() {
        launchGeneral {
            closestTurn = shiftInteractions.getClosestNewShiftTurn()
            tunr = closestTurn
        }
    }

    fun setTurn(turn: Int) {
        if (tunr > this.closestTurn) {
            assignationState.value = AssignationState.ErrorTurnAssigned
        } else {
            this.tunr = tunr
        }
    }

    fun createAssignation() {
        launchGeneral {
            assignationState.postValue(AssignationState.Loading)
            shiftInteractions.addNewTurn(tunr, phoneNumber, name, note).collect {
                assignationState.postValue(AssignationState.AssignationSet)
            }
        }
    }

}