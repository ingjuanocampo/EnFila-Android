package com.ingjuanocampo.enfila.android.home.account.model

data class AccountCard(
    val companyName: String = "",
    val phone: String = "",
    val email: String = "",
    val numberClients: String = "",
    val totalShifts: String = "",
    val shiftByDay: String = "",
    val clientsByDay: String = "",
    val waitingTime: String = "",
    val attentionTime: String = "",
    val options: List<OptionCard> = emptyList(),
    val loadingLogout: Boolean = false,
    // New fields for enhanced statistics
    val avgShiftsPerWeek: String = "",
    val peakHours: String = "",
    val customerSatisfaction: String = "",
    val monthlyGrowth: String = "",
    val totalRevenue: String = "",
    val activeClientsToday: String = "",
) {
    fun buildSections(): List<StatisticsSectionUI> {
        return listOf(
            StatisticsSectionUI(
                "Total Turns",
                totalShifts,
                "Total Clients",
                numberClients,
                StatisticsType.COUNTER
            ),
            StatisticsSectionUI(
                "Daily Shifts",
                shiftByDay,
                "Daily Clients",
                clientsByDay,
                StatisticsType.DAILY
            ),
            StatisticsSectionUI(
                "Waiting Time",
                waitingTime,
                "Attention Time",
                attentionTime,
                StatisticsType.TIME
            ),
            StatisticsSectionUI(
                "Weekly Average",
                avgShiftsPerWeek,
                "Peak Hours",
                peakHours,
                StatisticsType.TREND
            )
        )
    }

    fun buildDashboardMetrics(): List<DashboardMetric> {
        return listOf(
            DashboardMetric(
                title = "Active Today",
                value = clientsByDay,
                subtitle = "clients by day",
                type = MetricType.ACTIVE_STATUS,
                trend = TrendDirection.NEUTRAL
            ),
            DashboardMetric(
                title = "Monthly Growth",
                value = monthlyGrowth,
                subtitle = "vs last month",
                type = MetricType.GROWTH,
                trend = if (monthlyGrowth.contains("+")) TrendDirection.UP else TrendDirection.DOWN
            ),
            DashboardMetric(
                title = "Satisfaction",
                value = customerSatisfaction,
                subtitle = "customer rating",
                type = MetricType.RATING,
                trend = TrendDirection.UP
            ),
            DashboardMetric(
                title = "Efficiency",
                value = attentionTime,
                subtitle = "avg service time",
                type = MetricType.TIME,
                trend = TrendDirection.NEUTRAL
            )
        )
    }
}

data class OptionCard(
    val icon: Int,
    val title: String,
    val subtitle: String = "",
    val callback: () -> Unit
)

data class DashboardMetric(
    val title: String,
    val value: String,
    val subtitle: String,
    val type: MetricType,
    val trend: TrendDirection
)

enum class MetricType {
    ACTIVE_STATUS,
    GROWTH,
    RATING,
    TIME,
    COUNTER
}

enum class TrendDirection {
    UP,
    DOWN,
    NEUTRAL
}

enum class StatisticsType {
    COUNTER,
    DAILY,
    TIME,
    TREND,
    PERFORMANCE
}
