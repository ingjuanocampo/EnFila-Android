package com.ingjuanocampo.enfila.android.lobby.list

import com.ingjuanocampo.cdapter.RecyclerViewType
import com.ingjuanocampo.enfila.android.utils.ViewTypes

class UserItem(val phone: String, val name: String): RecyclerViewType {
    override fun getDelegateId(): Int = phone.hashCode()
    override fun getViewType(): Int {
        return ViewTypes.USER.ordinal
    }

}