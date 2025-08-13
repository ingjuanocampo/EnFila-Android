package com.ingjuanocampo.enfila.android.assignation.steps

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ingjuanocampo.enfila.android.assignation.viewmodel.AssignationState
import com.ingjuanocampo.enfila.android.assignation.viewmodel.ViewModelAssignation
import com.ingjuanocampo.enfila.android.ui.theme.AppTheme
import com.ingjuanocampo.enfila.android.ui.theme.ButtonPrimary
import kotlinx.coroutines.flow.MutableStateFlow

@Composable
fun ConfirmationStep(
    viewModel: ViewModelAssignation,
    onBack: () -> Unit,
    onAssign: () -> Unit,
) {
    val assignationState by viewModel.assignationState.observeAsState(AssignationState.IDLE)
    
    ConfirmationStepContent(
        clientName = viewModel.name,
        phoneNumber = viewModel.phoneNumber,
        notes = viewModel.note,
        assignationState = assignationState,
        turnNumber = viewModel.tunr,
        onBack = onBack,
        onAssign = {
            viewModel.createAssignation()
            onAssign()
        }
    )
}

@Composable
fun ConfirmationStepContent(
    clientName: String,
    phoneNumber: String,
    notes: String,
    assignationState: AssignationState,
    turnNumber: Int,
    onBack: () -> Unit,
    onAssign: () -> Unit,
) {
    val isLoading = assignationState == AssignationState.Loading
    val isAssigned = assignationState == AssignationState.AssignationSet
    val canAssign = !isLoading && !isAssigned
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .imePadding(),
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
                colors = CardDefaults.cardColors(
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
                        value = clientName.ifEmpty { "Not specified" },
                    )

                    SummaryRow(
                        label = "Phone Number",
                        value = phoneNumber,
                    )

                    if (notes.isNotEmpty()) {
                        SummaryRow(
                            label = "Notes",
                            value = notes,
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Turn number display
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                        ),
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            Text(
                                text = "Turn Number",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                            )
                            
                            val scale by animateFloatAsState(
                                targetValue = if (isAssigned) 1.2f else 1f,
                                animationSpec = tween(300),
                                label = "turn_scale"
                            )
                            
                            Text(
                                text = "#$turnNumber",
                                style = MaterialTheme.typography.headlineLarge,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                modifier = Modifier.scale(scale),
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // Action buttons or loading state
        AnimatedVisibility(
            visible = !isLoading,
            enter = fadeIn(tween(500, delayMillis = 400)),
            exit = fadeOut(tween(300)) + scaleOut(tween(300)),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                val backButtonState = remember { MutableStateFlow(!isLoading) }
                val assignButtonState = remember { MutableStateFlow(canAssign) }

                // Update button states
                backButtonState.value = !isLoading
                assignButtonState.value = canAssign

                ButtonPrimary(
                    onClick = onBack,
                    text = "Back",
                    modifier = Modifier.weight(1f),
                    enableState = backButtonState,
                )

                ButtonPrimary(
                    onClick = onAssign,
                    text = if (isAssigned) "Assigned!" else "Assign Turn",
                    modifier = Modifier.weight(1f),
                    enableState = assignButtonState,
                )
            }
        }

        // Loading state
        AnimatedVisibility(
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
        modifier = Modifier
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
    showSystemUi = true,
    device = Devices.PIXEL_XL,
)
@Composable
fun ConfirmationStepContentPreview() {
    AppTheme {
        ConfirmationStepContent(
            clientName = "John Doe",
            phoneNumber = "3137550993",
            notes = "Regular customer, prefers window seat",
            assignationState = AssignationState.IDLE,
            turnNumber = 42,
            onBack = {},
            onAssign = {}
        )
    }
}

@Preview(
    showSystemUi = true,
    device = Devices.PIXEL_XL,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
)
@Composable
fun ConfirmationStepContentPreviewDark() {
    AppTheme {
        ConfirmationStepContent(
            clientName = "Maria Garcia",
            phoneNumber = "5551234567",
            notes = "",
            assignationState = AssignationState.Loading,
            turnNumber = 15,
            onBack = {},
            onAssign = {}
        )
    }
}

@Preview(
    showSystemUi = true,
    device = Devices.PIXEL_XL,
)
@Composable
fun ConfirmationStepContentAssignedPreview() {
    AppTheme {
        ConfirmationStepContent(
            clientName = "Sarah Johnson",
            phoneNumber = "9876543210",
            notes = "First time customer",
            assignationState = AssignationState.AssignationSet,
            turnNumber = 7,
            onBack = {},
            onAssign = {}
        )
    }
}
