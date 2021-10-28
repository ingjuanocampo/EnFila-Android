package com.ingjuanocampo.enfila.android.assignation.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ingjuanocampo.enfila.android.utils.launchGeneral
import com.ingjuanocampo.enfila.domain.di.domain.DomainModule
import kotlinx.coroutines.flow.collect

class ViewModelAssignation : ViewModel() {

    var tunr: Int = 0

    var closestTurn = 0

    private val shiftInteractions = DomainModule.providesShiftInteractions()
    val assignationState: MutableLiveData<AssignationState> = MutableLiveData(AssignationState.IDLE)

    var phoneNumber: String = ""
        set(value) {
            if (value.isNotEmpty() && value.count() == 10) {
                field = value
                assignationState.value = AssignationState.NumberSet
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
            shiftInteractions.addNewTurn(tunr, phoneNumber, name, note).collect {
                if (it != null) {
                    assignationState.postValue(AssignationState.AssignationSet)
                }
            }
        }
    }

}