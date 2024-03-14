package com.ingjuanocampo.common.composable

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.ui.graphics.vector.ImageVector

interface ViewEffect

object FinishEffect : ViewEffect

object BackEffect : ViewEffect

object LogoutOut : ViewEffect

data class UpdateToolbarTitle(val title: String) : ViewEffect

data class ShowErrorDialogEffect(
    val title: String,
    val description: String,
    val icon: ImageVector = Icons.Rounded.Warning,
) : ViewEffect

object IDLE : ViewEffect

object InvalidateMenuOptions : ViewEffect

data class NavigationEffect(
    @IdRes val id: Int,
    val label: String,
    val fragment: String,
    val bundle: Bundle? = null,
) : ViewEffect
