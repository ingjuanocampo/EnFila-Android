package com.ingjuanocampo.enfila.android.utils

import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Chronometer
import androidx.core.view.isVisible
import com.ingjuanocampo.enfila.android.databinding.ViewActiveProgressBinding
import com.ingjuanocampo.enfila.android.lobby.list.model.ShiftItem
import com.ingjuanocampo.enfila.domain.entity.ShiftState

fun ViewGroup.inflate(layout: Int): View {
    return LayoutInflater.from(context).inflate(layout, this, false)
}

fun Chronometer.set(shiftItem: ShiftItem) {
    if (shiftItem.state == ShiftState.WAITING.name) {
        this.base = SystemClock.elapsedRealtime() - shiftItem.geElapsedTime()
        this.start()
    } else {
        this.stop()
        this.text = "0"
    }
}


fun ViewActiveProgressBinding.set(listener: (String) -> Unit, id: String) {
    setProgressVisible(false)
    this.finish.setOnClickListener {
        setProgressVisible(true)
        listener.invoke(id)
    }
}

fun ViewActiveProgressBinding.setProgressVisible(isProgressVisible: Boolean) {
    this.progress.isVisible = isProgressVisible
    this.stopButton.isVisible = !isProgressVisible
}