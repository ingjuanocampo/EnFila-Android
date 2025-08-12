package com.ingjuanocampo.enfila.android.home.clients

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ingjuanocampo.common.composable.GenericEmptyState
import com.ingjuanocampo.enfila.android.R
import com.ingjuanocampo.enfila.android.ui.theme.AppTheme
import com.ingjuanocampo.enfila.android.utils.SearchBox
import com.ingjuanocampo.enfila.domain.entity.Client
import com.ingjuanocampo.enfila.domain.entity.defaultClient

@Composable
fun ClientListScreenChat(
    clientList: List<Client>,
    onSearch: (String) -> Unit,
    onClientSelected: (String) -> Unit,
) {
    AppTheme {
        Column(
            Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
        ) {
            SearchBox(
                onSearch = onSearch,
                modifier =
                    Modifier
                        .fillMaxWidth(),
            )
            if (clientList.isEmpty()) {
                GenericEmptyState(
                    title =
                        stringResource(
                            R.string.empty_fragment_clients,
                        ),
                    icon = Icons.Outlined.Person,
                    modifier = Modifier.fillMaxHeight(),
                )
            } else {
                LazyColumn {
                    items(clientList) { client ->
                        val textColor = MaterialTheme.colorScheme.onPrimaryContainer
                        Row(
                            modifier =
                                Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        onClientSelected.invoke(client.id)
                                    }
                                    .padding(horizontal = 8.dp, vertical = 12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Column {
                                Text(
                                    text = client.name ?: "N/A",
                                    maxLines = 1,
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.onSurface,
                                )
                                Text(
                                    text = "ID: ${client.id}",
                                    maxLines = 1,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurface,
                                )
                            }
                            Text(
                                text = "${client.shifts?.size ?: 0} shifts",
                                style =
                                    MaterialTheme.typography.bodyMedium
                                        .copy(fontWeight = FontWeight.Bold),
                                color = textColor,
                            )
                        }

                        HorizontalDivider()
                    }
                }
            }
        }
    }
}

@Preview(
    backgroundColor = 0XFFFFFF,
    showSystemUi = true,
    device = Devices.PIXEL_XL,
)
@Composable
private fun PreviewClientItem() {
    ClientListScreenChat(listOf(defaultClient, defaultClient, defaultClient), {}, {})
}

@Preview(
    backgroundColor = 0XFFFFFF,
    showSystemUi = true,
    device = Devices.PIXEL_XL,
)
@Composable
private fun PreviewClientItemEmpty() {
    ClientListScreenChat(listOf(), {}, {})
}

@Preview(
    backgroundColor = 0X000000,
    showSystemUi = true,
    device = Devices.PIXEL_XL,
    uiMode = UI_MODE_NIGHT_YES,
)
@Composable
private fun PreviewClientItemDark() {
    ClientListScreenChat(listOf(defaultClient, defaultClient, defaultClient), {}, {})
}
