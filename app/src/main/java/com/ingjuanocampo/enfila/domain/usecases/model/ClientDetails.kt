package com.ingjuanocampo.enfila.domain.usecases.model

import com.ingjuanocampo.enfila.domain.entity.Client
import com.ingjuanocampo.enfila.domain.entity.Shift

data class ClientDetails(
    val client: Client,
    val shifts: List<Shift>,
    val totalShifts: Int,
    val activeShifts: Int,
    val averageWaitTime: Long,
    val orders: Int = 0,
    val cancels: Int = 0,
    val lastVisit: Long? = null,
    val usualRequest: String? = null,
    val mostVisitedStoreId: String? = null,
) {
    val phoneNumber: String get() = client.id
    val clientName: String get() = client.name ?: "Unknown Client"
    val hasShifts: Boolean get() = shifts.isNotEmpty()
    val completionRate: Float get() =
        if (totalShifts > 0) {
            (totalShifts - activeShifts).toFloat() / totalShifts
        } else {
            0f
        }
}
