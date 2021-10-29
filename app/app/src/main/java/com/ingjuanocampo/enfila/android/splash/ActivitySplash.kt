package com.ingjuanocampo.enfila.android.splash

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.activity.viewModels
import androidx.datastore.core.DataStore
import androidx.lifecycle.Observer
import com.ingjuanocampo.enfila.android.R
import com.ingjuanocampo.enfila.di.AppComponent
import java.util.prefs.Preferences

class SplashActivity : AppCompatActivity() {

    private val viewModel : ViewModelSplash by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val tv: TextView = findViewById(R.id.text_view)
        viewModel.state.observe(this, Observer {
            finishAffinity()
            AppComponent.providesState().navigateLaunchScreen()
        })
    }

    override fun onResume() {
        super.onResume()
        viewModel.launchSplash()
    }

}
