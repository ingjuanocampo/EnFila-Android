package com.ingjuanocampo.enfila.android.utils

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*

val handler = CoroutineExceptionHandler { _, exception ->
    Log.d("CoroutineExceptionHandler", "got $exception")
}

fun CoroutineScope.launchGeneral(function: suspend () -> Unit) {
    launch(handler + Dispatchers.Default) {
        withContext(Dispatchers.Default) {
            function()
        }
    }
}

fun ViewModel.launchGeneral(function: suspend () -> Unit) {
    viewModelScope.launch(handler + Dispatchers.Default) {
        withContext(Dispatchers.Default) {
            function()
        }
    }
}