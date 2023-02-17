package com.ingjuanocampo.enfila.android.utils

import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Chronometer
import androidx.core.view.isVisible
import com.ingjuanocampo.enfila.android.databinding.ViewActiveProgressBinding
import com.ingjuanocampo.enfila.android.home.list.model.ShiftItem
import com.ingjuanocampo.enfila.domain.entity.ShiftState
import android.widget.Chronometer.OnChronometerTickListener
import java.util.concurrent.TimeUnit


fun ViewGroup.inflate(layout: Int): View {
    return LayoutInflater.from(context).inflate(layout, this, false)
}

fun Chronometer.set(shiftItem: ShiftItem) {
    if (shiftItem.state == ShiftState.WAITING.name) {
        this.base = SystemClock.elapsedRealtime() - shiftItem.geElapsedTime()
        this.start()
        onChronometerTickListener =
            OnChronometerTickListener { chronometer ->
                var t = SystemClock.elapsedRealtime() - chronometer.base

                chronometer.text = t.toDurationText()
            }

    } else {
        this.stop()
        this.text = "0"
    }
}

fun Long.toDurationText(): String {

    var t = this
    val days = TimeUnit.MILLISECONDS.toDays(t)
    t -= TimeUnit.DAYS.toMillis(days)

    val hours = TimeUnit.MILLISECONDS.toHours(t)
    t -= TimeUnit.HOURS.toMillis(hours)

    val minutes = TimeUnit.MILLISECONDS.toMinutes(t)
    t -= TimeUnit.MINUTES.toMillis(minutes)

    val seconds = TimeUnit.MILLISECONDS.toSeconds(t)

    val text =if (days >= 1) {
        "${days.completeZero()} Dias ${hours.completeZero()}:${minutes.completeZero()}:${seconds.completeZero()}"

    } else {
        "${hours.completeZero()}:${minutes.completeZero()}:${seconds.completeZero()}"
    }

    return text

}

fun Long.completeZero(): String {
    return if (this < 10) "0$this" else this.toString()
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