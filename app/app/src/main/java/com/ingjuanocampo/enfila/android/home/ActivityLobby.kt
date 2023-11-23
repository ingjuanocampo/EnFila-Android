package com.ingjuanocampo.enfila.android.home

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomnavigation.LabelVisibilityMode.LABEL_VISIBILITY_SELECTED
import com.ingjuanocampo.enfila.android.R
import com.ingjuanocampo.enfila.android.home.clients.FragmentClientList
import com.ingjuanocampo.enfila.android.home.home.FragmentHome
import com.ingjuanocampo.enfila.android.home.profile.FragmentProfile
import com.ingjuanocampo.enfila.android.home.shift_pager.FragmentShiftPager
import com.ingjuanocampo.enfila.android.home.tips.FragmentTips
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ActivityLobby : AppCompatActivity() {

  private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        navController = findNavController(R.id.nav_host_fragment)
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNav)
        bottomNavigationView.setupWithNavController(navController)

        val appBarConfiguration = AppBarConfiguration(navController.graph)
        findViewById<Toolbar>(R.id.toolbarWidget)
            .setupWithNavController(navController, appBarConfiguration)
    }
}
