package com.ingjuanocampo.enfila.domain.usecases.model

import com.ingjuanocampo.enfila.domain.entity.CompanySite
import com.ingjuanocampo.enfila.domain.entity.Client
import com.ingjuanocampo.enfila.domain.entity.Shift

class Home(val selectedCompany: CompanySite,
           var totalTurns: Int = 0,
           var avrTime: Long = 0) {
    var currentTurn: ShiftWithClient? = null
}

