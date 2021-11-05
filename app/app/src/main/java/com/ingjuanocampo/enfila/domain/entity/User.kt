package com.ingjuanocampo.enfila.domain.entity

import com.ingjuanocampo.enfila.domain.util.EMPTY_STRING

data class User(
    val id: String,
    val phone: String,
    val name: String? = EMPTY_STRING,
    var companyIds: List<String>? = null
)