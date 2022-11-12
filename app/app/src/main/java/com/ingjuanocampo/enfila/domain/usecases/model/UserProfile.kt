package com.ingjuanocampo.enfila.domain.usecases.model

data class UserProfile(
    val companyName: String,
    val userName: String,
    val phone: String,
    val email: String,
/*    val averageOfTurnsByMonth: String,
    val averageOfClientMonths: String,*/
    val totalNumberClients: Int,
    val totalShiftHistory: Int,
)