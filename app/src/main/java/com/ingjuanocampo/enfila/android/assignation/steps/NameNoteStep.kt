package com.ingjuanocampo.enfila.android.assignation.steps

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ingjuanocampo.enfila.android.assignation.viewmodel.ViewModelAssignation
import com.ingjuanocampo.enfila.android.ui.theme.AppTheme
import com.ingjuanocampo.enfila.android.ui.theme.ButtonPrimary
import kotlinx.coroutines.flow.MutableStateFlow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NameNoteStep(
    viewModel: ViewModelAssignation,
    onNext: () -> Unit,
    onBack: () -> Unit,
) {
    val focusManager = LocalFocusManager.current
    var nameText by remember { mutableStateOf(viewModel.name) }
    var noteText by remember { mutableStateOf(viewModel.note) }

    NameNoteStepContent(
        nameText = nameText,
        noteText = noteText,
        onNameChange = {
            nameText = it
            viewModel.name = it
        },
        onNoteChange = {
            noteText = it
            viewModel.note = it
        },
        onNext = {
            focusManager.clearFocus()
            // Name and note are handled automatically in the setters
            onNext()
        },
        onBack = onBack,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NameNoteStepContent(
    nameText: String,
    noteText: String,
    onNameChange: (String) -> Unit,
    onNoteChange: (String) -> Unit,
    onNext: () -> Unit,
    onBack: () -> Unit,
) {
    val nameFocusRequester = remember { FocusRequester() }
    val canContinue = nameText.isNotEmpty()

    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(24.dp)
                .imePadding()
                .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.height(32.dp))

        AnimatedVisibility(
            visible = true,
            enter = fadeIn(tween(500)) + scaleIn(tween(500)),
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                // Icon
                Box(
                    modifier =
                        Modifier
                            .size(80.dp)
                            .background(
                                MaterialTheme.colorScheme.primaryContainer,
                                CircleShape,
                            ),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        imageVector = Icons.Filled.Person,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.size(40.dp),
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "Client Information",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center,
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Please enter the client's name and any additional notes",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                )
            }
        }

        Spacer(modifier = Modifier.height(40.dp))

        // Name field
        AnimatedVisibility(
            visible = true,
            enter = fadeIn(tween(500, delayMillis = 200)) + slideInHorizontally(),
        ) {
            OutlinedTextField(
                value = nameText,
                onValueChange = onNameChange,
                label = { Text("Client Name") },
                placeholder = { Text("Enter client name") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Person,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                },
                keyboardOptions =
                    KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next,
                    ),
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .focusRequester(nameFocusRequester),
                colors =
                    TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                    ),
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Notes field
        AnimatedVisibility(
            visible = true,
            enter = fadeIn(tween(500, delayMillis = 400)) + slideInHorizontally(),
        ) {
            OutlinedTextField(
                value = noteText,
                onValueChange = onNoteChange,
                label = { Text("Notes (Optional)") },
                placeholder = { Text("Any additional information...") },
                keyboardOptions =
                    KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Done,
                    ),
                modifier = Modifier.fillMaxWidth(),
                minLines = 3,
                maxLines = 5,
                colors =
                    TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                    ),
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Action buttons
        androidx.compose.animation.AnimatedVisibility(
            visible = true,
            enter = fadeIn(tween(500, delayMillis = 600)),
        ) {
            val backButtonState = remember { MutableStateFlow(true) }
            val nextButtonState = remember { MutableStateFlow(canContinue) }

            // Update button states
            nextButtonState.value = canContinue

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                ButtonPrimary(
                    onClick = onBack,
                    text = "Back",
                    modifier = Modifier.weight(1f),
                    enableState = backButtonState,
                )

                ButtonPrimary(
                    onClick = onNext,
                    text = "Continue",
                    modifier = Modifier.weight(1f),
                    enableState = nextButtonState,
                )
            }
        }

        // Bottom spacer to ensure proper spacing when keyboard is visible
        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Preview(
    showSystemUi = true,
    device = Devices.PIXEL_XL,
)
@Composable
fun NameNoteStepContentPreview() {
    AppTheme {
        NameNoteStepContent(
            nameText = "John Doe",
            noteText = "Regular customer, prefers window seat",
            onNameChange = {},
            onNoteChange = {},
            onNext = {},
            onBack = {},
        )
    }
}

@Preview(
    showSystemUi = true,
    device = Devices.PIXEL_XL,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
)
@Composable
fun NameNoteStepContentPreviewDark() {
    AppTheme {
        NameNoteStepContent(
            nameText = "",
            noteText = "",
            onNameChange = {},
            onNoteChange = {},
            onNext = {},
            onBack = {},
        )
    }
}

@Preview(
    showSystemUi = true,
    device = Devices.PIXEL_XL,
)
@Composable
fun NameNoteStepContentFilledPreview() {
    AppTheme {
        NameNoteStepContent(
            nameText = "Maria Garcia",
            noteText = "",
            onNameChange = {},
            onNoteChange = {},
            onNext = {},
            onBack = {},
        )
    }
}
