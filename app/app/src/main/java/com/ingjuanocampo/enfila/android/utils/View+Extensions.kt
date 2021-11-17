package com.ingjuanocampo.enfila.android.utils

import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Chronometer
import com.ingjuanocampo.enfila.android.lobby.list.ShiftItem
import com.ingjuanocampo.enfila.domain.entity.ShiftState
import java.util.concurrent.TimeUnit

fun ViewGroup.inflate(layout: Int): View {
    return LayoutInflater.from(context).inflate(layout, this, false)
}

fun Chronometer.set(shiftItem: ShiftItem) {
    if (shiftItem.state == ShiftState.CALLING.name || shiftItem.state == ShiftState.WAITING.name) {
        this.base = SystemClock.elapsedRealtime() - TimeUnit.SECONDS.toMillis(shiftItem.geElapsedTime())
        this.start()
    } else {
        this.base = SystemClock.elapsedRealtime() - TimeUnit.SECONDS.toMillis(shiftItem.geEndElapsedTime())
        this.stop()
        this.text = shiftItem.getStringEndDate()
    }
}