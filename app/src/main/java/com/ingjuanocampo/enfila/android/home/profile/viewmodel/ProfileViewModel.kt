package com.ingjuanocampo.enfila.android.home.profile.viewmodel

import androidx.lifecycle.viewModelScope
import com.ingjuanocampo.common.composable.LogoutOut
import com.ingjuanocampo.common.composable.MviBaseViewModel
import com.ingjuanocampo.enfila.android.home.profile.model.ProfileCard
import com.ingjuanocampo.enfila.android.utils.launchGeneral
import com.ingjuanocampo.enfila.domain.usecases.LoadUserProfile
import com.ingjuanocampo.enfila.domain.usecases.LogoutUC
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel
@Inject
constructor(
    private val loadProfileUC: LoadUserProfile,
    private val logoutUC: LogoutUC
) : MviBaseViewModel<ProfileCard>(ProfileCard()) {
    fun onLogout() {
        viewModelScope.launchGeneral {
            _state.value = state.value.copy(loadingLogout = true)
            logoutUC.invoke()
            _event.emit(LogoutOut)
        }
    }

    var clientCounter = 0

    init {
        viewModelScope.launchGeneral {

            // state.value = ProfileState.LoadingProfileInfo

            val user = loadProfileUC.invoke()
            clientCounter = user.totalNumberClients

            val profileCard =
                ProfileCard(
                    companyName = user.companyName,
                    phone = user.phone,
                    email = user.companyName,
                    numberClients = "#$clientCounter",
                    totalShifts = "#${user.totalShiftHistory}",
                    shiftByDay = user.shiftByDay,
                    clientsByDay = user.clientsByDay,
                    waitingTime = user.waitingTime,
                    attentionTime = user.attentionTime
                )
            _state.value = profileCard
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
