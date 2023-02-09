package com.ingjuanocampo.enfila.android.login

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.ingjuanocampo.enfila.android.R
import com.ingjuanocampo.enfila.android.login.viewmodel.ViewModelLogin
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ActivityLogin: AppCompatActivity() {

    val viewModel: ViewModelLogin by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.state.observe(this, Observer {

        })
        setContentView(R.layout.activity_login)

    }

}