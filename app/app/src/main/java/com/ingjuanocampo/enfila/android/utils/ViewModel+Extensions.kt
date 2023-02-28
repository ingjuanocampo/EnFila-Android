package com.ingjuanocampo.enfila.android.utils

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ingjuanocampo.enfila.android.AppEnFila
import kotlinx.coroutines.*

val handler = CoroutineExceptionHandler { _, exception ->
    Log.e("GeneralError", "got $exception")
    GlobalScope.launch(Dispatchers.Main) { // Bad practice just for testing
        Toast.makeText(AppEnFila.context, "General Error ${exception.localizedMessage}", Toast.LENGTH_LONG).show()
    }
}

fun CoroutineScope.launchGeneral(function: suspend () -> Unit) {
    launch(handler + Dispatchers.Default) {
        function()
    }
}

fun ViewModel.launchGeneral(function: suspend () -> Unit) {
    viewModelScope.launch(handler + Dispatchers.Default) {
        function()
    }
}
