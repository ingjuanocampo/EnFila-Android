package com.ingjuanocampo.enfila.android.login

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.ingjuanocampo.enfila.android.R
import com.ingjuanocampo.enfila.android.login.viewmodel.ViewModelLogin
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ActivityLogin : AppCompatActivity() {
    val viewModel: ViewModelLogin by viewModels()

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.state.observe(
            this,
            Observer {
            }
        )
        setContentView(R.layout.activity_login)

        navController = findNavController(this, R.id.nav_host_fragment)
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        findViewById<Toolbar>(R.id.toolbarWidget)
            .setupWithNavController(navController, appBarConfiguration)
    }
}
