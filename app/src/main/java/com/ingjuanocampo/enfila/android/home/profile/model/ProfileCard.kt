package com.ingjuanocampo.enfila.android.home.profile.model

data class ProfileCard(
    val companyName: String = "",
    val phone: String = "",
    val email: String = "",
    val numberClients: String = "",
    val totalShifts: String = "", // level 1
    val shiftByDay: String =  "",
    val clientsByDay: String = "", // level 3
    val waitingTime: String = "",
    val attentionTime: String = "",
    val options: List<OptionCard> = emptyList(),
    val loadingLogout: Boolean = false
) {

    fun buildSections(): List<StatisticsSectionUI> {
        return listOf(
            StatisticsSectionUI(
                "# Turns",
                totalShifts,
                "# Clients",
                numberClients,
            ),
            StatisticsSectionUI(
                "Shifts by day",
                shiftByDay,
                "Clients by day",
                clientsByDay,
            ),
            StatisticsSectionUI(
                "Waiting  time",
                waitingTime,
                "Attention time",
                attentionTime,
            ),
        )
    }
}

data class OptionCard(val icon: Int, val title: String, val callback: () -> Unit)
