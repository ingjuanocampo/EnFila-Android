package com.ingjuanocampo.common.composable

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ScreenshotMonitor
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ingjuanocampo.enfila.android.ui.theme.AppTheme

@Composable
fun GenericEmptyState(title: String, icon: ImageVector, modifier: Modifier = Modifier) {
    AppTheme {
        Column (
            modifier = modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center) {
            Icon(modifier = Modifier.size(65.dp), imageVector = icon,
                contentDescription = "", tint = MaterialTheme.colorScheme.onPrimaryContainer)
            Spacer(modifier = Modifier.size(20.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium),
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center)
        }
    }
}


@Preview(
    backgroundColor = 0XFFFFFF,
    showSystemUi = true,
    device = Devices.NEXUS_5,
)
@Composable
fun GenericEmptyPreview() {
    GenericEmptyState(title = "No info to show", icon =
    Icons.Default.ScreenshotMonitor
    )
}


@Preview(
    backgroundColor = 0X000000,
    showSystemUi = true,
    device = Devices.NEXUS_5,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun GenericEmptyPreviewDark() {
    GenericEmptyState(title = "No info to show", icon =
    Icons.Default.ScreenshotMonitor
    )
}