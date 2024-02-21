package com.ingjuanocampo.enfila.commons

import java.text.SimpleDateFormat
import java.util.*

fun Long.toYearMonthFormat(): String =
    this.toFormatDate("yyyy/MM")

fun Long.toYearMonthDayFormat(): String =
    this.toFormatDate("yyyy/MM/dd")

fun Long.toFormatDate(format: String): String =
    SimpleDateFormat(format).format(Date(this))
