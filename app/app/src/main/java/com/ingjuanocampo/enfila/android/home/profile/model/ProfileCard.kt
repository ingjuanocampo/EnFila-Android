package com.ingjuanocampo.enfila.android.home.profile.model

data class ProfileCard(
    val companyName: String,
    val phone: String,
    val email: String,
    val numberClients: String,
    val totalShifts: String,// level 1
    val shiftByDay: String,
    val clientsByDay: String, // level 3
    val avrTimeAttention: String, // Level 3
    val options: List<OptionCard> = emptyList()
)


data class OptionCard(val icon: Int, val title: String, val callback: ()-> Unit)