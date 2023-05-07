package com.ingjuanocampo.enfila.android.home.clients

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ingjuanocampo.enfila.android.ui.theme.AppTheme
import com.ingjuanocampo.enfila.android.utils.SearchBox
import com.ingjuanocampo.enfila.domain.entity.Client
import com.ingjuanocampo.enfila.domain.entity.defaultClient


@Composable
fun ClientListScreenChat(clientList: List<Client>, onSearch: (String) -> Unit) {
    AppTheme {
        Column(Modifier.fillMaxSize()) {
            SearchBox(
                onSearch = onSearch,
                modifier = Modifier
                    .fillMaxWidth()
            )

            LazyColumn {
                items(clientList) { client ->
                    Surface(
                        modifier = Modifier
                            .padding(horizontal = 4.dp, vertical = 8.dp)
                            .fillMaxWidth(),
                        shape = RoundedCornerShape(2.dp),
                        color = MaterialTheme.colorScheme.surface,
                        elevation = 4.dp,
                    ) {
                        val textColor = MaterialTheme.colorScheme.onPrimaryContainer

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 8.dp, vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Column {
                                Text(
                                    text = client.name ?: "N/A",
                                    maxLines = 1,
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                                )
                                Text(
                                    text = "ID: ${client.id}",
                                    maxLines = 1,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = textColor,
                                )
                            }
                            Text(
                                text = "${client.shifts?.size ?: 0} shifts",
                                style = MaterialTheme.typography.bodyMedium
                                    .copy(fontWeight = FontWeight.Bold),
                                color = textColor,
                            )
                        }
                    }
                }
            }
        }

    }

}


@Preview
@Composable
private fun PreviewClientItem() {
    ClientListScreenChat(listOf(defaultClient, defaultClient, defaultClient),{})
}

