package com.ingjuanocampo.common.tab

import androidx.fragment.app.Fragment

data class FragmentTabItem(
    val title: String,
    val fragmentInstance: () -> Fragment
)