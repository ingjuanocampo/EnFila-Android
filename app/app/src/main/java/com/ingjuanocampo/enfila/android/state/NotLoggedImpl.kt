package com.ingjuanocampo.enfila.android.state

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.ingjuanocampo.enfila.android.login.ActivityLogin
import com.ingjuanocampo.enfila.domain.state.states.NotLoggedState
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotLoggedImpl @Inject constructor() : NotLoggedState {

    override fun navigateLaunchScreen(context: Context) {
        (context as? Activity)?.finishAffinity()
        context.startActivity(
            Intent(context, ActivityLogin::class.java).apply {
                //flags = Intent.FLAG_ACTIVITY_NEW_TASK
            },
        )
    }
}
