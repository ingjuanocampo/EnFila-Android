package com.ingjuanocampo.enfila.android.home.list.viewmodel

import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ingjuanocampo.enfila.android.home.list.model.ShiftItem
import com.ingjuanocampo.enfila.android.home.list.model.mapToUI
import com.ingjuanocampo.enfila.android.utils.launchGeneral
import com.ingjuanocampo.enfila.domain.usecases.FinishShiftUC
import com.ingjuanocampo.enfila.domain.usecases.list.ListUC
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class ViewModelListItems
    @Inject
    constructor(
        private val listUC: ListUC,
        private val finishShiftUC: FinishShiftUC,
    ) : ViewModel() {
        val state = MutableLiveData<List<ShiftItem>>()

        fun load(
            isActive: Boolean = true,
            bundle: Bundle? = null,
        ) {
            val clientId = bundle?.getString("ClientId")
            launchGeneral {
                (
                    if (clientId.isNullOrEmpty().not()) {
                        listUC.loadByClientId(clientId!!)
                    } else if (isActive) {
                        listUC.loadActiveShift()
                    } else {
                        listUC.loadInactiveShift()
                    }
                ).map { shifts ->
                    shifts.map {
                        it.mapToUI()
                    }
                }.map {
                    if (isActive) {
                        it
                    } else {
                        it.sortedByDescending { item -> item.endDate }
                    }
                }.collect {
                    state.postValue(it)
                }
            }
        }

        fun finish(id: String) {
            viewModelScope.launchGeneral {
                finishShiftUC.invoke(id).collect { }
            }
        }
    }
