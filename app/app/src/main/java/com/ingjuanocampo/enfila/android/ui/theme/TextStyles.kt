package com.ingjuanocampo.enfila.android.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

val TextColor: Color
    @Composable
    get() = MaterialTheme.colorScheme.onPrimaryContainer

val TextStyleRoboto: TextStyle
    @Composable
    get() = TextStyle(
        fontFamily = FontFamily.SansSerif,
        color = TextColor,
    )

val Body3: TextStyle
    @Composable
    get() = TextStyleRoboto.copy(
        fontSize = 16.sp,
        fontWeight = FontWeight.Medium,
        fontStyle = FontStyle(FontStyle.Normal.value),
    )

val Body1: TextStyle
    @Composable
    get() = TextStyleRoboto.copy(
        fontSize = 14.sp,
        color = TextColor,
    )

val TitleStyle: TextStyle
    @Composable
    get() = TextStyleRoboto.copy(
        fontSize = 20.sp,
    )

val Typography: Typography
    @Composable
    get() = Typography(
        titleMedium = Body3,
        bodyMedium = Body1,
        titleLarge = TitleStyle,
    )
