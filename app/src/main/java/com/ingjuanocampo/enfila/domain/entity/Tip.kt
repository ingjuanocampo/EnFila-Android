package com.ingjuanocampo.enfila.domain.entity

enum class TipMilestone {
    ON_LOGIN,
    FIRST_SHIFT_ASSIGNED,
    FIRST_SHIFT_CALLED,
    FIRST_SHIFT_COMPLETED,
    HAS_CLIENTS,
    PROFILE_COMPLETE,
}

data class Tip(
    val id: String,
    val order: Int,
    val question: String,
    val answer: String,
    val milestone: TipMilestone,
    val isUnlocked: Boolean,
)

fun TipMilestone.unlockHint(): String = when (this) {
    TipMilestone.ON_LOGIN -> "Sign in to unlock this tip."
    TipMilestone.FIRST_SHIFT_ASSIGNED -> "Assign your first turn to unlock this tip."
    TipMilestone.FIRST_SHIFT_CALLED -> "Call a turn to unlock this tip."
    TipMilestone.FIRST_SHIFT_COMPLETED -> "Finish or cancel a turn to unlock this tip."
    TipMilestone.HAS_CLIENTS -> "Add a client to unlock this tip."
    TipMilestone.PROFILE_COMPLETE -> "Complete your profile and company info to unlock this tip."
}
