package com.ingjuanocampo.enfila.android.lobby.list.model

import com.ingjuanocampo.cdapter.RecyclerViewType
import com.ingjuanocampo.enfila.android.R
import com.ingjuanocampo.enfila.android.utils.ViewTypes

data class HeaderItem(val headerText: String, val icon: Int = R.drawable.ic_navigate_next, val linkText: String?= null): RecyclerViewType {
    override fun getDelegateId(): Int {
        return this.hashCode().toInt()
    }

    override fun getViewType(): Int {
        return ViewTypes.HEADER_LINK.ordinal
    }
}