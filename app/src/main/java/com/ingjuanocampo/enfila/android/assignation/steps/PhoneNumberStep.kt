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
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ingjuanocampo.enfila.android.assignation.viewmodel.AssignationState
import com.ingjuanocampo.enfila.android.assignation.viewmodel.ViewModelAssignation
import com.ingjuanocampo.enfila.android.ui.theme.AppTheme
import com.ingjuanocampo.enfila.android.ui.theme.ButtonPrimary
import kotlinx.coroutines.flow.MutableStateFlow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PhoneNumberStep(
    viewModel: ViewModelAssignation,
    onNext: () -> Unit,
) {
    val assignationState by viewModel.assignationState.observeAsState(AssignationState.IDLE)
    val keyboardController = LocalSoftwareKeyboardController.current
    var phoneText by remember { mutableStateOf(viewModel.phoneNumber) }

    PhoneNumberStepContent(
        phoneText = phoneText,
        assignationState = assignationState,
        onPhoneChange = { newValue ->
            if (newValue.length <= 10 && newValue.all { it.isDigit() }) {
                phoneText = newValue
                viewModel.phoneNumber = newValue
            }
        },
        onNext = {
            keyboardController?.hide()
            // Phone validation is handled automatically in the setter
            onNext()
        },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PhoneNumberStepContent(
    phoneText: String,
    assignationState: AssignationState,
    onPhoneChange: (String) -> Unit,
    onNext: () -> Unit,
) {
    val canContinue = phoneText.length >= 10
    val isLoading = assignationState == AssignationState.Loading

    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(24.dp)
                .imePadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
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
                        imageVector = Icons.Filled.Phone,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.size(40.dp),
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "Enter Client's Phone Number",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center,
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "We'll check if this client exists in our system",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                )
            }
        }

        Spacer(modifier = Modifier.height(40.dp))

        // Phone input field
        AnimatedVisibility(
            visible = true,
            enter = fadeIn(tween(500, delayMillis = 200)) + slideInHorizontally(),
        ) {
            OutlinedTextField(
                value = phoneText,
                onValueChange = onPhoneChange,
                label = { Text("Phone Number") },
                placeholder = { Text("3137550993") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Phone,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                },
                keyboardOptions =
                    KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done,
                    ),
                keyboardActions =
                    KeyboardActions(
                        onDone = {
                            if (canContinue) onNext()
                        },
                    ),
                modifier = Modifier.fillMaxWidth(),
                colors =
                    TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                    ),
                enabled = !isLoading,
            )
        }

        Spacer(modifier = Modifier.height(40.dp))

        // Action button
        androidx.compose.animation.AnimatedVisibility(
            visible = true,
            enter = fadeIn(tween(500, delayMillis = 400)),
        ) {
            val buttonState = remember { MutableStateFlow(canContinue && !isLoading) }

            // Update button state when conditions change
            buttonState.value = canContinue && !isLoading

            ButtonPrimary(
                onClick = onNext,
                text = if (isLoading) "Validating..." else "Continue",
                modifier = Modifier.fillMaxWidth(),
                enableState = buttonState,
            )
        }
    }
}

@Preview(
    showSystemUi = true,
    device = Devices.PIXEL_XL,
)
@Composable
fun PhoneNumberStepContentPreview() {
    AppTheme {
        PhoneNumberStepContent(
            phoneText = "3137550993",
            assignationState = AssignationState.IDLE,
            onPhoneChange = {},
            onNext = {},
        )
    }
}

@Preview(
    showSystemUi = true,
    device = Devices.PIXEL_XL,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
)
@Composable
fun PhoneNumberStepContentPreviewDark() {
    AppTheme {
        PhoneNumberStepContent(
            phoneText = "313",
            assignationState = AssignationState.Loading,
            onPhoneChange = {},
            onNext = {},
        )
    }
}

@Preview(
    showSystemUi = true,
    device = Devices.PIXEL_XL,
)
@Composable
fun PhoneNumberStepContentEmptyPreview() {
    AppTheme {
        PhoneNumberStepContent(
            phoneText = "",
            assignationState = AssignationState.IDLE,
            onPhoneChange = {},
            onNext = {},
        )
    }
}
