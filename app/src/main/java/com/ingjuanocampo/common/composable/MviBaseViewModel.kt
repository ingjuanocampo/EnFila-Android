package com.ingjuanocampo.common.composable

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow

abstract class MviBaseViewModel<STATE>(
    initialState: STATE
) : ViewModel() {
    protected val _state = MutableStateFlow<STATE>(initialState)
    val state: StateFlow<STATE> = _state.asStateFlow()

    val _event =
        MutableSharedFlow<ViewEffect>(
            extraBufferCapacity = 1,
            onBufferOverflow = BufferOverflow.DROP_OLDEST
        )

    val event: SharedFlow<ViewEffect> = _event.asSharedFlow()

    open fun setState(block: STATE.() -> STATE) {
        _state.value = block.invoke(_state.value)
    }
}
