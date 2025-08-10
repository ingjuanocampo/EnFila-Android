package com.ingjuanocampo.enfila.android.utils

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ingjuanocampo.common.composable.MviBaseViewModel
import com.ingjuanocampo.common.composable.ShowErrorDialogEffect
import com.ingjuanocampo.enfila.android.AppEnFila
import kotlinx.coroutines.*

val handler =
    CoroutineExceptionHandler { _, exception ->
        Log.e("GeneralError", "got ${exception.printStackTrace()}")
        GlobalScope.launch(Dispatchers.Main) { // Bad practice just for testing
            Toast.makeText(AppEnFila.context, "Something went wrong, please try later", Toast.LENGTH_LONG).show()
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

fun MviBaseViewModel<*>.launchGeneralWithErrorHandling(function: suspend () -> Unit) {
    viewModelScope.launch(
        CoroutineExceptionHandler { _, exception ->
            _event.tryEmit(
                ShowErrorDialogEffect(
                    title = "Something went wrong",
                    description = "Please try later"
                )
            )
        } + Dispatchers.Default
    ) {
        function()
    }
}
