package com.ingjuanocampo.enfila.android.home.tips

data class TipsViewState(
    val isLoading: Boolean = false,
    val tips: List<com.ingjuanocampo.enfila.domain.entity.Tip> = emptyList(),
    val error: String? = null,
)
