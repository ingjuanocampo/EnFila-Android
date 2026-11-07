package com.ingjuanocampo.enfila.android.home.clients

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import com.ingjuanocampo.common.composable.GenericEmptyState
import com.ingjuanocampo.enfila.android.R
import com.ingjuanocampo.enfila.android.home.clients.components.ClientCard
import com.ingjuanocampo.enfila.android.home.clients.components.ClientCardSize
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
            verticalArrangement = Arrangement.Top,
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
                    modifier = Modifier.fillMaxSize(),
                )
            } else {
                LazyColumn {
                    items(clientList) { client ->
                        ClientCard(
                            client = client,
                            shiftCount = client.shifts?.size ?: 0,
                            size = ClientCardSize.COMPACT,
                            onClick = { onClientSelected.invoke(client.id) },
                        )
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
