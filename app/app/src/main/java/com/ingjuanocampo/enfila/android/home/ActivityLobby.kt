package com.ingjuanocampo.enfila.android.home

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomnavigation.LabelVisibilityMode.LABEL_VISIBILITY_SELECTED
import com.ingjuanocampo.enfila.android.R
import com.ingjuanocampo.enfila.android.home.history.FragmentHistory
import com.ingjuanocampo.enfila.android.home.home.FragmentHome
import com.ingjuanocampo.enfila.android.home.list.FragmentListItems
import com.ingjuanocampo.enfila.android.home.profile.FragmentProfile
import com.ingjuanocampo.enfila.android.home.tips.FragmentTips
import com.ingjuanocampo.enfila.android.menu.BottomMenuBuilder
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ActivityLobby : AppCompatActivity() {

    private val bottomNavBuilder by lazy {
        BottomMenuBuilder()
            .appendItem(fragmentFactory = { FragmentTips.newInstance() }, icon = getDrawable(R.drawable.ic_tips_and_updates), title = "Tips")
            .appendItem(fragmentFactory = { FragmentListItems.newInstance() }, icon = getDrawable(R.drawable.ic_format_list), title = "Turnos")
            .appendItem(fragmentFactory = { FragmentHome.newInstance() }, icon = getDrawable(R.drawable.ic_home), title = "Panel", default = true)
            .appendItem(fragmentFactory = { FragmentHistory.newInstance() }, icon = getDrawable(R.drawable.ic_history), title = "Historial")
            .appendItem(fragmentFactory = { FragmentProfile.newInstance() }, icon = getDrawable(R.drawable.ic_account), title = "Profile")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        val toolbar = findViewById<Toolbar>(R.id.toolbarWidget)
        setSupportActionBar(toolbar)
        var bottomNav: BottomNavigationView = findViewById(R.id.bottomNav)
        bottomNav.labelVisibilityMode = LABEL_VISIBILITY_SELECTED
        bottomNavBuilder.attachMenu(bottomNav, this)
    }
}
