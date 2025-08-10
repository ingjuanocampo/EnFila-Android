package com.ingjuanocampo.enfila.android.home.home.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ingjuanocampo.enfila.android.utils.launchGeneral
import com.ingjuanocampo.enfila.domain.state.home.HomeState
import com.ingjuanocampo.enfila.domain.usecases.FinishShiftUC
import com.ingjuanocampo.enfila.domain.usecases.HomeUC
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ViewModelHome
@Inject
constructor(
    private val homeUC: HomeUC,
    private val finishShiftUC: FinishShiftUC
) : ViewModel() {
    val state = MutableLiveData<HomeState>()

    fun loadCurrentTurn() {
        viewModelScope.launchGeneral {
            state.postValue(HomeState.Loading)
            homeUC.load().collect {
                state.postValue(it)
            }
        }
    }

    fun finish(id: String) {
        viewModelScope.launchGeneral {
            finishShiftUC.invoke(id).collect { }
        }
    }

    fun cancel(id: String) {
        viewModelScope.launchGeneral {
            homeUC.cancel(id).collect { }
        }
    }

    fun next(id: String) {
        viewModelScope.launchGeneral {
            homeUC.next(id).collect { }
        }
    }
}
