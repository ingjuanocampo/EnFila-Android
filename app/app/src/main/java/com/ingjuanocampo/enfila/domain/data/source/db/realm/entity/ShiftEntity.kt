package com.ingjuanocampo.enfila.domain.data.source.db.realm.entity

import com.ingjuanocampo.enfila.domain.entity.Shift
import com.ingjuanocampo.enfila.domain.entity.getShiftState
import com.ingjuanocampo.enfila.domain.util.EMPTY_STRING
import io.realm.RealmObject

class ShiftEntity: RealmObject {

    var date: Long = 0
    var id: String = EMPTY_STRING
    var parentCompanySite: String= EMPTY_STRING
    var number: Int = 0
    var contactId: String= EMPTY_STRING
    var notes: String= EMPTY_STRING
    var state: Int= 0
    var endDate: Long = 0
}

fun ShiftEntity.toModel(): Shift {
    return Shift(
        date = this.date,
        id = this.id,
        parentCompanySite = this.parentCompanySite,
        number = this.number,
        contactId = this.contactId,
        notes = this.notes,
        state = getShiftState(this.state)
    )
}


fun Shift.toEntity(): ShiftEntity {
    val model = this
    return ShiftEntity().apply {
        this.date = model.date
        id = model.id
        parentCompanySite = model.parentCompanySite
        number = model.number
        contactId = model.contactId
        notes = model.notes?: EMPTY_STRING
        state = model.state.ordinal
    }
}



