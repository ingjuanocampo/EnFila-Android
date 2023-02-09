package com.ingjuanocampo.enfila.android.splash

import android.os.Bundle
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.ingjuanocampo.enfila.android.R
import com.ingjuanocampo.enfila.domain.state.AppStateProvider
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {

    private val viewModel: ViewModelSplash by viewModels()

    @Inject
    lateinit var stateProvider: AppStateProvider

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val tv: TextView = findViewById(R.id.text_view)
        viewModel.state.observe(this, Observer {
            finishAffinity()
            stateProvider.provideCurrentState().navigateLaunchScreen()
        })
    }

    override fun onResume() {
        super.onResume()
        viewModel.launchSplash()
    }

}
