package com.ingjuanocampo.enfila.android.assignation

import android.content.res.Configuration
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ingjuanocampo.enfila.android.assignation.viewmodel.AssignationState
import com.ingjuanocampo.enfila.android.assignation.viewmodel.ViewModelAssignation
import com.ingjuanocampo.enfila.android.ui.theme.AppTheme
import com.ingjuanocampo.enfila.android.ui.theme.ButtonPrimary
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow

@Composable
fun AssignationComposable(
    viewModel: ViewModelAssignation,
    onClose: () -> Unit,
    onComplete: () -> Unit,
) {
    val assignationState by viewModel.assignationState.observeAsState(AssignationState.IDLE)
    var currentStep by remember { mutableStateOf(0) }

    // Handle state changes and navigation
    LaunchedEffect(assignationState) {
        when (assignationState) {
            is AssignationState.NumberSet -> {
                if (currentStep == 0) {
                    delay(300) // Small delay for smooth transition
                    currentStep = 1
                }
            }
            is AssignationState.NameAndNoteSet -> {
                // Stay on step 1, just enable next button
            }
            is AssignationState.AssignationSet -> {
                onComplete()
            }
            is AssignationState.ErrorTurnAssigned -> {
                // Handle error state
            }
            else -> {}
        }
    }

    AppTheme {
        Box(
            modifier =
                Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
                    .imePadding(), // This handles keyboard padding automatically
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .windowInsetsPadding(WindowInsets.ime), // Additional keyboard handling
            ) {
                // Header with close button and progress
                AssignationHeader(
                    currentStep = currentStep,
                    totalSteps = 3,
                    onClose = onClose,
                )

                // Content area with animated transitions
                Box(
                    modifier =
                        Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                ) {
                    AnimatedContent(
                        targetState = currentStep,
                        transitionSpec = {
                            slideInHorizontally(
                                initialOffsetX = { if (targetState > initialState) it else -it },
                                animationSpec = spring(stiffness = 300f),
                            ) + fadeIn() togetherWith slideOutHorizontally(
                                targetOffsetX = { if (targetState > initialState) -it else it },
                                animationSpec = spring(stiffness = 300f),
                            ) + fadeOut()
                        },
                        label = "step_transition",
                    ) { step ->
                        when (step) {
                            0 ->
                                PhoneNumberStep(
                                    viewModel = viewModel,
                                    onNext = { currentStep = 1 },
                                )
                            1 ->
                                NameNoteStep(
                                    viewModel = viewModel,
                                    onNext = { currentStep = 2 },
                                    onBack = { currentStep = 0 },
                                )
                            2 ->
                                ConfirmationStep(
                                    viewModel = viewModel,
                                    onBack = { currentStep = 1 },
                                    isLoading = assignationState is AssignationState.Loading,
                                )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun AssignationHeader(
    currentStep: Int,
    totalSteps: Int,
    onClose: () -> Unit,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors =
            CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface,
            ),
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "New Assignment",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                )

                IconButton(onClick = onClose) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = "Close",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Progress indicator
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                repeat(totalSteps) { index ->
                    val isCompleted = index < currentStep
                    val isCurrent = index == currentStep

                    StepIndicator(
                        isCompleted = isCompleted,
                        isCurrent = isCurrent,
                        stepNumber = index + 1,
                        modifier = Modifier.weight(1f),
                    )
                }
            }
        }
    }
}

@Composable
private fun StepIndicator(
    isCompleted: Boolean,
    isCurrent: Boolean,
    stepNumber: Int,
    modifier: Modifier = Modifier,
) {
    val animatedProgress by animateFloatAsState(
        targetValue =
            when {
                isCompleted -> 1f
                isCurrent -> 0.5f
                else -> 0f
            },
        animationSpec = spring(stiffness = 300f),
        label = "step_progress",
    )

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            modifier =
                Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(
                        when {
                            isCompleted -> MaterialTheme.colorScheme.primary
                            isCurrent -> MaterialTheme.colorScheme.primaryContainer
                            else -> MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                        },
                    ),
            contentAlignment = Alignment.Center,
        ) {
            androidx.compose.animation.AnimatedVisibility(
                visible = isCompleted,
                enter = scaleIn() + fadeIn(),
                exit = scaleOut() + fadeOut(),
            ) {
                Icon(
                    imageVector = Icons.Filled.Check,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(16.dp),
                )
            }

            androidx.compose.animation.AnimatedVisibility(
                visible = !isCompleted,
                enter = scaleIn() + fadeIn(),
                exit = scaleOut() + fadeOut(),
            ) {
                Text(
                    text = stepNumber.toString(),
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color =
                        when {
                            isCurrent -> MaterialTheme.colorScheme.onPrimaryContainer
                            else -> MaterialTheme.colorScheme.outline
                        },
                )
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        LinearProgressIndicator(
            progress = { animatedProgress },
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(3.dp)
                    .clip(CircleShape),
            color = MaterialTheme.colorScheme.primary,
            trackColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PhoneNumberStep(
    viewModel: ViewModelAssignation,
    onNext: () -> Unit,
) {
    val assignationState by viewModel.assignationState.observeAsState(AssignationState.IDLE)
    val keyboardController = LocalSoftwareKeyboardController.current
    var phoneText by remember { mutableStateOf(viewModel.phoneNumber) }

    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(24.dp)
                .imePadding(), // Ensure this step also handles keyboard
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
                onValueChange = { newValue ->
                    if (newValue.length <= 10 && newValue.all { it.isDigit() }) {
                        phoneText = newValue
                        viewModel.phoneNumber = newValue
                    }
                },
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
                        keyboardType = KeyboardType.Phone,
                        imeAction = ImeAction.Done,
                    ),
                keyboardActions =
                    KeyboardActions(
                        onDone = {
                            keyboardController?.hide()
                            if (assignationState is AssignationState.NumberSet) {
                                onNext()
                            }
                        },
                    ),
                modifier =
                    Modifier
                        .fillMaxWidth(),
                colors =
                    TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                    ),
                isError = phoneText.isNotEmpty() && phoneText.length < 10,
                supportingText = {
                    if (phoneText.isNotEmpty() && phoneText.length < 10) {
                        Text(
                            text = "Phone number must be 10 digits",
                            color = MaterialTheme.colorScheme.error,
                        )
                    } else if (assignationState is AssignationState.NumberSet) {
                        Text(
                            text = "✓ Valid phone number",
                            color = MaterialTheme.colorScheme.primary,
                        )
                    }
                },
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Next button
        androidx.compose.animation.AnimatedVisibility(
            visible = assignationState is AssignationState.NumberSet,
            enter = fadeIn(tween(300)) + scaleIn(tween(300)),
            exit = fadeOut(tween(300)) + scaleOut(tween(300)),
        ) {
            ButtonPrimary(
                onClick = onNext,
                text = "Continue",
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun NameNoteStep(
    viewModel: ViewModelAssignation,
    onNext: () -> Unit,
    onBack: () -> Unit,
) {
    val focusManager = LocalFocusManager.current
    var nameText by remember { mutableStateOf(viewModel.name) }
    var noteText by remember { mutableStateOf(viewModel.note) }
    val nameFocusRequester = remember { FocusRequester() }
    val canContinue = nameText.isNotEmpty()

    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(24.dp)
                .imePadding() // Ensure this step also handles keyboard
                .verticalScroll(rememberScrollState()), // Make it scrollable for keyboard
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
                onValueChange = {
                    nameText = it
                    viewModel.name = it
                },
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
                onValueChange = {
                    noteText = it
                    viewModel.note = it
                },
                label = { Text("Notes (Optional)") },
                placeholder = { Text("Any special requirements or notes...") },
                keyboardOptions =
                    KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Done,
                    ),
                keyboardActions =
                    KeyboardActions(
                        onDone = { focusManager.clearFocus() },
                    ),
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .height(120.dp),
                maxLines = 4,
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

            LaunchedEffect(canContinue) {
                nextButtonState.value = canContinue
            }

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

@Composable
private fun ConfirmationStep(
    viewModel: ViewModelAssignation,
    onBack: () -> Unit,
    isLoading: Boolean,
) {
    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(24.dp)
                .imePadding(), // Ensure this step also handles keyboard
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.height(32.dp))

        AnimatedVisibility(
            visible = true,
            enter = fadeIn(tween(500)) + scaleIn(tween(500)),
        ) {
            Text(
                text = "Confirm Assignment",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center,
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Summary card
        AnimatedVisibility(
            visible = true,
            enter = fadeIn(tween(500, delayMillis = 200)) + slideInHorizontally(),
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                colors =
                    CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    ),
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                ) {
                    Text(
                        text = "Assignment Summary",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    SummaryRow(
                        label = "Client Name",
                        value = viewModel.name.ifEmpty { "Not specified" },
                    )

                    SummaryRow(
                        label = "Phone Number",
                        value = viewModel.phoneNumber,
                    )

                    if (viewModel.note.isNotEmpty()) {
                        SummaryRow(
                            label = "Notes",
                            value = viewModel.note,
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Turn number display
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors =
                            CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer,
                            ),
                    ) {
                        Column(
                            modifier =
                                Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            Text(
                                text = "Turn Number",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                            )
                            Text(
                                text = viewModel.tunr.toString().padStart(2, '0'),
                                style = MaterialTheme.typography.displayMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // Action buttons
        androidx.compose.animation.AnimatedVisibility(
            visible = !isLoading,
            enter = fadeIn(tween(300)),
            exit = fadeOut(tween(300)),
        ) {
            val backButtonState = remember { MutableStateFlow(!isLoading) }
            val assignButtonState = remember { MutableStateFlow(!isLoading) }

            LaunchedEffect(isLoading) {
                backButtonState.value = !isLoading
                assignButtonState.value = !isLoading
            }

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
                    onClick = { viewModel.createAssignation() },
                    text = "Assign Turn",
                    modifier = Modifier.weight(1f),
                    enableState = assignButtonState,
                )
            }
        }

        // Loading state
        androidx.compose.animation.AnimatedVisibility(
            visible = isLoading,
            enter = fadeIn(tween(300)) + scaleIn(tween(300)),
            exit = fadeOut(tween(300)) + scaleOut(tween(300)),
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth(),
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(48.dp),
                    color = MaterialTheme.colorScheme.primary,
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Creating assignment...",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}

@Composable
private fun SummaryRow(
    label: String,
    value: String,
) {
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.weight(1f),
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.End,
        )
    }
}

@Preview(
    backgroundColor = 0XFFFFFF,
    showSystemUi = true,
    device = Devices.PIXEL_XL,
)
@Composable
fun AssignationComposablePreview() {
    // Note: This is just for preview, in real usage ViewModelAssignation would be injected
    AppTheme {
        Text("Assignation Preview - ViewModel required for full functionality")
    }
}

@Preview(
    backgroundColor = 0X000000,
    showSystemUi = true,
    device = Devices.PIXEL_XL,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
)
@Composable
fun AssignationComposablePreviewDark() {
    AppTheme {
        Text("Assignation Dark Preview - ViewModel required for full functionality")
    }
}
