package com.ingjuanocampo.enfila.android.home.profile.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ingjuanocampo.enfila.android.home.profile.model.ProfileCard
import com.ingjuanocampo.enfila.android.utils.launchGeneral
import com.ingjuanocampo.enfila.di.AppComponent
import com.ingjuanocampo.enfila.domain.usecases.LoadUserProfile
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val loadProfileUC: LoadUserProfile,
): ViewModel() {

    private val state = MutableStateFlow<ProfileState>(ProfileState.LoadingProfileInfo)

    fun getState(): StateFlow<ProfileState> = state

    var clientCounter = 0

    init {
        viewModelScope.launchGeneral {

            state.value = ProfileState.LoadingProfileInfo

            val user = loadProfileUC.invoke()
            clientCounter = user.totalNumberClients

            val profileCard = ProfileCard(
                companyName = user.companyName,
                phone = user.phone,
                email = user.companyName,
                numberClients = "#${clientCounter}",
                totalShifts = "#${user.totalShiftHistory}",
                shiftByDay = user.shiftByDay,
                clientsByDay = user.clientsByDay,
                waitingTime = user.waitingTime,
                attentionTime = user.attentionTime

            )
            state.value = ProfileState.ProfileLoaded(profileCard)
           // startUpdates()

        }

    }

   /* private suspend fun startUpdates() {
        while (true) {
            delay(TimeUnit.SECONDS.toMillis(1))
            clientCounter += 1
            val profileCard = state.value.copy(numberClients = "#${clientCounter}")
            state.value = profileCard

        }


    }*/


}