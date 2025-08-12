package com.ingjuanocampo.enfila.android.home.clients.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ingjuanocampo.enfila.domain.entity.Client

enum class ClientCardSize {
    COMPACT, // For list view
    FULL, // For detail view
}

@Composable
fun ClientCard(
    client: Client,
    shiftCount: Int,
    size: ClientCardSize,
    onClick: (() -> Unit)? = null,
    onRefresh: (() -> Unit)? = null,
    showShiftCount: Boolean = true,
    modifier: Modifier = Modifier,
) {
    val (
        cardPadding,
        avatarSize,
        iconSize,
        phoneIconSize,
        titleTextSize,
        subtitleTextSize,
        shiftTextSize,
        elevation,
        showCard,
    ) =
        when (size) {
            ClientCardSize.COMPACT ->
                ClientCardDimensions(
                    cardPadding = 12.dp,
                    avatarSize = 40.dp,
                    iconSize = 20.dp,
                    phoneIconSize = 14.dp,
                    titleTextSize = 16.sp,
                    subtitleTextSize = 14.sp,
                    shiftTextSize = 12.sp,
                    elevation = 0.dp,
                    showCard = false,
                )
            ClientCardSize.FULL ->
                ClientCardDimensions(
                    cardPadding = 20.dp,
                    avatarSize = 56.dp,
                    iconSize = 28.dp,
                    phoneIconSize = 16.dp,
                    titleTextSize = 22.sp,
                    subtitleTextSize = 16.sp,
                    shiftTextSize = 14.sp,
                    elevation = 0.dp,
                    showCard = true,
                )
        }

    val cardModifier =
        if (onClick != null) {
            modifier
                .fillMaxWidth()
                .clickable { onClick() }
        } else {
            modifier.fillMaxWidth()
        }

    val content = @Composable {
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(cardPadding),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f),
            ) {
                // Avatar
                Box(
                    modifier =
                        Modifier
                            .size(avatarSize)
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
                        modifier = Modifier.size(iconSize),
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                // Client Info
                Column(
                    modifier = Modifier.weight(1f),
                ) {
                    Text(
                        text = client.name ?: "Unknown Client",
                        fontSize = titleTextSize,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Phone,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(phoneIconSize),
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = client.id,
                            fontSize = subtitleTextSize,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }
                }
            }

            // Right side content
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                // Shift count (only show if requested)
                if (showShiftCount) {
                    Column(
                        horizontalAlignment = Alignment.End,
                    ) {
                        Text(
                            text = shiftCount.toString(),
                            fontSize = shiftTextSize,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                        )
                        Text(
                            text = if (shiftCount == 1) "shift" else "shifts",
                            fontSize = (shiftTextSize.value - 2).sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }

                // Refresh button (only for full size)
                if (onRefresh != null && size == ClientCardSize.FULL) {
                    if (showShiftCount) {
                        Spacer(modifier = Modifier.width(8.dp))
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

    if (showCard) {
        Card(
            modifier = cardModifier,
            elevation = CardDefaults.cardElevation(defaultElevation = elevation),
            colors =
                CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                ),
        ) {
            content()
        }
    } else {
        Box(modifier = cardModifier) {
            content()
        }
    }
}

private data class ClientCardDimensions(
    val cardPadding: Dp,
    val avatarSize: Dp,
    val iconSize: Dp,
    val phoneIconSize: Dp,
    val titleTextSize: TextUnit,
    val subtitleTextSize: TextUnit,
    val shiftTextSize: TextUnit,
    val elevation: Dp,
    val showCard: Boolean,
)
