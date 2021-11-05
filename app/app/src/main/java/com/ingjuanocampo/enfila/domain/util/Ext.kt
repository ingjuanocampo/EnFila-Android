package com.ingjuanocampo.enfila.domain.util

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

fun <T> MutableSharedFlow<T>.emitInContext(data: T) {
    val shareFlow = this
    GlobalScope.launch {
        shareFlow.emit(data)
    }
}
