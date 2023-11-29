package com.ingjuanocampo.enfila.android.home.list.model

import com.ingjuanocampo.cdapter.RecyclerViewType
import com.ingjuanocampo.enfila.android.utils.ViewTypes

data class EmptyDelegate(
    val title: String
): RecyclerViewType {
    override fun getDelegateId(): Int {
        return this.hashCode()
    }

    override fun getViewType(): Int {
        return ViewTypes.EMPTY_VIEW.ordinal
    }
}