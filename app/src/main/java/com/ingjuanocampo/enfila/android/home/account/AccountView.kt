package com.ingjuanocampo.enfila.android.home.account

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Canvas
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
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshContainer
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ingjuanocampo.enfila.android.R
import com.ingjuanocampo.enfila.android.home.account.model.AccountCard
import com.ingjuanocampo.enfila.android.home.account.model.DashboardMetric
import com.ingjuanocampo.enfila.android.home.account.model.MetricType
import com.ingjuanocampo.enfila.android.home.account.model.OptionCard
import com.ingjuanocampo.enfila.android.home.account.model.StatisticsSectionUI
import com.ingjuanocampo.enfila.android.home.account.model.StatisticsType
import com.ingjuanocampo.enfila.android.home.account.model.TrendDirection
import com.ingjuanocampo.enfila.android.home.account.viewmodel.AccountViewModel
import com.ingjuanocampo.enfila.android.ui.theme.AppTheme
import com.ingjuanocampo.enfila.android.ui.theme.ButtonPrimary
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlin.math.cos
import kotlin.math.sin

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountScreen(accountViewModel: AccountViewModel = viewModel()) {
    AppTheme {
        val state by accountViewModel.state.collectAsState()
        val pullToRefreshState = rememberPullToRefreshState()

        if (pullToRefreshState.isRefreshing) {
            LaunchedEffect(true) {
                delay(1500)
                accountViewModel.refreshData()
                pullToRefreshState.endRefresh()
            }
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .nestedScroll(pullToRefreshState.nestedScrollConnection)
        ) {
            AccountView(
                account = state,
                onLogoutAction = { accountViewModel.onLogout() },
                onRefresh = { accountViewModel.refreshData() }
            )

            PullToRefreshContainer(
                modifier = Modifier.align(Alignment.TopCenter),
                state = pullToRefreshState,
            )
        }
    }
}

@Composable
fun AccountView(
    account: AccountCard,
    onLogoutAction: () -> Unit,
    onRefresh: () -> Unit = {}
) {
    var animationPlayed by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = true) {
        animationPlayed = true
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(16.dp)
    ) {
        // Account Header with Company Info
        item {

            AccountHeaderCard(account = account, onRefresh = onRefresh)

        }

        // Dashboard Metrics
        item {

            DashboardMetricsSection(account = account)

        }

        // Statistics Chart
        item {
            StatisticsChartCard(account = account)
        }

        /*// Performance Analytics
        item {
            AnimatedVisibility(
                visible = animationPlayed,
                enter = fadeIn(tween(800, delayMillis = 600)) + slideInVertically(
                    initialOffsetY = { -40 },
                    animationSpec = tween(600, delayMillis = 600)
                )
            ) {
                PerformanceAnalyticsCard(account = account)
            }
        }*/

        // Statistics Sections
        items(account.buildSections()) { section ->
            StatisticsSectionCard(section = section)
        }

        // Account Options
        item {
            AccountOptionsCard()
        }

        // Logout Section
        item {
            LogoutCard(
                isLoading = account.loadingLogout,
                onLogoutAction = onLogoutAction
            )
        }

        // Bottom spacing
        item {
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
private fun AccountHeaderCard(
    account: AccountCard,
    onRefresh: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(
            modifier = Modifier.padding(24.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Welcome back!",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                    )
                    Text(
                        text = account.companyName.ifEmpty { "Your Company" },
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }

                IconButton(
                    onClick = onRefresh,
                    modifier = Modifier
                        .size(48.dp)
                        .background(
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                            CircleShape
                        )
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Refresh",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                ContactInfoChip(
                    icon = Icons.Default.Phone,
                    text = account.phone.ifEmpty { "Not set" },
                    modifier = Modifier.weight(1f)
                )
                ContactInfoChip(
                    icon = Icons.Default.Email,
                    text = account.email.ifEmpty { "Not set" },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun ContactInfoChip(
    icon: ImageVector,
    text: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .background(
                MaterialTheme.colorScheme.surface.copy(alpha = 0.3f),
                RoundedCornerShape(12.dp)
            )
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(16.dp),
            tint = MaterialTheme.colorScheme.onPrimaryContainer
        )
        Text(
            text = text,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            maxLines = 1
        )
    }
}

@Composable
private fun DashboardMetricsSection(account: AccountCard) {
    Column {
        Text(
            text = "Today's Overview",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            account.buildDashboardMetrics().take(2).forEach { metric ->
                DashboardMetricCard(
                    metric = metric,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            account.buildDashboardMetrics().drop(2).forEach { metric ->
                DashboardMetricCard(
                    metric = metric,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun DashboardMetricCard(
    metric: DashboardMetric,
    modifier: Modifier = Modifier
) {
    val animatedProgress by animateFloatAsState(
        targetValue = 1f,
        animationSpec = tween(1000),
        label = "metric_animation"
    )

    Card(
        modifier = modifier.height(120.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = when (metric.type) {
                MetricType.ACTIVE_STATUS -> MaterialTheme.colorScheme.secondaryContainer
                MetricType.GROWTH -> MaterialTheme.colorScheme.tertiaryContainer
                MetricType.RATING -> MaterialTheme.colorScheme.primaryContainer
                MetricType.TIME -> MaterialTheme.colorScheme.surfaceVariant
                MetricType.COUNTER -> MaterialTheme.colorScheme.surface
            }
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = metric.title,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )

                TrendIcon(trend = metric.trend)
            }

            Text(
                text = (metric.value.toFloatOrNull()?.times(animatedProgress)?.toInt()?.toString()
                    ?: metric.value.take((metric.value.length * animatedProgress).toInt())),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Text(
                text = metric.subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        }
    }
}

@Composable
private fun TrendIcon(trend: TrendDirection) {
    val color = when (trend) {
        TrendDirection.UP -> Color(0xFF4CAF50)
        TrendDirection.DOWN -> Color(0xFFF44336)
        TrendDirection.NEUTRAL -> MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
    }

    Box(
        modifier = Modifier
            .size(24.dp)
            .background(color.copy(alpha = 0.1f), CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = when (trend) {
                TrendDirection.UP -> Icons.Default.TrendingUp
                TrendDirection.DOWN -> Icons.Default.TrendingUp // Would rotate 180° for down
                TrendDirection.NEUTRAL -> Icons.Default.Star
            },
            contentDescription = null,
            modifier = Modifier.size(14.dp),
            tint = color
        )
    }
}

@Composable
private fun StatisticsChartCard(account: AccountCard) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(
            modifier = Modifier.padding(24.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Weekly Performance",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Current week shifts by day",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                }

                Icon(
                    imageVector = Icons.Default.TrendingUp,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Real Weekly Performance Chart
            Column {
                // Y-axis title
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "# of Shifts",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                        modifier = Modifier.padding(end = 8.dp)
                    )
                }
                
                Spacer(modifier = Modifier.height(8.dp))
                
                LineChart(
                    modifier = Modifier.fillMaxWidth(),
                    data = account.weeklyChartData.ifEmpty { listOf(0f, 0f, 0f, 0f, 0f, 0f, 0f) },
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.height(12.dp))
            
            // Day labels
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                account.weeklyLabels.ifEmpty { listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat") }
                    .forEach { dayLabel ->
                        Text(
                            text = dayLabel,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.weight(1f)
                        )
                    }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                ChartLegendItem(
                    color = MaterialTheme.colorScheme.primary,
                    label = "This Week",
                    value = "${account.weeklyTotal}"
                )
                ChartLegendItem(
                    color = MaterialTheme.colorScheme.secondary,
                    label = "Daily Avg",
                    value = "${account.weeklyAverage.toInt()}"
                )
                ChartLegendItem(
                    color = MaterialTheme.colorScheme.tertiary,
                    label = "Peak",
                    value = account.peakHours.split(" ").firstOrNull() ?: "N/A"
                )
            }
        }
    }
}

@Composable
private fun LineChart(
    modifier: Modifier = Modifier,
    data: List<Float>,
    color: Color
) {
    val animatedProgress by animateFloatAsState(
        targetValue = 1f,
        animationSpec = tween(2000),
        label = "chart_animation"
    )

    Row(modifier = modifier) {
        // Y-axis with labels
        YAxisLabels(
            modifier = Modifier.height(120.dp),
            data = data,
            color = color
        )
        
        Spacer(modifier = Modifier.width(8.dp))
        
        // Chart area
        Canvas(
            modifier = Modifier
                .weight(1f)
                .height(120.dp)
        ) {
            if (data.isEmpty() || data.size < 7) return@Canvas

            val maxValue = data.maxOrNull()?.takeIf { it > 0 } ?: 1f
            val stepX = size.width / (data.size - 1).coerceAtLeast(1)
            
            // Calculate chart area (leave space for axis)
            val chartHeight = size.height
            val chartWidth = size.width
            
            val points = data.mapIndexed { index, value ->
                val yPosition = if (maxValue > 0) {
                    chartHeight - (value / maxValue * chartHeight * animatedProgress)
                } else {
                    chartHeight * 0.5f // Center line if no data
                }
                Offset(
                    x = index * stepX,
                    y = yPosition
                )
            }

            // Draw grid lines for better readability
            val gridColor = color.copy(alpha = 0.15f)
            val gridLines = 4
            for (i in 0..gridLines) {
                val y = (chartHeight / gridLines) * i
                drawLine(
                    color = gridColor,
                    start = Offset(0f, y),
                    end = Offset(chartWidth, y),
                    strokeWidth = 1.dp.toPx()
                )
            }

            // Draw line
            val path = Path()
            if (points.isNotEmpty()) {
                path.moveTo(points[0].x, points[0].y)
                for (i in 1 until points.size) {
                    path.lineTo(points[i].x, points[i].y)
                }
            }

            drawPath(
                path = path,
                color = color,
                style = androidx.compose.ui.graphics.drawscope.Stroke(width = 3.dp.toPx())
            )

            // Draw points
            points.forEachIndexed { index, point ->
                // Only draw point if there's data
                if (data[index] > 0) {
                    drawCircle(
                        color = color,
                        radius = 6.dp.toPx(),
                        center = point
                    )
                    drawCircle(
                        color = androidx.compose.ui.graphics.Color.White,
                        radius = 3.dp.toPx(),
                        center = point
                    )
                } else {
                    // Draw smaller point for zero values
                    drawCircle(
                        color = color.copy(alpha = 0.3f),
                        radius = 3.dp.toPx(),
                        center = point
                    )
                }
            }
        }
    }
}

@Composable
private fun YAxisLabels(
    modifier: Modifier = Modifier,
    data: List<Float>,
    color: Color
) {
    val maxValue = data.maxOrNull()?.takeIf { it > 0 } ?: 1f
    
    // Calculate nice scale values
    val scaleValues = calculateYAxisScale(maxValue)
    
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.End
    ) {
        // Draw labels from top to bottom (high to low values)
        scaleValues.reversed().forEach { value ->
            Text(
                text = "${value.toInt()}",
                style = MaterialTheme.typography.bodySmall,
                color = color.copy(alpha = 0.7f),
                fontSize = 10.sp
            )
        }
    }
}

private fun calculateYAxisScale(maxValue: Float): List<Float> {
    val gridLines = 5 // 0, 25%, 50%, 75%, 100%
    val stepValue = maxValue / (gridLines - 1)
    
    return (0 until gridLines).map { i ->
        stepValue * i
    }
}

@Composable
private fun ChartLegendItem(
    color: Color,
    label: String,
    value: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(12.dp)
                .background(color, CircleShape)
        )
        Column {
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
private fun PerformanceAnalyticsCard(account: AccountCard) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(
            modifier = Modifier.padding(24.dp)
        ) {
            Text(
                text = "Performance Analytics",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Efficiency Score
            PerformanceMetric(
                title = "Service Efficiency",
                value = 85,
                maxValue = 100,
                suffix = "%",
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Customer Satisfaction
            PerformanceMetric(
                title = "Customer Satisfaction",
                value = 96,
                maxValue = 100,
                suffix = "%",
                color = MaterialTheme.colorScheme.tertiary
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Queue Management
            PerformanceMetric(
                title = "Queue Management",
                value = 78,
                maxValue = 100,
                suffix = "%",
                color = MaterialTheme.colorScheme.secondary
            )
        }
    }
}

@Composable
private fun PerformanceMetric(
    title: String,
    value: Int,
    maxValue: Int,
    suffix: String,
    color: Color
) {
    val animatedProgress by animateFloatAsState(
        targetValue = value.toFloat() / maxValue.toFloat(),
        animationSpec = tween(1500),
        label = "performance_animation"
    )

    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "${(value * animatedProgress).toInt()}$suffix",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = color
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        LinearProgressIndicator(
            progress = { animatedProgress },
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp)),
            color = color,
            trackColor = color.copy(alpha = 0.2f)
        )
    }
}

@Composable
private fun StatisticsSectionCard(section: StatisticsSectionUI) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            StatisticItem(
                title = section.title1,
                value = section.value1,
                type = section.type,
                modifier = Modifier.weight(1f)
            )

            Divider(
                modifier = Modifier
                    .height(60.dp)
                    .width(1.dp),
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
            )

            StatisticItem(
                title = section.title2,
                value = section.value2,
                type = section.type,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun StatisticItem(
    title: String,
    value: String,
    type: StatisticsType,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val iconColor = when (type) {
            StatisticsType.COUNTER -> MaterialTheme.colorScheme.primary
            StatisticsType.DAILY -> MaterialTheme.colorScheme.secondary
            StatisticsType.TIME -> MaterialTheme.colorScheme.tertiary
            StatisticsType.TREND -> Color(0xFF4CAF50)
            StatisticsType.PERFORMANCE -> Color(0xFF9C27B0)
        }

        Box(
            modifier = Modifier
                .size(48.dp)
                .background(iconColor.copy(alpha = 0.1f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = when (type) {
                    StatisticsType.COUNTER -> "📊"
                    StatisticsType.DAILY -> "📅"
                    StatisticsType.TIME -> "⏱️"
                    StatisticsType.TREND -> "📈"
                    StatisticsType.PERFORMANCE -> "⭐"
                },
                style = MaterialTheme.typography.titleMedium
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = value,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center
        )

        Text(
            text = title,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun AccountOptionsCard() {
    val options = listOf(
        OptionCard(R.drawable.ic_account, "Profile Settings", "Manage your account") {},
        OptionCard(R.drawable.ic_groups_48px, "Team Management", "Invite team members") {},
        OptionCard(R.drawable.ic_format_list, "Business Hours", "Set operating hours") {},
        OptionCard(R.drawable.ic_tips_and_updates, "Help & Support", "Get assistance") {}
    )

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Text(
                text = "Account Settings",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            options.forEach { option ->
                AccountOptionItem(option = option)
                if (option != options.last()) {
                    Spacer(modifier = Modifier.height(4.dp))
                }
            }
        }
    }
}

@Composable
private fun AccountOptionItem(option: OptionCard) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { option.callback() }
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(
                    MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f),
                    CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = when (option.title) {
                    "Profile Settings" -> Icons.Default.Person
                    "Team Management" -> Icons.Default.Business
                    "Business Hours" -> Icons.Default.Settings
                    else -> Icons.Default.Settings
                },
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(20.dp)
            )
        }

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = option.title,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface
            )
            if (option.subtitle.isNotEmpty()) {
                Text(
                    text = option.subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
        }
    }
}

@Composable
private fun LogoutCard(
    isLoading: Boolean,
    onLogoutAction: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.3f)
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val buttonState = remember { MutableStateFlow(!isLoading) }
            buttonState.value = !isLoading

            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = MaterialTheme.colorScheme.error
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Signing out...",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.error
                )
            } else {
                ButtonPrimary(
                    onClick = onLogoutAction,
                    text = "Sign Out",
                    modifier = Modifier.fillMaxWidth(),
                    enableState = buttonState
                )
            }
        }
    }
}

// Preview composables
@Preview(
    showSystemUi = true,
    device = Devices.PIXEL_XL,
)
@Composable
fun AccountScreenPreview() {
    AppTheme {
        AccountView(
            account = AccountCard(
                companyName = "Demo Restaurant",
                phone = "+1234567890",
                email = "demo@restaurant.com",
                numberClients = "#127",
                totalShifts = "#456",
                shiftByDay = "23",
                clientsByDay = "45",
                waitingTime = "8 min",
                attentionTime = "12 min",
                avgShiftsPerWeek = "16",
                peakHours = "Wed (25)",
                customerSatisfaction = "4.8⭐",
                monthlyGrowth = "+15%",
                // Real weekly chart data sample
                weeklyChartData = listOf(5f, 12f, 8f, 25f, 18f, 22f, 15f), // Sun-Sat
                weeklyLabels = listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"),
                weeklyTotal = 105,
                weeklyAverage = 15f
            ),
            onLogoutAction = {}
        )
    }
}

@Preview(
    showSystemUi = true,
    device = Devices.PIXEL_XL,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
)
@Composable
fun AccountScreenPreviewDark() {
    AppTheme {
        AccountView(
            account = AccountCard(
                companyName = "Night Café",
                phone = "+1987654321",
                email = "night@cafe.com",
                numberClients = "#89",
                totalShifts = "#234",
                loadingLogout = true,
                // Low activity week sample
                weeklyChartData = listOf(2f, 5f, 3f, 8f, 6f, 12f, 4f),
                weeklyLabels = listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"),
                weeklyTotal = 40,
                weeklyAverage = 5.7f,
                peakHours = "Fri (12)"
            ),
            onLogoutAction = {}
        )
    }
}
