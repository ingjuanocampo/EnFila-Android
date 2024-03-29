package com.ingjuanocampo.enfila.domain.entity

import com.ingjuanocampo.enfila.android.utils.toDurationText
import com.ingjuanocampo.enfila.domain.util.EMPTY_STRING
import java.util.*

data class Shift internal constructor(
    val date: Long, // Create date
    override val id: String = EMPTY_STRING,
    val parentCompanySite: String,
    var number: Int = 0,
    val contactId: String,
    val notes: String?,
    var state: ShiftState,
    var attentionStartDate: Long? = null,
    var endDate: Long? = null,
) : Comparable<Shift>, IdentifyObject {

    override fun compareTo(other: Shift): Int {
        val numberCompare = this.number.compareTo(other.number)
        if (numberCompare == 0) {
            return state.compareTo(other.state)
        }
        return numberCompare
    }

    fun getDiffTime(): Long {
        val current = getNow()
        return current - date
    }
}

fun Shift.getAttentionTime(): String = try {
    endDate!!.minus(attentionStartDate!!).toDurationText()
} catch (e: Exception) { "" }

fun getShiftState(value: Int?): ShiftState {
    return ShiftState.values().firstOrNull { it.ordinal == value ?: 0 } ?: ShiftState.WAITING
}

object ShiftFactory {

    fun createWaiting(
        number: Int,
        contactId: String,
        notes: String,
        currentCompanySiteId: String,
    ): Shift {
        val instantNow = getNow()
        return Shift(
            date = instantNow,
            id = number.toString() + currentCompanySiteId,
            parentCompanySite = currentCompanySiteId,
            number = number,
            contactId = contactId,
            notes = notes,
            state = ShiftState.WAITING,
        )
    }
}

fun getNow(): Long {
    return Date().time
}
