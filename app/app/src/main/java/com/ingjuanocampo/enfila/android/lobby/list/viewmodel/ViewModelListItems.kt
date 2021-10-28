package com.ingjuanocampo.enfila.android.lobby.list.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ingjuanocampo.enfila.android.lobby.list.ShiftItem
import com.ingjuanocampo.enfila.domain.di.domain.DomainModule.provideListUC
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class ViewModelListItems : ViewModel() {

    val state = MutableLiveData<List<ShiftItem>>()

    private val listUC = provideListUC()

    fun load(isActive: Boolean  = true) {
        viewModelScope.launch {
            (if (isActive) listUC.loadActiveShift() else listUC.loadInactiveShift()).map { shifts ->
                shifts.map {
                    ShiftItem(
                        id = it.shift.id?.toInt() ?: 0,
                        name = it.client.name ?: "",
                        phone = it.client.id ?: "",
                        currentTurn = it.shift.number.toString(),
                        issueDate = it.shift.date ?: 0L,
                        state = it.shift.state.name,
                        endDate = it.shift.endDate ?: 0L
                    )
                }

            }.collect {
                state.postValue(it)
            }
        }
    }

}