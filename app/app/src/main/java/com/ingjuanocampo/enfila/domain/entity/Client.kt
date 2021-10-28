package com.ingjuanocampo.enfila.domain.entity

import com.ingjuanocampo.enfila.domain.util.EMPTY_STRING

data class Client(val id: String,// Same as phone
                  val name: String? = EMPTY_STRING,
                  val shifts: ArrayList<String> = ArrayList())
