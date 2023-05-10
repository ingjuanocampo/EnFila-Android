package com.ingjuanocampo.common.composable

interface ViewEffect

object FinishEffect: ViewEffect
object BackEffect: ViewEffect
object LogoutOut: ViewEffect
data class UpdateToolbarTitle(val title: String): ViewEffect

data class ShowErrorDialogEffect(val title: String, val description: String): ViewEffect

object IDLE: ViewEffect

object InvalidateMenuOptions: ViewEffect