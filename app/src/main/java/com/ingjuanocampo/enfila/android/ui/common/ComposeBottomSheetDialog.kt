package com.ingjuanocampo.enfila.android.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ingjuanocampo.common.composable.ShowErrorDialogEffect
import com.ingjuanocampo.enfila.android.ui.theme.AppTheme
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

@OptIn(
    ExperimentalMaterial3Api::class,
    DelicateCoroutinesApi::class
)
@Composable
fun ComposeBottomSheetDialog(showDialog: MutableStateFlow<ShowErrorDialogEffect?>) {
    val showErrorDialog by showDialog.collectAsState()
    showErrorDialog?.let { info ->
        AppTheme {
            val bottomSheetScaffoldState =
                rememberBottomSheetScaffoldState(
                    bottomSheetState =
                    SheetState(
                        skipPartiallyExpanded = false,
                        density = LocalDensity.current,
                        initialValue = SheetValue.Expanded
                    )
                )
            val scope = rememberCoroutineScope()

            LaunchedEffect(bottomSheetScaffoldState) {
                snapshotFlow { bottomSheetScaffoldState.bottomSheetState.isVisible }.collect { isVisible ->
                    if (isVisible.not()) {
                        showDialog.emit(null)
                    }
                }
            }
            Box(modifier = Modifier.background(Color.Black.copy(alpha = 0.6f)).fillMaxSize())
            BottomSheetScaffold(
                scaffoldState = bottomSheetScaffoldState,
                sheetContent = {
                    BottomSheetContent(
                        info.title,
                        info.description,
                        info.icon
                    ) {
                        scope.launch {
                            bottomSheetScaffoldState.bottomSheetState.hide()
                        }
                    }
                },
                content = {
                }
            )
        }
    }
}

@Composable
fun BottomSheetContent(
    title: String,
    description: String,
    icon: ImageVector = Icons.Rounded.Warning,
    onDismissClick: () -> Unit
) {
    Column(
        modifier =
        Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier =
            Modifier
                .size(48.dp)
                .padding(bottom = 16.dp),
            tint = MaterialTheme.colorScheme.error
        )

        Text(
            text = description,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Button(
            onClick = {
                onDismissClick()
            },
            modifier =
            Modifier
                .align(Alignment.CenterHorizontally)
                .fillMaxWidth()
        ) {
            Text(text = "Dismiss")
        }
    }
}

@Preview
@Composable
fun BottomSheetDialogExamplePreview() {
    AppTheme {
        ComposeBottomSheetDialog(
            MutableStateFlow(
                ShowErrorDialogEffect(
                    "Title",
                    "Description",
                    Icons.Filled.Warning
                )
            )
        )
    }
}
