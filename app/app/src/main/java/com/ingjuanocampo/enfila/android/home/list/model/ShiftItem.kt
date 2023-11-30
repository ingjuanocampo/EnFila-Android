package com.ingjuanocampo.enfila.android.home.list.model

import android.content.Context
import com.ingjuanocampo.cdapter.RecyclerViewType
import com.ingjuanocampo.enfila.android.R
import com.ingjuanocampo.enfila.android.utils.ViewTypes
import com.ingjuanocampo.enfila.android.utils.toDurationText
import com.ingjuanocampo.enfila.domain.entity.ShiftState
import com.ingjuanocampo.enfila.domain.entity.getAttentionTime
import com.ingjuanocampo.enfila.domain.entity.getNow
import com.ingjuanocampo.enfila.domain.usecases.model.ShiftWithClient
import java.text.SimpleDateFormat
import java.util.Date

data class ShiftItem(
    val id: String = "",
    val phone: String = "",
    val name: String = "",
    val currentTurn: String = "",
    val issueDate: Long = 0L,
    val endDate: Long = 0L,
    val state: String = "",
    val viewType: ViewTypes = ViewTypes.SHIFT,
    val isCancellable: Boolean = false,
    val scheduledHour: String = "", // Not being mapped yet
    val notes: String = "",
    val attentionTime: String = "",
    val waitTime: String = "",
    val formmattedIssueDate: String = "",
    val isProcessingActions: Boolean = false,
    val visualStatus: String = "",
) : RecyclerViewType {

    fun geElapsedTime(): Long {
        val current = getNow()
        return current - issueDate
    }

    /* fun getTotalElapsedTime(): String {
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
     }*/

    override fun getDelegateId(): Int = id.hashCode()

    override fun getViewType(): Int = viewType.ordinal
}

fun ShiftWithClient.mapToUI(viewType: ViewTypes = ViewTypes.SHIFT): ShiftItem {

    return ShiftItem(
        id = this.shift.id,
        name = this.client.name ?: "",
        phone = this.client.id,
        currentTurn = this.shift.number.toString(),
        issueDate = this.shift.date,
        state = this.shift.state.name,
        endDate = this.shift.endDate ?: 0L,
        viewType = viewType,
        isCancellable = this.shift.state == ShiftState.CALLING,
        notes = this.shift.notes.orEmpty(),
        attentionTime = this.shift.getAttentionTime(),
        waitTime = ((getNow() - this.shift.date)).toDurationText(),
        formmattedIssueDate = SimpleDateFormat("EEE, MMM dd, yyyy H:mm").format(Date(this.shift.date)),
    )
}

fun ShiftItem.getVisualStatus(context: Context) = when (this.state.toUpperCase()) {
        ShiftState.WAITING.name -> context.getString(R.string.watting)
        ShiftState.CALLING.name -> context.getString(R.string.calling)
        ShiftState.CANCELLED.name-> context.getString(R.string.cancelled)
        ShiftState.FINISHED.name -> context.getString(R.string.finished)
    else -> { "" }

}

