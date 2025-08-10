package com.ingjuanocampo.enfila.domain.usecases

import com.ingjuanocampo.enfila.android.utils.toDurationText
import com.ingjuanocampo.enfila.domain.entity.Shift
import com.ingjuanocampo.enfila.domain.entity.ShiftState
import dagger.Reusable
import javax.inject.Inject

@Reusable
class CalculateShiftAverageWaitTimes
    @Inject
    constructor() {
        operator fun invoke(shifts: List<Shift>): String {
            if (shifts.isEmpty()) return "- -"
            val shiftTimes =
                shifts.filter { it.state == ShiftState.FINISHED }
                    .filter { it.endDate != null && it.endDate!! >= 0 && it.endDate!! > it.date }.map {
                        return@map it.endDate!!.minus(it.date)
                    }
            var totalTimes = 0L
            shiftTimes.forEach {
                totalTimes += it
            }
            return if (totalTimes == 0L) {
                "- -"
            } else {
                (totalTimes / shiftTimes.size).toDurationText()
            }
        }

        fun attentionTime(shifts: List<Shift>): String {
            if (shifts.isEmpty()) return "- -"
            val shiftTimes =
                shifts.filter { it.state == ShiftState.FINISHED }
                    .filter {
                        it.endDate != null &&
                            it.endDate!! > 0 &&
                            it.attentionStartDate != null &&
                            it.attentionStartDate!! > 0 &&
                            it.endDate!! > it.attentionStartDate!!
                    }.map {
                        return@map it.endDate!!.minus(it.attentionStartDate!!)
                    }
            var totalTimes = 0L
            shiftTimes.forEach {
                totalTimes += it
            }

            return if (totalTimes == 0L) {
                "- -"
            } else {
                (totalTimes / shiftTimes.size).toDurationText()
            }
        }
    }
