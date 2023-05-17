package com.ingjuanocampo.enfila.android.home.list.details

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ingjuanocampo.enfila.android.home.list.model.ShiftItem
import com.ingjuanocampo.enfila.android.ui.theme.AppTheme
import com.ingjuanocampo.enfila.android.utils.toDurationText

@Composable
fun ShiftDetailScreen(shiftDetailUi: ShiftItem) {
    AppTheme {
        Column(modifier = Modifier
            .padding(16.dp)
            .verticalScroll(rememberScrollState())) {
            TableItem(label = "Name", value = shiftDetailUi.name)
            DividerBody()
            TableItem(label = "Phone", value = shiftDetailUi.phone)
            DividerBody()
            TableItem(label = "Created Date", value = shiftDetailUi.formmattedIssueDate)
/*            DividerBody()
            TableItem(label = "Duration", value = shiftDetailUi.geEndElapsedTime().toDurationText())*/
            DividerBody()
            TableItem(label = "Waiting Time", value = shiftDetailUi.waitTime)
            DividerBody()
            TableItem(label = "Number Turn", value = shiftDetailUi.currentTurn)
            DividerBody()
            TableItem(label = "Attention Duration", value = shiftDetailUi.attentionTime)
            DividerBody()
            TableItem(label = "Scheduled Hour", value = shiftDetailUi.scheduledHour)
            DividerBody()
            TableItem(label = "Notes", value = shiftDetailUi.notes)
        }
    }

}

@Composable
private fun TableItem(label: String, value: String) {
    Row(modifier = Modifier.padding(vertical = 8.dp)) {
        Text(
            text = "$label:",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onPrimaryContainer,

            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(0.2f)
        )
        Text(text = value,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            modifier = Modifier.weight(0.8f))


    }
}

@Composable
private fun DividerBody() {
    Divider(modifier = Modifier
        .fillMaxWidth()
        .height(1.dp),
        color = MaterialTheme.colorScheme.onPrimaryContainer)
}

@Preview(
    backgroundColor = 0XFFFFFF,
    showSystemUi = true,
    device = Devices.NEXUS_5,
)
@Composable
fun TableScreenPreview() {
    val shiftDetailUi = ShiftItem(
        id = "",
        name = "John Doe",
        formmattedIssueDate = "2023-05-13",
        waitTime = "2 hours",
        currentTurn = "5",
        attentionTime = "30 minutes",
        scheduledHour = "09:00 AM",
        notes = "Lorem ipsum dolor sit amet",
        endDate = 0L,
        state = "Shif",
        issueDate = 0L,
        phone = "31312313"
    )
    ShiftDetailScreen(shiftDetailUi)
}