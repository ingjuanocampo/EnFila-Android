package com.ingjuanocampo.enfila.android.assignation

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import com.ingjuanocampo.enfila.android.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ActivityAssignation: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_assignation)
    }
}