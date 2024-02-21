package com.ingjuanocampo.enfila.domain.state

import android.content.Context

interface AppState {
    fun navigateLaunchScreen(context: Context)
}
