package com.ingjuanocampo.enfila.android.assignation.viewmodel

sealed class AssignationState {
    object IDLE: AssignationState()
    object NumberSet: AssignationState()
    object NameAndNoteSet: AssignationState()
    object ErrorTurnAssigned: AssignationState()
    object AssignationSet: AssignationState()
}