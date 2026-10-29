package com.ingjuanocampo.enfila.android.home.clients.details

import android.content.res.Configuration
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
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Timeline
import androidx.compose.material.icons.outlined.Assignment
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ingjuanocampo.common.composable.GenericEmptyState
import com.ingjuanocampo.enfila.android.home.clients.components.ClientCard
import com.ingjuanocampo.enfila.android.home.clients.components.ClientCardSize
import com.ingjuanocampo.enfila.android.ui.theme.AppTheme
import com.ingjuanocampo.enfila.android.utils.toDurationText
import com.ingjuanocampo.enfila.domain.entity.Client
import com.ingjuanocampo.enfila.domain.entity.CompanySite
import com.ingjuanocampo.enfila.domain.entity.Shift
import com.ingjuanocampo.enfila.domain.entity.ShiftState
import com.ingjuanocampo.enfila.domain.entity.getNow
import com.ingjuanocampo.enfila.domain.usecases.model.ClientDetails
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.Period
import java.util.Date
import java.util.Locale

@Composable
fun ClientDetailsScreen(
    state: ClientDetailsViewState,
    onShiftClick: (String) -> Unit,
    onRefresh: () -> Unit,
    editor: ProfileEditorState = ProfileEditorState(),
    onStartEdit: () -> Unit = {},
    onCancelEdit: () -> Unit = {},
    onSave: () -> Unit = {},
    onEditorChange: (ProfileEditorState) -> Unit = {},
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
                        editor = editor,
                        onShiftClick = onShiftClick,
                        onRefresh = onRefresh,
                        onStartEdit = onStartEdit,
                        onCancelEdit = onCancelEdit,
                        onSave = onSave,
                        onEditorChange = onEditorChange,
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
    editor: ProfileEditorState,
    onShiftClick: (String) -> Unit,
    onRefresh: () -> Unit,
    onStartEdit: () -> Unit,
    onCancelEdit: () -> Unit,
    onSave: () -> Unit,
    onEditorChange: (ProfileEditorState) -> Unit,
) {
    LazyColumn(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        item {
            ClientCard(
                client = clientDetails.client,
                shiftCount = clientDetails.totalShifts,
                size = ClientCardSize.FULL,
                onRefresh = onRefresh,
                showShiftCount = false,
                detailLine = clientDetails.client.favoriteOrder?.takeIf { it.isNotBlank() },
            )
        }

        item {
            VisitsCard(clientDetails = clientDetails, companySites = editor.companySites)
        }

        item {
            AboutCard(
                clientDetails = clientDetails,
                editor = editor,
                onStartEdit = onStartEdit,
                onCancelEdit = onCancelEdit,
                onSave = onSave,
                onEditorChange = onEditorChange,
            )
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
private fun VisitsCard(
    clientDetails: ClientDetails,
    companySites: List<CompanySite>,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
    ) {
        Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text(
                text = "Visits",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            if (!clientDetails.hasShifts) {
                Text("No visits yet", color = MaterialTheme.colorScheme.onSurfaceVariant)
            } else {
                FactRow("Orders", clientDetails.orders.toString())
                FactRow("Cancels", clientDetails.cancels.toString())
                FactRow("Last visit", clientDetails.lastVisit?.let(::formatVisitDate) ?: "—")
                FactRow("Usual request", clientDetails.usualRequest ?: "—")
                FactRow("Most visited store", storeName(clientDetails.mostVisitedStoreId, companySites) ?: "—")
                FactRow("Avg. wait", clientDetails.averageWaitTime.toDurationText())
            }
        }
    }
}

@Composable
private fun AboutCard(
    clientDetails: ClientDetails,
    editor: ProfileEditorState,
    onStartEdit: () -> Unit,
    onCancelEdit: () -> Unit,
    onSave: () -> Unit,
    onEditorChange: (ProfileEditorState) -> Unit,
) {
    val client = clientDetails.client
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
    ) {
        Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "About",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                if (editor.isEditing) {
                    Row {
                        TextButton(onClick = onCancelEdit, enabled = !editor.isSaving) { Text("Cancel") }
                        Button(onClick = onSave, enabled = !editor.isSaving) {
                            Text(if (editor.isSaving) "Saving" else "Save")
                        }
                    }
                } else {
                    TextButton(onClick = onStartEdit) { Text("Edit") }
                }
            }
            if (editor.isEditing) {
                AboutForm(editor = editor, onEditorChange = onEditorChange)
            } else {
                val pinnedOrder = client.favoriteOrder?.takeIf { it.isNotBlank() }
                val order = pinnedOrder ?: clientDetails.usualRequest
                val pinnedStore = client.favoriteStoreId?.takeIf { it.isNotBlank() }
                val storeId = pinnedStore ?: clientDetails.mostVisitedStoreId
                ProfileRow(
                    "Favorite order",
                    order ?: "Add favorite order",
                    caption = if (pinnedOrder == null && clientDetails.usualRequest != null) "From their visits" else null,
                    muted = order == null,
                )
                ProfileRow(
                    "Favorite store",
                    storeName(storeId, editor.companySites) ?: "Add favorite store",
                    caption = if (pinnedStore == null && clientDetails.mostVisitedStoreId != null) "From their visits" else null,
                    muted = storeId == null,
                )
                ProfileRow("Email", client.email?.takeIf { it.isNotBlank() } ?: "Add email", muted = client.email.isNullOrBlank())
                val age = ageLabel(client.birthDate)
                ProfileRow("Age", age ?: "Add age", muted = age == null)
                ProfileRow("Sex", sexLabel(client.sex) ?: "Add sex", muted = sexLabel(client.sex) == null)
                ProfileRow("City", client.city?.takeIf { it.isNotBlank() } ?: "Add city", muted = client.city.isNullOrBlank())
                ProfileRow("Notes", client.notes?.takeIf { it.isNotBlank() } ?: "Add notes", muted = client.notes.isNullOrBlank())
            }
            editor.error?.let { message ->
                Text(message, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}

@Composable
private fun AboutForm(
    editor: ProfileEditorState,
    onEditorChange: (ProfileEditorState) -> Unit,
) {
    ProfileField(editor.name, "Name") { onEditorChange(editor.copy(name = it)) }
    ProfileField(editor.favoriteOrder, "Favorite order") { onEditorChange(editor.copy(favoriteOrder = it.take(120))) }
    ChoiceField(
        label = "Favorite store",
        value = editor.favoriteStoreId,
        options = listOf("" to "Not set") + editor.companySites.map { site ->
            site.id to (site.name?.takeIf { it.isNotBlank() } ?: site.id)
        },
    ) { onEditorChange(editor.copy(favoriteStoreId = it)) }
    ProfileField(editor.email, "Email") { onEditorChange(editor.copy(email = it)) }
    ProfileField(editor.birthDate, "Birth date (yyyy-MM-dd)") { onEditorChange(editor.copy(birthDate = it)) }
    ChoiceField(
        label = "Sex",
        value = editor.sex,
        options = listOf("" to "Not set", "FEMALE" to "Female", "MALE" to "Male", "OTHER" to "Other"),
    ) { onEditorChange(editor.copy(sex = it)) }
    ProfileField(editor.city, "City") { onEditorChange(editor.copy(city = it.take(100))) }
    ProfileField(editor.notes, "Notes", singleLine = false) { onEditorChange(editor.copy(notes = it.take(500))) }
}

@Composable
private fun FactRow(label: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(value, fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Composable
private fun ProfileRow(
    label: String,
    value: String,
    caption: String? = null,
    muted: Boolean = false,
) {
    Column {
        Text(label, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(
            value,
            color = if (muted) MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f) else MaterialTheme.colorScheme.onSurface,
        )
        if (caption != null) {
            Text(caption, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.primary)
        }
    }
}

@Composable
private fun ProfileField(
    value: String,
    label: String,
    singleLine: Boolean = true,
    onValueChange: (String) -> Unit,
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        singleLine = singleLine,
        minLines = if (singleLine) 1 else 3,
        modifier = Modifier.fillMaxWidth(),
    )
}

@Composable
private fun ChoiceField(
    label: String,
    value: String,
    options: List<Pair<String, String>>,
    onValueChange: (String) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    val selected = options.firstOrNull { it.first == value }?.second ?: "Not set"
    Box {
        OutlinedButton(onClick = { expanded = true }, modifier = Modifier.fillMaxWidth()) {
            Text("$label: $selected")
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            options.forEach { (id, name) ->
                DropdownMenuItem(
                    text = { Text(name) },
                    onClick = {
                        onValueChange(id)
                        expanded = false
                    },
                )
            }
        }
    }
}

private fun storeName(id: String?, sites: List<CompanySite>): String? {
    if (id.isNullOrBlank()) return null
    return sites.firstOrNull { it.id == id }?.name?.takeIf { it.isNotBlank() } ?: "Unknown store"
}

private fun sexLabel(sex: String?): String? = when (sex?.uppercase()) {
    "FEMALE" -> "Female"
    "MALE" -> "Male"
    "OTHER" -> "Other"
    else -> null
}

private fun ageLabel(birthDate: String?): String? {
    val date = runCatching { LocalDate.parse(birthDate) }.getOrNull() ?: return null
    val years = Period.between(date, LocalDate.now()).years
    if (years < 0) return null
    return if (years == 1) "1 year" else "$years years"
}

private fun formatVisitDate(timestamp: Long): String {
    val formatter = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
    return formatter.format(Date(timestamp))
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
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
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
