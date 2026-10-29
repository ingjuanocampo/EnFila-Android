package com.ingjuanocampo.enfila.domain.entity

import com.ingjuanocampo.enfila.domain.util.EMPTY_STRING

data class Client(
    override val id: String, // Same as phone
    val name: String? = EMPTY_STRING,
    val shifts: List<String>? = ArrayList(),
    val email: String? = null,
    val birthDate: String? = null,
    val sex: String? = null,
    val city: String? = null,
    val notes: String? = null,
    val favoriteOrder: String? = null,
    val favoriteStoreId: String? = null,
) : IdentifyObject

val defaultClient = Client("", "No name", emptyList())
