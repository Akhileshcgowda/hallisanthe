package com.example.hallisanthe.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.compose.ui.res.stringResource
import com.example.hallisanthe.R
import com.example.hallisanthe.data.Notification
import com.example.hallisanthe.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationsScreen(navController: NavController, viewModel: com.example.hallisanthe.data.viewmodel.SellerDashboardViewModel) {
    val notifications by viewModel.notifications.collectAsState(initial = emptyList())
    val unreadCount = notifications.count { !it.isRead }

    Scaffold(
        containerColor = Background
    ) { paddingValues ->
        if (notifications.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize().padding(paddingValues), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Icon(Icons.Filled.NotificationsNone, contentDescription = null, modifier = Modifier.size(64.dp), tint = OnSurfaceVariant.copy(alpha = 0.3f))
                    Text(stringResource(R.string.all_caught_up), color = OnSurfaceVariant)
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                if (unreadCount > 0) {
                    item {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                            Text(stringResource(R.string.new_label), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                            Surface(color = SurfaceContainerHigh, shape = CircleShape) {
                                Text(stringResource(R.string.new_count, unreadCount), modifier = Modifier.padding(horizontal = 12.dp, vertical = 2.dp), style = MaterialTheme.typography.labelSmall, color = Primary)
                            }
                        }
                    }
                }

                items(notifications, key = { it.id }) { notification ->
                    NotificationItem(
                        notification = notification,
                        onViewDetails = { viewModel.markNotificationAsRead(notification.id) },
                        onDismiss = { viewModel.dismissNotification(notification.id) }
                    )
                }
            }
        }
    }
}

@Composable
fun NotificationItem(
    notification: com.example.hallisanthe.data.Notification,
    onViewDetails: () -> Unit,
    onDismiss: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (notification.isRead) SurfaceContainerLowest else SurfaceContainerLow
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = if (notification.isRead) 1.dp else 4.dp),
        border = if (notification.isRead) androidx.compose.foundation.BorderStroke(1.dp, OutlineVariant) else null
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            val (icon, color) = when (notification.type) {
                "promo" -> Icons.Filled.LocalOffer to Secondary
                "order" -> Icons.Filled.ShoppingBag to Tertiary
                else -> Icons.Filled.Notifications to Primary
            }

            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(color.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(24.dp))
            }

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Text(text = notification.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = OnSurface)
                    Text(text = notification.time, style = MaterialTheme.typography.labelSmall, color = OnSurfaceVariant)
                }
                Spacer(Modifier.height(4.dp))
                Text(text = notification.message, style = MaterialTheme.typography.bodyMedium, color = OnSurfaceVariant, lineHeight = 20.sp)
                
                if (!notification.isRead) {
                    Spacer(Modifier.height(12.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(
                            onClick = onViewDetails,
                            modifier = Modifier.height(32.dp),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 0.dp),
                            shape = CircleShape,
                            colors = ButtonDefaults.buttonColors(containerColor = Primary)
                        ) {
                            Text(stringResource(R.string.view_details), fontSize = 12.sp)
                        }
                        OutlinedButton(
                            onClick = onDismiss,
                            modifier = Modifier.height(32.dp),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 0.dp),
                            shape = CircleShape
                        ) {
                            Text(stringResource(R.string.dismiss), fontSize = 12.sp)
                        }
                    }
                }
            }
        }
    }
}
