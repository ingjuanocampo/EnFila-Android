package com.ingjuanocampo.enfila.android.home.clients.details

import android.content.res.Configuration
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Timeline
import androidx.compose.material.icons.outlined.Assignment
import androidx.compose.material.icons.outlined.HourglassEmpty
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ingjuanocampo.common.composable.GenericEmptyState
import com.ingjuanocampo.enfila.android.ui.theme.AppTheme
import com.ingjuanocampo.enfila.android.utils.toDurationText
import com.ingjuanocampo.enfila.domain.entity.Client
import com.ingjuanocampo.enfila.domain.entity.Shift
import com.ingjuanocampo.enfila.domain.entity.ShiftState
import com.ingjuanocampo.enfila.domain.entity.getNow
import com.ingjuanocampo.enfila.domain.usecases.model.ClientDetails
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ClientDetailsScreen(
    state: ClientDetailsViewState,
    onShiftClick: (String) -> Unit,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier,
) {
    AppTheme {
        Box(modifier = modifier.fillMaxSize()) {
            when (state) {
                is ClientDetailsViewState.Loading -> {
                    LoadingState(modifier = Modifier.align(Alignment.Center))
                }
                is ClientDetailsViewState.Success -> {
                    ClientDetailsContent(
                        clientDetails = state.clientDetails,
                        onShiftClick = onShiftClick,
                        onRefresh = onRefresh,
                    )
                }
                is ClientDetailsViewState.Error -> {
                    ErrorState(
                        message = state.message,
                        onRetry = onRefresh,
                        modifier = Modifier.align(Alignment.Center),
                    )
                }
                is ClientDetailsViewState.NotFound -> {
                    NotFoundState(modifier = Modifier.align(Alignment.Center))
                }
            }
        }
    }
}

@Composable
private fun ClientDetailsContent(
    clientDetails: ClientDetails,
    onShiftClick: (String) -> Unit,
    onRefresh: () -> Unit,
) {
    LazyColumn(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        item {
            ClientHeaderCard(
                clientDetails = clientDetails,
                onRefresh = onRefresh,
            )
        }

        item {
            ClientStatsCard(clientDetails = clientDetails)
        }

        item {
            Text(
                text = "Recent Shifts",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(vertical = 8.dp),
            )
        }

        if (clientDetails.hasShifts) {
            items(clientDetails.shifts) { shift ->
                ShiftCard(
                    shift = shift,
                    onClick = { onShiftClick(shift.id) },
                )
            }
        } else {
            item {
                GenericEmptyState(
                    title = "No shifts yet",
                    icon = Icons.Outlined.Assignment,
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                )
            }
        }
    }
}

@Composable
private fun ClientHeaderCard(
    clientDetails: ClientDetails,
    onRefresh: () -> Unit,
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
            modifier = Modifier.padding(20.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Box(
                        modifier =
                            Modifier
                                .size(56.dp)
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
                            modifier = Modifier.size(28.dp),
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Column {
                        Text(
                            text = clientDetails.clientName,
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Phone,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(16.dp),
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = clientDetails.phoneNumber,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                    }
                }

                IconButton(onClick = onRefresh) {
                    Icon(
                        imageVector = Icons.Filled.Refresh,
                        contentDescription = "Refresh",
                        tint = MaterialTheme.colorScheme.primary,
                    )
                }
            }
        }
    }
}

@Composable
private fun ClientStatsCard(clientDetails: ClientDetails) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors =
            CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant,
            ),
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
        ) {
            Text(
                text = "Statistics",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(bottom = 16.dp),
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
            ) {
                StatItem(
                    icon = Icons.Outlined.Assignment,
                    label = "Total Shifts",
                    value = clientDetails.totalShifts.toString(),
                )

                StatItem(
                    icon = Icons.Outlined.HourglassEmpty,
                    label = "Active",
                    value = clientDetails.activeShifts.toString(),
                )

                StatItem(
                    icon = Icons.Filled.AccessTime,
                    label = "Avg. Wait",
                    value = clientDetails.averageWaitTime.toDurationText(),
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Completion Rate Progress
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(
                        text = "Completion Rate",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    Text(
                        text = "${(clientDetails.completionRate * 100).toInt()}%",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                val animatedProgress by animateFloatAsState(
                    targetValue = clientDetails.completionRate,
                    label = "completion_progress",
                )

                LinearProgressIndicator(
                    progress = { animatedProgress },
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp)),
                    color = MaterialTheme.colorScheme.primary,
                    trackColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                )
            }
        }
    }
}

@Composable
private fun StatItem(
    icon: ImageVector,
    label: String,
    value: String,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(24.dp),
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
private fun ShiftCard(
    shift: Shift,
    onClick: () -> Unit,
) {
    Card(
        modifier =
            Modifier
                .fillMaxWidth()
                .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = "Shift #${shift.number}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    ShiftStatusBadge(state = shift.state)
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = formatShiftDate(shift.date),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )

                if (shift.notes?.isNotEmpty() == true) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = shift.notes,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f),
                        maxLines = 2,
                    )
                }
            }

            Icon(
                imageVector = Icons.Filled.Timeline,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(20.dp),
            )
        }
    }
}

@Composable
private fun ShiftStatusBadge(state: ShiftState) {
    val (backgroundColor, textColor, text) =
        when (state) {
            ShiftState.WAITING ->
                Triple(
                    MaterialTheme.colorScheme.secondaryContainer,
                    MaterialTheme.colorScheme.onSecondaryContainer,
                    "Waiting",
                )
            ShiftState.CALLING ->
                Triple(
                    MaterialTheme.colorScheme.primaryContainer,
                    MaterialTheme.colorScheme.onPrimaryContainer,
                    "Calling",
                )
            ShiftState.CANCELLED ->
                Triple(
                    MaterialTheme.colorScheme.errorContainer,
                    MaterialTheme.colorScheme.onErrorContainer,
                    "Cancelled",
                )
            ShiftState.FINISHED ->
                Triple(
                    MaterialTheme.colorScheme.tertiaryContainer,
                    MaterialTheme.colorScheme.onTertiaryContainer,
                    "Finished",
                )
        }

    Box(
        modifier =
            Modifier
                .background(
                    backgroundColor,
                    RoundedCornerShape(12.dp),
                )
                .padding(horizontal = 8.dp, vertical = 4.dp),
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall,
            color = textColor,
            fontSize = 10.sp,
        )
    }
}

@Composable
private fun LoadingState(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        CircularProgressIndicator(
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(48.dp),
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Loading client details...",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
private fun ErrorState(
    message: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        GenericEmptyState(
            title = "Error Loading Client",
            icon = Icons.Outlined.Person,
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.height(16.dp))
        IconButton(onClick = onRetry) {
            Icon(
                imageVector = Icons.Filled.Refresh,
                contentDescription = "Retry",
                tint = MaterialTheme.colorScheme.primary,
            )
        }
    }
}

@Composable
private fun NotFoundState(modifier: Modifier = Modifier) {
    GenericEmptyState(
        title = "Client Not Found",
        icon = Icons.Outlined.Person,
        modifier = modifier,
    )
}

private fun formatShiftDate(timestamp: Long): String {
    val formatter = SimpleDateFormat("MMM dd, yyyy 'at' HH:mm", Locale.getDefault())
    return formatter.format(Date(timestamp))
}

// Preview Data
private val sampleShifts =
    listOf(
        Shift(
            // 1 day ago
            date = getNow() - 86400000,
            id = "1",
            parentCompanySite = "site1",
            number = 1,
            contactId = "123456789",
            notes = "Regular appointment",
            state = ShiftState.FINISHED,
            attentionStartDate = getNow() - 86400000 + 3600000,
            endDate = getNow() - 86400000 + 7200000,
        ),
        Shift(
            // 12 hours ago
            date = getNow() - 43200000,
            id = "2",
            parentCompanySite = "site1",
            number = 2,
            contactId = "123456789",
            notes = "Urgent consultation",
            state = ShiftState.CALLING,
            attentionStartDate = getNow() - 43200000 + 1800000,
            endDate = null,
        ),
    )

private val sampleClientDetails =
    ClientDetails(
        client =
            Client(
                id = "123456789",
                name = "John Doe",
                shifts = listOf("1", "2"),
            ),
        shifts = sampleShifts,
        totalShifts = 2,
        activeShifts = 1,
        // 1 hour
        averageWaitTime = 3600000,
    )

@Preview(
    showSystemUi = true,
    device = Devices.PIXEL_XL,
)
@Composable
private fun ClientDetailsScreenPreview() {
    ClientDetailsScreen(
        state = ClientDetailsViewState.Success(sampleClientDetails),
        onShiftClick = { },
        onRefresh = { },
    )
}

@Preview(
    showSystemUi = true,
    device = Devices.PIXEL_XL,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
)
@Composable
private fun ClientDetailsScreenDarkPreview() {
    ClientDetailsScreen(
        state = ClientDetailsViewState.Success(sampleClientDetails),
        onShiftClick = { },
        onRefresh = { },
    )
}

@Preview
@Composable
private fun LoadingStatePreview() {
    AppTheme {
        ClientDetailsScreen(
            state = ClientDetailsViewState.Loading,
            onShiftClick = { },
            onRefresh = { },
        )
    }
}
