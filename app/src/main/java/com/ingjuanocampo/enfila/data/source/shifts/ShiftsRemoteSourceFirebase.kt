package com.ingjuanocampo.enfila.data.source.shifts

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.ingjuanocampo.enfila.data.source.companysite.companyInfoPath
import com.ingjuanocampo.enfila.data.util.fetchProcess
import com.ingjuanocampo.enfila.data.util.fetchProcessMultiples
import com.ingjuanocampo.enfila.data.util.uploadProcess
import com.ingjuanocampo.enfila.data.util.uploadProcessMultiples
import com.ingjuanocampo.enfila.domain.entity.Shift
import com.ingjuanocampo.enfila.domain.entity.getShiftState
import com.ingjuanocampo.enfila.domain.util.EMPTY_STRING
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ShiftsRemoteSourceFirebase
@Inject
constructor() {
    private val remote by lazy { Firebase.firestore }
    private val shiftPath = "shifts"

    fun fetchShiftsByCompanyId(companyId: String): Flow<List<Shift>?> {
        return remote.fetchProcessMultiples({
            mapToModel(it)
        }, getPath(companyId))
    }

    private fun mapToModel(it: Map<String, Any>) =
        Shift(
            date = it["date"] as Long? ?: 0L,
            id = it["id"] as String,
            parentCompanySite = it["parentCompanySite"] as String? ?: EMPTY_STRING,
            number = (it["number"] as Long? ?: 0).toInt(),
            contactId = it["contactId"] as String? ?: EMPTY_STRING,
            notes = it["notes"] as String? ?: EMPTY_STRING,
            state = getShiftState((it["state"] as Long? ?: 0).toInt()),
            endDate = it["endDate"] as Long? ?: 0,
            attentionStartDate = it["attentionStartDate"] as Long? ?: 0
        )

    fun fetchByShiftId(
        shiftId: String,
        companyId: String
    ): Flow<Shift?> {
        return remote.fetchProcess({
            mapToModel(it)
        }, getPath(companyId), shiftId)
    }

    private fun getPath(parentCompanySite: String) = "$companyInfoPath/$parentCompanySite/$shiftPath"

    fun updateData(data: Shift): Flow<Shift?> {
        return remote.uploadProcess({
            return@uploadProcess mapShift(it)
        }, data, getPath(data.parentCompanySite), data.id)
    }

    fun updateData(data: List<Shift>): Flow<List<Shift>?> {
        return remote.uploadProcessMultiples({
            return@uploadProcessMultiples mapShift(it)
        }, data, getPath(data.first().parentCompanySite))
    }

    private fun mapShift(it: Shift) =
        hashMapOf(
            "date" to it.date,
            "id" to it.id,
            "parentCompanySite" to it.parentCompanySite,
            "number" to it.number,
            "contactId" to it.contactId,
            "notes" to it.notes,
            "state" to it.state.ordinal,
            "endDate" to it.endDate,
            "attentionStartDate" to it.attentionStartDate
        )
}
