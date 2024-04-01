package com.ingjuanocampo.enfila.domain.state.home

import com.ingjuanocampo.cdapter.RecyclerViewType

sealed class HomeState {
    object Loading : HomeState()

    object Empty : HomeState()

    data class HomeLoaded(val items: List<RecyclerViewType>) : HomeState()
}
