package com.ingjuanocampo.enfila.android.lobby.list.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ingjuanocampo.enfila.android.lobby.list.model.ShiftItem
import com.ingjuanocampo.enfila.android.lobby.list.model.mapToUI
import com.ingjuanocampo.enfila.android.utils.launchGeneral
import com.ingjuanocampo.enfila.di.AppComponent
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map

class ViewModelListItems : ViewModel() {

    val state = MutableLiveData<List<ShiftItem>>()

    private val listUC = AppComponent.domainModule.provideListUC()

    fun load(isActive: Boolean  = true) {
        launchGeneral {
            (if (isActive) listUC.loadActiveShift() else listUC.loadInactiveShift()).map { shifts ->
                shifts.map {
                    it.mapToUI()
                }

            }.collect {
                state.postValue(it)
            }
        }
    }

}