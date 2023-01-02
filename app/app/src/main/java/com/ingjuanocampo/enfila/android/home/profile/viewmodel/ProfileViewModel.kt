package com.ingjuanocampo.enfila.android.home.profile.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ingjuanocampo.enfila.android.home.profile.domain.ProfileCard
import com.ingjuanocampo.enfila.android.utils.launchGeneral
import com.ingjuanocampo.enfila.di.AppComponent
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.concurrent.TimeUnit

class ProfileViewModel: ViewModel() {

    val loadProfileUC = AppComponent.domainModule.providesLoadUserProfile()

    val state = MutableStateFlow<ProfileState>(ProfileState.LoadingProfileInfo)

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
                "20",
                "12",
                "12 mins"

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