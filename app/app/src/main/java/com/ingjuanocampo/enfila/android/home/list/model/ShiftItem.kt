package com.ingjuanocampo.enfila.android.home.list.model

import com.ingjuanocampo.cdapter.RecyclerViewType
import com.ingjuanocampo.enfila.android.utils.ViewTypes
import com.ingjuanocampo.enfila.domain.entity.ShiftState
import com.ingjuanocampo.enfila.domain.entity.getNow
import com.ingjuanocampo.enfila.domain.usecases.model.ShiftWithClient
import java.text.SimpleDateFormat
import java.util.*


data class ShiftItem(val id: String,
                     val phone: String,
                     val name: String,
                     val currentTurn: String,
                     val issueDate: Long,
                     val endDate: Long,
                     val state: String,
                     val viewType: ViewTypes = ViewTypes.SHIFT,
                     val isCancellable: Boolean = false
): RecyclerViewType {

    fun geElapsedTime(): Long {
        val current = getNow()
        return current - issueDate
    }

    fun geEndElapsedTime(): Long {
        return endDate - issueDate
    }

    fun getStringIssueDate(): String {
        return SimpleDateFormat("EEE, MMM dd, yyyy H:mm").format(Date(issueDate))
    }

    fun getTotalElapsedTime(): String {
        var diff = geEndElapsedTime()

        val day = 60 * 60 * 24
        val hour = 60 * 60
        val minute = 60
        val numOfDays = (diff / (day)).toInt()
        diff %= day

        val hours = (diff / (hour)).toInt()
        diff %= hour

        val minutes = (diff / (minute)).toInt()
        diff %= minute

        val seconds = (diff).toInt()
        return "$hours:$minutes:$seconds"
    }

    override fun getDelegateId(): Int = id.hashCode()

    override fun getViewType(): Int = viewType.ordinal
}


fun ShiftWithClient.mapToUI(viewType: ViewTypes = ViewTypes.SHIFT): ShiftItem {
    return ShiftItem(
        id = this.shift.id,
        name = this.client.name ?: "",
        phone = this.client.id ?: "",
        currentTurn = this.shift.number.toString(),
        issueDate = this.shift.date ?: 0L,
        state = this.shift.state.name,
        endDate = this.shift.endDate ?: 0L,
        viewType = viewType,
        isCancellable = this.shift.state == ShiftState.CALLING

    )
}