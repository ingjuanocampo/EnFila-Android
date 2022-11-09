package com.ingjuanocampo.enfila.android.home.profile.domain

data class ProfileCard(
    val companyName: String,
    val phone: String,
    val email: String,
    val numberClients: String,
    val totalShifts: String,
    val options: List<OptionCard> = emptyList()
)


data class OptionCard(val icon: Int, val title: String, val callback: ()-> Unit)