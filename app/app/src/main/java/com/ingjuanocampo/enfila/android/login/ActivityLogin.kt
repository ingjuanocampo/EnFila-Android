package com.ingjuanocampo.enfila.android.login

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.ingjuanocampo.enfila.android.R
import com.ingjuanocampo.enfila.android.login.viewmodel.ViewModelLogin

class ActivityLogin: AppCompatActivity() {

    val viewModel by viewModels<ViewModelLogin> ()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.state.observe(this, Observer {

        })
        setContentView(R.layout.activity_login)

    }

}