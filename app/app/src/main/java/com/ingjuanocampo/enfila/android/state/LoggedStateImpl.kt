package com.ingjuanocampo.enfila.android.state

import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import com.ingjuanocampo.enfila.android.lobby.ActivityLobby
import com.ingjuanocampo.enfila.domain.state.states.LoggedState

class LoggedStateImpl(private val context: Context): LoggedState {

    override fun navigateLaunchScreen() {
        context.startActivity(Intent(context, ActivityLobby::class.java).apply {
            flags = FLAG_ACTIVITY_NEW_TASK
        })
    }

}