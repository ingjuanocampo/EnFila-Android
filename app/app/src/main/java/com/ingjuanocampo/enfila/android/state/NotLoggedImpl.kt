package com.ingjuanocampo.enfila.android.state

import android.content.Context
import android.content.Intent
import com.ingjuanocampo.enfila.android.login.ActivityLogin
import com.ingjuanocampo.enfila.domain.state.states.NotLoggedState
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotLoggedImpl @Inject constructor(@ApplicationContext private val context: Context) : NotLoggedState {

    override fun navigateLaunchScreen() {
        context.startActivity(
            Intent(context, ActivityLogin::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            },
        )
    }
}
