package com.ingjuanocampo.enfila.domain.entity

import com.ingjuanocampo.enfila.domain.util.EMPTY_STRING

data class Client(
    override val id: String, // Same as phone
    val name: String? = EMPTY_STRING,
    val shifts: List<String>? = ArrayList(),
) : IdentifyObject

val defaultClient = Client("", "No name", emptyList())
