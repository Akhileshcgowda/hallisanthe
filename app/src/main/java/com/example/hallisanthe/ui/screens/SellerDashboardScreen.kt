package com.example.hallisanthe.ui.screens

import com.example.hallisanthe.R
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.hallisanthe.data.viewmodel.AuthViewModel
import com.example.hallisanthe.ui.theme.*

@Composable
fun SellerDashboardScreen(navController: NavController, viewModel: com.example.hallisanthe.data.viewmodel.SellerDashboardViewModel, authViewModel: AuthViewModel) {
    val metrics by viewModel.metrics.collectAsState()
    val inquiries by viewModel.inquiries.collectAsState(initial = emptyList())
    var startAnimation by remember { mutableStateOf(false) }
    val profileProgress by animateFloatAsState(
        targetValue = if (startAnimation) 0.85f else 0f,
        animationSpec = tween(durationMillis = 1000),
        label = "ProfileProgress"
    )

    LaunchedEffect(Unit) {
        startAnimation = true
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 100.dp, top = 8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Greeting
            item {
                val currentUser by authViewModel.currentUser.collectAsState()
                val userName = currentUser?.displayName ?: stringResource(R.string.default_seller_name)
                Column(modifier = Modifier.padding(top = 8.dp)) {
                    Text(
                        "${stringResource(R.string.greeting_namaskara)}, $userName! 🙏",
                        style = MaterialTheme.typography.headlineMedium,
                        color = Primary,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        stringResource(R.string.seller_greeting_subtitle),
                        fontSize = 16.sp,
                        color = OnSurfaceVariant
                    )
                }
            }

            // Stats Grid
            item {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    StatCard(
                        modifier = Modifier.weight(1f),
                        title = stringResource(R.string.active_products),
                        value = metrics.activeListings.toString(),
                        icon = Icons.Filled.Inventory2,
                        iconTint = Primary
                    )
                    StatCard(
                        modifier = Modifier.weight(1f),
                        title = stringResource(R.string.new_inquiries),
                        value = metrics.pendingInquiries.toString(),
                        icon = Icons.Filled.Forum,
                        iconTint = Secondary
                    )
                }
            }

            // Shop Performance
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(
                            brush = Brush.linearGradient(
                                colors = listOf(SurfaceContainer, SurfaceVariant)
                            )
                        )
                        .border(1.dp, OutlineVariant, RoundedCornerShape(16.dp))
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(stringResource(R.string.total_sales), style = MaterialTheme.typography.labelLarge, color = OnSurfaceVariant)
                            Text(metrics.totalSales, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, color = OnSurface)
                        }
                        Surface(color = TertiaryFixedDim, shape = CircleShape) {
                            Row(modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp), verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Filled.TrendingUp, contentDescription = null, tint = OnTertiaryFixed, modifier = Modifier.size(14.dp))
                                Text("+${metrics.salesTrend}%", color = OnTertiaryFixed, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }

            // Quick Actions
            item {
                Column {
                    Text(
                        text = stringResource(R.string.quick_actions),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = OnSurface,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        QuickAction(
                            modifier = Modifier.weight(1f),
                            title = stringResource(R.string.add_product),
                            icon = Icons.Filled.AddCircle,
                            backgroundColor = Primary,
                            contentColor = OnPrimary,
                            onClick = { navController.navigate("add_product") }
                        )
                        QuickAction(
                            modifier = Modifier.weight(1f),
                            title = stringResource(R.string.my_listings),
                            icon = Icons.Filled.ListAlt,
                            backgroundColor = Secondary,
                            contentColor = OnSecondary,
                            onClick = { navController.navigate("my_listings") }
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        QuickAction(
                            modifier = Modifier.weight(1f),
                            title = stringResource(R.string.inquiries),
                            icon = Icons.Filled.MarkChatUnread,
                            iconTint = Secondary,
                            backgroundColor = SurfaceContainerLowest,
                            contentColor = OnSurface,
                            border = true,
                            onClick = { navController.navigate("inquiries") }
                        )
                        QuickAction(
                            modifier = Modifier.weight(1f),
                            title = stringResource(R.string.shop_settings),
                            icon = Icons.Filled.Settings,
                            iconTint = Primary,
                            backgroundColor = SurfaceContainerLowest,
                            contentColor = OnSurface,
                            border = true,
                            onClick = { navController.navigate("shop_settings") }
                        )
                    }
                }
            }

            // Recent Inquiries Header
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(stringResource(R.string.recent_inquiries), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = OnSurface)
                    Text(stringResource(R.string.view_all), color = Primary, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, modifier = Modifier.clickable { navController.navigate("inquiries") })
                }
            }

            // Recent Inquiries List
            items(inquiries.take(2)) { inquiry ->
                InquiryItem(
                    initial = inquiry.customerName.take(1),
                    name = inquiry.customerName,
                    message = inquiry.lastMessage,
                    time = inquiry.time,
                    isUnread = inquiry.isUnread
                )
            }
        }

        // Floating Action Button
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            FloatingActionButton(
                onClick = { navController.navigate("santhe_ai_assistant") },
                containerColor = SecondaryContainer,
                contentColor = OnSecondaryContainer,
                shape = CircleShape
            ) {
                Icon(Icons.Filled.SmartToy, contentDescription = stringResource(R.string.seller_ai_assistant))
            }
        }
    }
}

@Composable
fun StatCard(modifier: Modifier = Modifier, title: String, value: String, icon: ImageVector, iconTint: Color) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceContainerLowest),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(title, style = MaterialTheme.typography.labelSmall, color = OnSurfaceVariant)
                Icon(icon, contentDescription = null, tint = iconTint, modifier = Modifier.size(18.dp))
            }
            Text(value, style = MaterialTheme.typography.headlineMedium, color = OnSurface, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun QuickAction(
    modifier: Modifier = Modifier,
    title: String,
    icon: ImageVector,
    iconTint: Color = LocalContentColor.current,
    backgroundColor: Color,
    contentColor: Color,
    border: Boolean = false,
    onClick: () -> Unit
) {
    Surface(
        modifier = modifier.height(52.dp),
        onClick = onClick,
        shape = RoundedCornerShape(26.dp),
        color = backgroundColor,
        border = if (border) androidx.compose.foundation.BorderStroke(1.dp, OutlineVariant) else null,
        shadowElevation = if (backgroundColor == SurfaceContainerLowest) 1.dp else 4.dp
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icon, contentDescription = null, tint = iconTint, modifier = Modifier.size(20.dp).padding(end = 6.dp))
            Text(title, color = contentColor, fontSize = 12.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun InquiryItem(initial: String, name: String, message: String, time: String, isUnread: Boolean) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { }
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .background(if (isUnread) PrimaryContainer else SurfaceVariant),
            contentAlignment = Alignment.Center
        ) {
            Text(initial, color = if (isUnread) OnPrimaryContainer else Primary, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        }
        Column(modifier = Modifier.weight(1f)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(name, style = MaterialTheme.typography.bodyLarge, fontWeight = if (isUnread) FontWeight.Bold else FontWeight.SemiBold, color = OnSurface)
                Text(time, style = MaterialTheme.typography.labelSmall, color = OnSurfaceVariant)
            }
            Text(
                message,
                style = MaterialTheme.typography.bodySmall,
                color = if (isUnread) OnSurface else OnSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
        if (isUnread) {
            Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(Primary))
        }
    }
}
