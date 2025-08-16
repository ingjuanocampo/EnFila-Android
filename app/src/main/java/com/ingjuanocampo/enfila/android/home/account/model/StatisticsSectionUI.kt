package com.ingjuanocampo.enfila.android.home.account.model

data class StatisticsSectionUI(
    val title1: String,
    val value1: String,
    val title2: String,
    val value2: String,
    val type: StatisticsType = StatisticsType.COUNTER,
)
