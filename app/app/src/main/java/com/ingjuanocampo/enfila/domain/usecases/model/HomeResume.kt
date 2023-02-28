package com.ingjuanocampo.enfila.domain.usecases.model

import com.ingjuanocampo.cdapter.RecyclerViewType
import com.ingjuanocampo.enfila.android.utils.ViewTypes
import com.ingjuanocampo.enfila.domain.entity.CompanySite

data class HomeResume(
    val selectedCompany: CompanySite,
    var totalTurns: Int = 0,
    var avrTime: String = "",
) : RecyclerViewType {
    override fun getDelegateId(): Int {
        return selectedCompany.hashCode()
    }

    override fun getViewType(): Int {
        return ViewTypes.HOME_RESUME.ordinal
    }
}
