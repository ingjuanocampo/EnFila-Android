package com.ingjuanocampo.enfila.data.backend.mappers

import com.enfila.data.backend.models.*
import com.ingjuanocampo.enfila.domain.entity.*

// User mapping
fun BackendUser.toDomainUser(): User = User(
    id = this.id,
    phone = this.phone,
    name = this.name,
    companyIds = this.companyIds
)

fun User.toCreateUserRequest(): CreateUserRequest = CreateUserRequest(
    phone = this.phone,
    name = this.name,
    companyIds = this.companyIds
)

// Client mapping
fun BackendClient.toDomainClient(): Client = Client(
    id = this.id,
    name = this.name,
    shifts = this.shifts
)

fun Client.toCreateClientRequest(): CreateClientRequest = CreateClientRequest(
    id = this.id,
    name = this.name
)

// Shift mapping
fun BackendShift.toDomainShift(): Shift = Shift(
    date = this.date,
    id = this.id,
    parentCompanySite = this.parentCompanySite,
    number = this.number,
    contactId = this.contactId,
    notes = this.notes,
    state = this.state.toDomainShiftState(),
    attentionStartDate = this.attentionStartDate,
    endDate = this.endDate
)

fun BackendShiftState.toDomainShiftState(): ShiftState = when (this) {
    BackendShiftState.WAITING -> ShiftState.WAITING
    BackendShiftState.CALLING -> ShiftState.CALLING
    BackendShiftState.CANCELLED -> ShiftState.CANCELLED
    BackendShiftState.FINISHED -> ShiftState.FINISHED
}

fun ShiftState.toBackendShiftState(): BackendShiftState = when (this) {
    ShiftState.WAITING -> BackendShiftState.WAITING
    ShiftState.CALLING -> BackendShiftState.CALLING
    ShiftState.CANCELLED -> BackendShiftState.CANCELLED
    ShiftState.FINISHED -> BackendShiftState.FINISHED
}

// Company Site mapping
fun BackendCompanySite.toDomainCompanySite(): CompanySite = CompanySite(
    id = this.id,
    name = this.name,
    shiftsIdList = this.shiftsIdList
)
