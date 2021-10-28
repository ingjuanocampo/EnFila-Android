package com.ingjuanocampo.enfila.android.lobby.home.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ingjuanocampo.enfila.android.utils.launchGeneral
import com.ingjuanocampo.enfila.domain.di.domain.DomainModule
import com.ingjuanocampo.enfila.domain.usecases.model.Home
import com.ingjuanocampo.enfila.domain.usecases.model.ShiftWithClient
import kotlinx.coroutines.flow.collect

class ViewModelHome : ViewModel() {

    private val homeUC = DomainModule.provideHomeUC()
    val state = MutableLiveData<HomeState>()

    fun loadCurrentTurn() {
        viewModelScope.launchGeneral {
            state.postValue(HomeState.Loading)
            homeUC.load().collect {
                state.postValue(HomeState.HomeLoaded(it))
            }

        }
    }

    private fun updateCurrentTurn(currentTurn: ShiftWithClient?) {
        if (currentTurn!= null) {
            state.postValue(HomeState.CurrentTurn(currentTurn))
        } else {
            state.postValue(HomeState.Empty)
        }
    }

    fun next() {
        viewModelScope.launchGeneral {
            val next = homeUC.next()
            updateCurrentTurn(next)
        }
    }

}

sealed class HomeState {
    object Loading: HomeState()
    object Empty: HomeState()
    data class HomeLoaded(val home: Home) : HomeState()
    data class CurrentTurn(val shift: ShiftWithClient) : HomeState()
}