package com.ingjuanocampo.enfila.android.home.list.details

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
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
    onFinish: () -> Unit
) {
    AppTheme {
        ConstraintLayout(modifier = Modifier.fillMaxSize()) {
            val (columRef, buttonsRef) = createRefs()
            Column(modifier = Modifier
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
                .constrainAs(columRef) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
                verticalArrangement = Arrangement.Top) {
                TableItem(label = stringResource(R.string.client_name), value = shiftDetailUi.name)
                DividerBody()
                TableItem(label = stringResource(R.string.client_phone), value = shiftDetailUi.phone)
                DividerBody()
                TableItem(label = stringResource(R.string.shift_created_date), value = shiftDetailUi.formmattedIssueDate)
                /*            DividerBody()
                            TableItem(label = "Duration", value = shiftDetailUi.geEndElapsedTime().toDurationText())*/
                DividerBody()

                var chronometer: String by remember { mutableStateOf(shiftDetailUi.waitTime) }

                if (shiftDetailUi.state == ShiftState.WAITING.name) {
                    LaunchedEffect(Unit) {
                        var ticks = 0L
                        val initialNow = getNow()
                        while (true) {
                            delay(1000)
                            ticks++
                            chronometer =
                                ((initialNow- (shiftDetailUi.issueDate - TimeUnit.SECONDS.toMillis(
                                    ticks
                                ))).toDurationText())
                        }
                    }
                }

                TableItem(label = stringResource(R.string.shift_waiting_time), value = chronometer)


                DividerBody()
                TableItem(label = stringResource(R.string.shift_number_turn), value = shiftDetailUi.currentTurn)
                DividerBody()
                TableItem(label = stringResource(R.string.shift_attention_duration), value = shiftDetailUi.attentionTime)
                DividerBody()
                TableItem(label = stringResource(R.string.shift_status), value = shiftDetailUi.visualStatus)
                DividerBody()
/*                TableItem(label = stringResource(R.string.shift_scheduled_hour), value = shiftDetailUi.scheduledHour)
                DividerBody()*/
                TableItem(label = stringResource(R.string.shift_notes), value = shiftDetailUi.notes)
            }
            val loadingState = shiftDetailUi.isProcessingActions.toLoadingState()
            AnimatedComposeButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 20.dp)
                    .constrainAs(buttonsRef) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom)
                    },
                state = loadingState
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    when (shiftDetailUi.state.toUpperCase()) {
                        ShiftState.WAITING.name -> {
                            ButtonPrimary(
                                {
                                    onActive()
                                }, stringResource(id = R.string.active),
                                modifier = Modifier.weight(0.5f)
                            )
                            Spacer(modifier = Modifier.size(10.dp))
                            ButtonGrayStroke(
                                {
                                    onCancel()
                                }, stringResource(id = R.string.cancel),
                                modifier = Modifier.weight(0.5f)
                            )
                        }

                        ShiftState.CALLING.name -> {
                            ButtonError(
                                {
                                    onFinish()
                                }, stringResource(id = R.string.finish),
                                modifier = Modifier.fillMaxWidth()
                            )
                        }

                        else -> {
                        }
                    }
                }
            }

        }

    }

}

@Composable
private fun TableItem(label: String, value: String) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Text(
            text = "$label",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier
        )


    }
}

@Composable
private fun DividerBody() {
    Divider(
        modifier = Modifier
            .fillMaxWidth()
            .height(0.5.dp),
        color = MaterialTheme.colorScheme.outline
    )
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
        state = "WAITING",
        issueDate = 0L,
        phone = "31312313",
    )
    ShiftDetailScreen(shiftDetailUi, {}, {}, {})
}

@Preview(
    backgroundColor = 0x000000,
    showSystemUi = true,
    device = Devices.NEXUS_5,
    uiMode = UI_MODE_NIGHT_YES
)
@Composable
fun TableScreenPreviewDark() {
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
        state = "WAITING",
        issueDate = 0L,
        phone = "31312313",
    )
    ShiftDetailScreen(shiftDetailUi, {}, {}, {})
}