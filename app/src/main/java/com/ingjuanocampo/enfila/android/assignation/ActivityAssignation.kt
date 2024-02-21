package com.ingjuanocampo.enfila.android.assignation

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.ingjuanocampo.enfila.android.R
import com.ingjuanocampo.enfila.android.ui.common.hideKeyboard
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ActivityAssignation : AppCompatActivity() {

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_assignation)

        findViewById<View>(R.id.close).setOnClickListener {
            finish()
            hideKeyboard()
        }
    }
}
