package com.ingjuanocampo.enfila.android.assignation

import android.content.res.Configuration
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ingjuanocampo.enfila.android.assignation.steps.ConfirmationStep
import com.ingjuanocampo.enfila.android.assignation.steps.NameNoteStep
import com.ingjuanocampo.enfila.android.assignation.steps.PhoneNumberStep
import com.ingjuanocampo.enfila.android.assignation.viewmodel.AssignationState
import com.ingjuanocampo.enfila.android.assignation.viewmodel.ViewModelAssignation
import com.ingjuanocampo.enfila.android.ui.theme.AppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AssignationComposable(
    viewModel: ViewModelAssignation = viewModel(),
    onClose: () -> Unit,
) {
    val assignationState by viewModel.assignationState.observeAsState(AssignationState.IDLE)
    var currentStep by remember { mutableIntStateOf(1) }

    Box(
        modifier =
            Modifier
                .fillMaxSize()
                .imePadding()
                .windowInsetsPadding(WindowInsets.ime),
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .windowInsetsPadding(WindowInsets.ime),
        ) {
            // Header with progress
            AssignationHeader(
                currentStep = currentStep,
                totalSteps = 3,
                onClose = onClose,
            )

            // Step content with animations
            AnimatedContent(
                targetState = currentStep,
                transitionSpec = {
                    if (targetState > initialState) {
                        slideInHorizontally(
                            initialOffsetX = { it },
                            animationSpec = spring(),
                        ) + fadeIn(tween(300)) togetherWith
                            slideOutHorizontally(
                                targetOffsetX = { -it },
                                animationSpec = spring(),
                            ) + fadeOut(tween(300))
                    } else {
                        slideInHorizontally(
                            initialOffsetX = { -it },
                            animationSpec = spring(),
                        ) + fadeIn(tween(300)) togetherWith
                            slideOutHorizontally(
                                targetOffsetX = { it },
                                animationSpec = spring(),
                            ) + fadeOut(tween(300))
                    }
                },
                label = "step_transition",
            ) { step ->
                when (step) {
                    1 ->
                        PhoneNumberStep(
                            viewModel = viewModel,
                            onNext = { currentStep = 2 },
                        )
                    2 ->
                        NameNoteStep(
                            viewModel = viewModel,
                            onNext = { currentStep = 3 },
                            onBack = { currentStep = 1 },
                        )
                    3 ->
                        ConfirmationStep(
                            viewModel = viewModel,
                            onBack = { currentStep = 2 },
                            onAssign = {
                                // Handle successful assignment
                                // The onClose will be called by the parent when state changes
                            },
                        )
                }
            }
        }
    }

    // Handle state changes that should close the dialog
    when (assignationState) {
        AssignationState.AssignationSet -> {
            // Assignment completed successfully, close after a brief delay
            // This will be handled by the parent component
        }
        AssignationState.ErrorTurnAssigned -> {
            // Handle error case if needed
        }
        else -> {
            // Other states don't require action
        }
    }
}

@Composable
private fun AssignationHeader(
    currentStep: Int,
    totalSteps: Int,
    onClose: () -> Unit,
) {
    Column {
        // Top bar with close button
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "New Assignment",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
            )

            IconButton(onClick = onClose) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = "Close",
                    tint = MaterialTheme.colorScheme.onSurface,
                )
            }
        }

        // Progress indicator
        Column(
            modifier = Modifier.padding(horizontal = 16.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = "Step $currentStep of $totalSteps",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Text(
                    text =
                        when (currentStep) {
                            1 -> "Phone Number"
                            2 -> "Client Info"
                            3 -> "Confirmation"
                            else -> ""
                        },
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.End,
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            LinearProgressIndicator(
                progress = { currentStep.toFloat() / totalSteps.toFloat() },
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.surfaceVariant,
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Preview(
    showSystemUi = true,
    device = Devices.PIXEL_XL,
)
@Composable
fun AssignationComposablePreview() {
    AppTheme {
        Text(
            text = "Assignation Preview\nViewModel required for full functionality\n\nStep components now available separately:\n• PhoneNumberStepContent\n• NameNoteStepContent\n• ConfirmationStepContent",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(24.dp),
        )
    }
}

@Preview(
    showSystemUi = true,
    device = Devices.PIXEL_XL,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
)
@Composable
fun AssignationComposablePreviewDark() {
    AppTheme {
        Text(
            text = "Assignation Dark Preview\nViewModel required for full functionality\n\nStep components now available separately:\n• PhoneNumberStepContent\n• NameNoteStepContent\n• ConfirmationStepContent",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(24.dp),
        )
    }
}
