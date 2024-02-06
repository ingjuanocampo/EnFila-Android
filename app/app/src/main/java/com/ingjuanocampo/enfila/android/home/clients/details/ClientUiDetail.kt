package com.ingjuanocampo.enfila.android.home.clients.details

import com.ingjuanocampo.enfila.android.home.list.model.ShiftItem

data class ClientUiDetail(
    val phone: String, 
    val name: String,
    val turns: List<ShiftItem>
)
