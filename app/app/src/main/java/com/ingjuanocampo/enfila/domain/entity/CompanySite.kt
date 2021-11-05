package com.ingjuanocampo.enfila.domain.entity

import com.ingjuanocampo.enfila.domain.util.EMPTY_STRING

data class CompanySite(
     val id: String = EMPTY_STRING,
     val name: String? = EMPTY_STRING,
     val shiftsIdList: List<String>? = null
) {
    var shifts: ArrayList<Shift> = arrayListOf()
}
