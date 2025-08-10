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
    val shiftByDay: String,
    val clientsByDay: String, // level 3
    val waitingTime: String,
    val attentionTime: String
)
