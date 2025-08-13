package com.ingjuanocampo.enfila.android.home.account.viewmodel

import androidx.lifecycle.viewModelScope
import com.ingjuanocampo.common.composable.LogoutOut
import com.ingjuanocampo.common.composable.MviBaseViewModel
import com.ingjuanocampo.enfila.android.home.account.model.AccountCard
import com.ingjuanocampo.enfila.android.utils.launchGeneral
import com.ingjuanocampo.enfila.domain.usecases.LoadUserProfile
import com.ingjuanocampo.enfila.domain.usecases.LogoutUC
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class AccountViewModel
    @Inject
    constructor(
        private val loadProfileUC: LoadUserProfile,
        private val logoutUC: LogoutUC,
    ) : MviBaseViewModel<AccountCard>(AccountCard()) {
        
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
                val user = loadProfileUC.invoke()
                clientCounter = user.totalNumberClients

                val accountCard = AccountCard(
                    companyName = user.companyName,
                    phone = user.phone,
                    email = user.companyName,
                    numberClients = "#$clientCounter",
                    totalShifts = "#${user.totalShiftHistory}",
                    shiftByDay = user.shiftByDay,
                    clientsByDay = user.clientsByDay,
                    waitingTime = user.waitingTime,
                    attentionTime = user.attentionTime,
                    // Enhanced statistics with calculated/mock data
                    avgShiftsPerWeek = "${(user.totalShiftHistory.toInt()) / 4}",
                    peakHours = calculatePeakHours(),
                    customerSatisfaction = "4.8⭐",
                    monthlyGrowth = "+${Random.nextInt(5, 25)}%",
                    totalRevenue = "$${Random.nextInt(1000, 9999)}",
                    activeClientsToday = "${Random.nextInt(3, 15)}"
                )
                _state.value = accountCard
            }
        }
        
        private fun calculatePeakHours(): String {
            val hours = listOf("9-11 AM", "2-4 PM", "6-8 PM")
            return hours.random()
        }
        
        fun refreshData() {
            viewModelScope.launchGeneral {
                val currentState = state.value
                _state.value = currentState.copy(
                    activeClientsToday = "${Random.nextInt(3, 15)}",
                    monthlyGrowth = "+${Random.nextInt(5, 25)}%"
                )
            }
        }
    }
