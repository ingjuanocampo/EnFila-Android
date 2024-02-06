package com.ingjuanocampo.enfila.android.home.clients.details

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.StopCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ingjuanocampo.enfila.android.ui.common.AnimatedButtonState
import com.ingjuanocampo.enfila.android.ui.common.AnimatedComposeButton
import com.ingjuanocampo.enfila.android.ui.theme.AppTheme

@Composable
fun TurnListItem(
    currentTurn: String,
    number: String,
    name: String,
    state: String,
    issueDate: String,
    timeElapse: String,
    isCancellable: Boolean
) {
    AppTheme {
        Surface(
            modifier = Modifier
                .fillMaxWidth(),
            color = MaterialTheme.colorScheme.surface,
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier.weight(0.1f, fill = true),
                    text = currentTurn,
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.secondary
                )
                Spacer(modifier = Modifier.size(10.dp))

                Column(
                    modifier = Modifier.weight(0.4f, fill = true),
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {

                    Text(
                        text = number,
                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = name,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = state,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = issueDate,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }


                Column(
                    modifier = Modifier.weight(0.4f, fill = true),
                    horizontalAlignment = Alignment.End
                ) {
                    if (isCancellable) {
                        var animationState by remember { mutableStateOf(AnimatedButtonState.IDLE) }
                        AnimatedComposeButton(state = animationState) {
                            Icon(
                                Icons.Outlined.StopCircle,
                                modifier = Modifier.clickable {
                                    animationState = AnimatedButtonState.PROGRESS
                                },
                                contentDescription = "Clear",
                                tint = MaterialTheme.colorScheme.error
                            )


                        }
                    } else {

                        Text(
                            text = timeElapse,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }

            }
        }
    }

}

@Preview
@Composable
fun TurnListItemPreview() {
    TurnListItem(
        currentTurn = "123",
        state = "Waitting",
        name = "Juan O",
        isCancellable = false,
        issueDate = "12/12/12",
        number = "3137550993",
        timeElapse = "6 hours"
    )
}

@Preview
@Composable
fun TurnListItemPreviewActive() {
    TurnListItem(
        currentTurn = "123",
        state = "Waitting",
        name = "Juan O",
        isCancellable = true,
        issueDate = "12/12/12",
        number = "3137550993",
        timeElapse = "6 hours"
    )
}


@Preview(uiMode = UI_MODE_NIGHT_YES)
@Composable
fun TurnListItemPreviewDark() {
    TurnListItem(
        currentTurn = "123",
        state = "Waitting",
        name = "Juan O",
        isCancellable = false,
        issueDate = "12/12/12",
        number = "3137550993",
        timeElapse = "6 hours"
    )
}

@Preview(uiMode = UI_MODE_NIGHT_YES)
@Composable
fun TurnListItemPreviewActiveDark() {
    TurnListItem(
        currentTurn = "123",
        state = "Waitting",
        name = "Juan O",
        isCancellable = true,
        issueDate = "12/12/12",
        number = "3137550993",
        timeElapse = "6 hours"
    )
}
