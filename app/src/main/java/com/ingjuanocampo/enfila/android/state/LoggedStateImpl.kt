package com.ingjuanocampo.enfila.android.state

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import com.ingjuanocampo.enfila.android.home.ActivityLobby
import com.ingjuanocampo.enfila.domain.state.states.LoggedState
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LoggedStateImpl
@Inject
constructor() : LoggedState {
    override fun navigateLaunchScreen(context: Context) {
        (context as? Activity)?.finishAffinity()
        context.startActivity(
            Intent(context, ActivityLobby::class.java).apply {
                flags = FLAG_ACTIVITY_NEW_TASK
            }
        )
    }
}
