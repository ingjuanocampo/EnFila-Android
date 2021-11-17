package com.ingjuanocampo.enfila.android.lobby.home.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ingjuanocampo.enfila.domain.state.home.HomeState
import com.ingjuanocampo.enfila.android.utils.launchGeneral
import com.ingjuanocampo.enfila.di.AppComponent
import kotlinx.coroutines.flow.collect

class ViewModelHome : ViewModel() {

    private val homeUC = AppComponent.domainModule.provideHomeUC()
    val state = MutableLiveData<HomeState>()

    fun loadCurrentTurn() {
        viewModelScope.launchGeneral {
            state.postValue(HomeState.Loading)
            homeUC.load().collect {
                state.postValue(it)
            }

        }
    }

    fun next() {
        viewModelScope.launchGeneral {
            homeUC.next()
        }
    }

}
