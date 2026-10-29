package com.ingjuanocampo.enfila.domain.usecases

import com.ingjuanocampo.enfila.domain.entity.Shift
import com.ingjuanocampo.enfila.domain.entity.ShiftState
import com.ingjuanocampo.enfila.domain.usecases.model.ClientDetails
import com.ingjuanocampo.enfila.domain.usecases.repository.ClientRepository
import com.ingjuanocampo.enfila.domain.usecases.repository.ShiftRepository
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@ViewModelScoped
class LoadClientDetailsUC
    @Inject
    constructor(
        private val clientRepository: ClientRepository,
        private val shiftRepository: ShiftRepository,
    ) {
        operator fun invoke(clientId: String): Flow<ClientDetails?> {
            return combine(
                flow { emit(clientRepository.loadById(clientId)) }.filterNotNull(),
                shiftRepository.getAllObserveData().filterNotNull(),
            ) { client, allShifts ->
                val clientShifts =
                    allShifts.filter { shift ->
                        shift.contactId == client.id
                    }.sortedByDescending { it.date }
                val finished = clientShifts.filter { it.state == ShiftState.FINISHED }

                ClientDetails(
                    client = client,
                    shifts = clientShifts,
                    totalShifts = clientShifts.size,
                    activeShifts = clientShifts.count { it.state == ShiftState.WAITING || it.state == ShiftState.CALLING },
                    averageWaitTime = calculateAverageWaitTime(clientShifts),
                    orders = finished.size,
                    cancels = clientShifts.count { it.state == ShiftState.CANCELLED },
                    lastVisit = finished.maxOfOrNull { it.date },
                    usualRequest = mostCommon(
                        finished.mapNotNull { shift ->
                            shift.notes?.trim()?.takeIf { it.isNotEmpty() }?.let { note -> note to shift.date }
                        },
                    ),
                    mostVisitedStoreId = mostCommon(finished.map { it.parentCompanySite to it.date }),
                )
            }
        }

        private fun <T> mostCommon(items: List<Pair<T, Long>>): T? {
            if (items.isEmpty()) return null
            val grouped = items.groupBy { it.first }
            val maxCount = grouped.values.maxOf { it.size }
            return grouped
                .filter { it.value.size == maxCount }
                .maxBy { group -> group.value.maxOf { it.second } }
                .key
        }

        private fun calculateAverageWaitTime(shifts: List<Shift>): Long {
            val completedShifts =
                shifts.filter {
                    it.attentionStartDate != null && it.endDate != null
                }
            return if (completedShifts.isNotEmpty()) {
                completedShifts.map {
                    (it.endDate ?: 0) - it.date
                }.average().toLong()
            } else {
                0L
            }
        }
    }
