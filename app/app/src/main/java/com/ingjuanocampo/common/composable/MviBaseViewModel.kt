package com.ingjuanocampo.common.composable

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ingjuanocampo.enfila.android.utils.launchGeneral
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow


abstract class MviBaseViewModel<STATE>(
    initialState: STATE
) : ViewModel() {

    protected val _state = MutableStateFlow<STATE>(initialState)
    val state: StateFlow<STATE> = _state.asStateFlow()

    abstract suspend fun handleIntent(intent: Any)

    fun sendIntent(intent: Any) {
        viewModelScope.launchGeneral {
            handleIntent(intent)
        }
    }
}