package com.ingjuanocampo.enfila.android.assignation.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ingjuanocampo.enfila.android.utils.launchGeneral
import com.ingjuanocampo.enfila.domain.entity.Client
import com.ingjuanocampo.enfila.domain.usecases.ShiftInteractions
import com.ingjuanocampo.enfila.domain.usecases.repository.ClientRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ViewModelAssignation @Inject constructor(
    private val shiftInteractions: ShiftInteractions,
    private val clientRepository: ClientRepository,
) : ViewModel() {

    var tunr: Int = 0

    var closestTurn = 0

    val assignationState: MutableLiveData<AssignationState> = MutableLiveData(AssignationState.IDLE)

    private var client: Client? = null

    var phoneNumber: String = ""
        set(value) {
            if (value.isNotEmpty() && value.count() == 10) {
                field = value
                launchGeneral {
                    client = clientRepository.getById(value)
                    if (client != null) {
                        withContext(Dispatchers.Main) {
                            name = client!!.name!!
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
            client = if (client == null ) {
                Client(id = phoneNumber, name = name, shifts = listOf(tunr.toString()))
            } else {
                client!!.copy(shifts = client!!.shifts?.toMutableList()?.apply {
                    add(tunr.toString())
                }?: listOf(tunr.toString()))
            }
            shiftInteractions.addNewTurn(tunr, client!!, note).collect {
                assignationState.postValue(AssignationState.AssignationSet)
            }
        }
    }
}
