package com.ingjuanocampo.enfila.android.home.list.details

import android.content.res.Configuration
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.HourglassEmpty
import androidx.compose.material.icons.filled.Notes
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Timeline
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ingjuanocampo.enfila.android.R
import com.ingjuanocampo.enfila.android.home.list.model.ShiftItem
import com.ingjuanocampo.enfila.android.ui.common.AnimatedComposeButton
import com.ingjuanocampo.enfila.android.ui.common.toLoadingState
import com.ingjuanocampo.enfila.android.ui.theme.AppTheme
import com.ingjuanocampo.enfila.android.ui.theme.ButtonError
import com.ingjuanocampo.enfila.android.ui.theme.ButtonGrayStroke
import com.ingjuanocampo.enfila.android.ui.theme.ButtonPrimary
import com.ingjuanocampo.enfila.android.utils.toDurationText
import com.ingjuanocampo.enfila.domain.entity.ShiftState
import com.ingjuanocampo.enfila.domain.entity.getNow
import kotlinx.coroutines.delay
import java.util.concurrent.TimeUnit

@Composable
fun ShiftDetailScreen(
    shiftDetailUi: ShiftItem,
    onCancel: () -> Unit,
    onActive: () -> Unit,
    onFinish: () -> Unit,
) {
    AppTheme {
        Box(
            modifier =
                Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background),
        ) {
            LazyColumn(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                item {
                    ShiftClientHeader(shiftDetailUi)
                }

                item {
                    ShiftStatusCard(shiftDetailUi)
                }

                item {
                    ShiftTimelineCard(shiftDetailUi)
                }

                item {
                    ShiftDetailsCard(shiftDetailUi)
                }

                item {
                    if (shiftDetailUi.notes.isNotEmpty()) {
                        ShiftNotesCard(shiftDetailUi)
                    }
                }

                // Add spacing for floating action buttons
                item {
                    Spacer(modifier = Modifier.height(100.dp))
                }
            }

            // Floating Action Buttons
            val loadingState = shiftDetailUi.isProcessingActions.toLoadingState()
            Box(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .align(Alignment.BottomCenter),
            ) {
                AnimatedComposeButton(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                    state = loadingState,
                ) {
                    ShiftActionButtons(
                        shiftState = shiftDetailUi.state,
                        onCancel = onCancel,
                        onActive = onActive,
                        onFinish = onFinish,
                    )
                }
            }
        }
    }
}

@Composable
private fun ShiftClientHeader(shiftDetailUi: ShiftItem) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        colors =
            CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant,
            ),
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            // Avatar
            Box(
                modifier =
                    Modifier
                        .size(64.dp)
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
                    modifier = Modifier.size(32.dp),
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Client Info
            Column(
                modifier = Modifier.weight(1f),
            ) {
                Text(
                    text = shiftDetailUi.name.ifEmpty { "Anonymous Client" },
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                )

                Spacer(modifier = Modifier.height(4.dp))

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
                        text = shiftDetailUi.phone,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        imageVector = Icons.Filled.Assignment,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(16.dp),
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Turn #${shiftDetailUi.currentTurn}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontWeight = FontWeight.Medium,
                    )
                }
            }
        }
    }
}

@Composable
private fun ShiftStatusCard(shiftDetailUi: ShiftItem) {
    val statusData = getStatusData(shiftDetailUi.state)

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        colors =
            CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant,
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
                                .size(12.dp)
                                .background(statusData.color, CircleShape),
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Status",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }

                Text(
                    text = statusData.displayName,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    color = statusData.color,
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = statusData.description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Composable
private fun ShiftTimelineCard(shiftDetailUi: ShiftItem) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        colors =
            CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant,
            ),
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    imageVector = Icons.Filled.Timeline,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(20.dp),
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Timeline",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            ShiftTimelineItem(
                icon = Icons.Filled.CalendarToday,
                title = "Created",
                subtitle = shiftDetailUi.formmattedIssueDate,
                isCompleted = true,
            )

            if (shiftDetailUi.state != ShiftState.WAITING.name) {
                ShiftTimelineItem(
                    icon = Icons.Filled.AccessTime,
                    title = "Called",
                    subtitle = "Client was called for service",
                    isCompleted = true,
                )
            }

            if (shiftDetailUi.state == ShiftState.FINISHED.name) {
                ShiftTimelineItem(
                    icon = Icons.Filled.CheckCircle,
                    title = "Completed",
                    subtitle = "Service completed successfully",
                    isCompleted = true,
                )
            }
        }
    }
}

@Composable
private fun ShiftTimelineItem(
    icon: ImageVector,
    title: String,
    subtitle: String,
    isCompleted: Boolean,
) {
    Row(
        modifier = Modifier.padding(vertical = 8.dp),
        verticalAlignment = Alignment.Top,
    ) {
        val iconColor =
            if (isCompleted) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.outline
            }

        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = iconColor,
            modifier = Modifier.size(20.dp),
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
            )
        }
    }
}

@Composable
private fun ShiftDetailsCard(shiftDetailUi: ShiftItem) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        colors =
            CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant,
            ),
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
        ) {
            Text(
                text = "Details",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(bottom = 16.dp),
            )

            // Real-time waiting time
            var chronometer: String by remember { mutableStateOf(shiftDetailUi.waitTime) }

            if (shiftDetailUi.state == ShiftState.WAITING.name) {
                LaunchedEffect(Unit) {
                    var ticks = 0L
                    val initialNow = getNow()
                    while (true) {
                        delay(1000)
                        ticks++
                        chronometer = (
                            (
                                initialNow - (
                                    shiftDetailUi.issueDate -
                                        TimeUnit.SECONDS.toMillis(ticks)
                                )
                            ).toDurationText()
                        )
                    }
                }
            }

            DetailItem(
                icon = Icons.Filled.HourglassEmpty,
                label = "Waiting Time",
                value = chronometer,
                isHighlighted = shiftDetailUi.state == ShiftState.WAITING.name,
            )

            if (shiftDetailUi.attentionTime.isNotEmpty()) {
                DetailItem(
                    icon = Icons.Filled.AccessTime,
                    label = "Attention Duration",
                    value = shiftDetailUi.attentionTime,
                )
            }

            if (shiftDetailUi.scheduledHour.isNotEmpty()) {
                DetailItem(
                    icon = Icons.Filled.Schedule,
                    label = "Scheduled Hour",
                    value = shiftDetailUi.scheduledHour,
                )
            }
        }
    }
}

@Composable
private fun DetailItem(
    icon: ImageVector,
    label: String,
    value: String,
    isHighlighted: Boolean = false,
) {
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint =
                if (isHighlighted) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.onSurfaceVariant
                },
            modifier = Modifier.size(20.dp),
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column(
            modifier = Modifier.weight(1f),
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = if (isHighlighted) FontWeight.Bold else FontWeight.Normal,
                color =
                    if (isHighlighted) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.onSurfaceVariant
                    },
            )
        }
    }
}

@Composable
private fun ShiftNotesCard(shiftDetailUi: ShiftItem) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        colors =
            CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant,
            ),
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    imageVector = Icons.Filled.Notes,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(20.dp),
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Notes",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = shiftDetailUi.notes,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 20.sp,
            )
        }
    }
}

@Composable
private fun ShiftActionButtons(
    shiftState: String,
    onCancel: () -> Unit,
    onActive: () -> Unit,
    onFinish: () -> Unit,
) {
    when (shiftState.uppercase()) {
        ShiftState.WAITING.name -> {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                ButtonPrimary(
                    onClick = onActive,
                    text = stringResource(id = R.string.active),
                    modifier = Modifier.weight(1f),
                )
                ButtonGrayStroke(
                    onClick = onCancel,
                    text = stringResource(id = R.string.cancel),
                    modifier = Modifier.weight(1f),
                )
            }
        }

        ShiftState.CALLING.name -> {
            ButtonError(
                onClick = onFinish,
                text = stringResource(id = R.string.finish),
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

private data class StatusData(
    val displayName: String,
    val description: String,
    val color: Color,
)

@Composable
private fun getStatusData(state: String): StatusData {
    return when (state.uppercase()) {
        ShiftState.WAITING.name ->
            StatusData(
                displayName = "Waiting",
                description = "Client is in queue waiting to be called",
                color = MaterialTheme.colorScheme.primary,
            )
        ShiftState.CALLING.name ->
            StatusData(
                displayName = "Active",
                description = "Client is being attended",
                color = MaterialTheme.colorScheme.tertiary,
            )
        ShiftState.FINISHED.name ->
            StatusData(
                displayName = "Completed",
                description = "Service has been completed",
                color = MaterialTheme.colorScheme.primary,
            )
        ShiftState.CANCELLED.name ->
            StatusData(
                displayName = "Cancelled",
                description = "Shift was cancelled",
                color = MaterialTheme.colorScheme.error,
            )
        else ->
            StatusData(
                displayName = "Unknown",
                description = "Status not recognized",
                color = MaterialTheme.colorScheme.outline,
            )
    }
}

@Preview(
    backgroundColor = 0XFFFFFF,
    showSystemUi = true,
    device = Devices.NEXUS_5,
)
@Composable
fun ShiftDetailScreenPreview() {
    val shiftDetailUi =
        ShiftItem(
            id = "1",
            name = "John Doe",
            formmattedIssueDate = "Wed, May 13, 2023 14:30",
            waitTime = "2h 30m",
            currentTurn = "5",
            attentionTime = "30 minutes",
            scheduledHour = "09:00 AM",
            notes = "Client requested special assistance. Please ensure wheelchair accessibility is available.",
            endDate = 0L,
            state = "WAITING",
            issueDate = 0L,
            phone = "+1 (555) 123-4567",
        )
    ShiftDetailScreen(shiftDetailUi, {}, {}, {})
}

@Preview(
    backgroundColor = 0XFFFFFF,
    showSystemUi = true,
    device = Devices.NEXUS_5,
)
@Composable
fun ShiftDetailScreenActivePreview() {
    val shiftDetailUi =
        ShiftItem(
            id = "2",
            name = "Jane Smith",
            formmattedIssueDate = "Wed, May 13, 2023 15:45",
            waitTime = "1h 15m",
            currentTurn = "3",
            attentionTime = "45 minutes",
            scheduledHour = "10:30 AM",
            notes = "",
            endDate = 0L,
            state = "CALLING",
            issueDate = 0L,
            phone = "+1 (555) 987-6543",
        )
    ShiftDetailScreen(shiftDetailUi, {}, {}, {})
}

@Preview(
    backgroundColor = 0X000000,
    showSystemUi = true,
    device = Devices.PIXEL_XL,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
)
@Composable
fun ShiftDetailScreenActivePreviewNight() {
    val shiftDetailUi =
        ShiftItem(
            id = "2",
            name = "Jane Smith",
            formmattedIssueDate = "Wed, May 13, 2023 15:45",
            waitTime = "1h 15m",
            currentTurn = "3",
            attentionTime = "45 minutes",
            scheduledHour = "10:30 AM",
            notes = "",
            endDate = 0L,
            state = "CALLING",
            issueDate = 0L,
            phone = "+1 (555) 987-6543",
        )
    ShiftDetailScreen(shiftDetailUi, {}, {}, {})
}
