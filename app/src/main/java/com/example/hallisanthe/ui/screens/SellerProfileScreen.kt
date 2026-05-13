package com.example.hallisanthe.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.compose.ui.res.stringResource
import com.example.hallisanthe.R
import com.example.hallisanthe.data.viewmodel.AuthViewModel
import com.example.hallisanthe.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SellerProfileScreen(navController: NavController, isSeller: MutableState<Boolean>, viewModel: com.example.hallisanthe.data.viewmodel.SellerDashboardViewModel, authViewModel: AuthViewModel) {
    val metrics by viewModel.metrics.collectAsState()
    val currentUser by authViewModel.currentUser.collectAsState()
    Scaffold(
        containerColor = Background,
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("santhe_ai_assistant") },
                containerColor = Secondary,
                contentColor = OnSecondary,
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.padding(bottom = 80.dp)
            ) {
                Icon(Icons.Filled.SmartToy, contentDescription = stringResource(R.string.ai_title), modifier = Modifier.size(32.dp))
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Profile Header
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(96.dp)
                        .clip(CircleShape)
                        .border(4.dp, Primary, CircleShape)
                        .background(SurfaceVariant)
                ) {
                    // Avatar Placeholder
                }
                Text(stringResource(R.string.my_shop), style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, color = OnSurface)
                Text(
                    currentUser?.displayName ?: currentUser?.email ?: stringResource(R.string.default_seller_name),
                    style = MaterialTheme.typography.bodyLarge,
                    color = OnSurfaceVariant
                )
                Text(
                    currentUser?.phoneNumber ?: "",
                    style = MaterialTheme.typography.bodyMedium,
                    color = OnSurfaceVariant
                )
            }

            // Role Toggle
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 320.dp)
                    .height(48.dp)
                    .clip(CircleShape)
                    .background(SurfaceVariant)
                    .padding(4.dp)
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .clip(CircleShape)
                        .clickable { 
                            isSeller.value = false
                            navController.navigate("home") {
                                popUpTo(0) { inclusive = true }
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        Icon(Icons.Filled.ShoppingBag, contentDescription = null, modifier = Modifier.size(18.dp), tint = OnSurfaceVariant)
                        Text(stringResource(R.string.buyer_mode), style = MaterialTheme.typography.labelLarge, color = OnSurfaceVariant)
                    }
                }
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .clip(CircleShape)
                        .background(Primary)
                        .clickable { },
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        Icon(Icons.Filled.Storefront, contentDescription = null, modifier = Modifier.size(18.dp), tint = OnPrimary)
                        Text(stringResource(R.string.seller_mode), style = MaterialTheme.typography.labelLarge, color = OnPrimary)
                    }
                }
            }

            // Kolam Divider
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                Text("◆ ◇ ◆ ◇ ◆", color = Outline, letterSpacing = 8.sp, fontSize = 14.sp)
            }

            // Bento Grid Menu
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    BentoCard(
                        modifier = Modifier.weight(1f).aspectRatio(1f),
                        title = stringResource(R.string.my_listings),
                        subtitle = "${metrics.activeListings} ${stringResource(R.string.active_products)}",
                        icon = Icons.Filled.Inventory2,
                        iconContainerColor = PrimaryContainer,
                        onIconColor = OnPrimaryContainer,
                        onClick = { navController.navigate("my_listings") }
                    )
                    BentoCard(
                        modifier = Modifier.weight(1f).aspectRatio(1f),
                        title = stringResource(R.string.shop_settings),
                        subtitle = "",
                        icon = Icons.Filled.Store,
                        iconContainerColor = SecondaryContainer,
                        onIconColor = OnSecondaryContainer,
                        onClick = { navController.navigate("shop_settings") }
                    )
                }

                // AI Assistant Card
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { navController.navigate("santhe_ai_assistant") },
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .background(
                                brush = Brush.linearGradient(
                                    colors = listOf(SecondaryContainer, PrimaryContainer)
                                )
                            )
                            .padding(16.dp)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(CircleShape)
                                    .background(SurfaceContainerLowest),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Filled.AutoAwesome, contentDescription = null, tint = Primary)
                            }
                            Column {
                                Text(stringResource(R.string.seller_ai_assistant), style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = OnPrimary)
                                Text(stringResource(R.string.optimize_insights), style = MaterialTheme.typography.bodyMedium, color = OnPrimary.copy(alpha = 0.9f))
                            }
                        }
                        Icon(Icons.Filled.ChevronRight, contentDescription = null, tint = OnPrimary)
                    }
                }

                // List Items
                ProfileListItem(
                    title = stringResource(R.string.notifications),
                    icon = Icons.Filled.NotificationsActive,
                    iconContainerColor = SurfaceVariant,
                    badgeCount = metrics.pendingInquiries,
                    onClick = { navController.navigate("notifications") }
                )

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    BentoCardSmall(
                        modifier = Modifier.weight(1f),
                        title = stringResource(R.string.language),
                        icon = Icons.Filled.Language,
                        onClick = { navController.navigate("language") }
                    )
                    BentoCardSmall(
                        modifier = Modifier.weight(1f),
                        title = stringResource(R.string.help_support),
                        icon = Icons.Filled.HelpOutline,
                        onClick = { navController.navigate("help") }
                    )
                }
            }

            // Logout
            OutlinedButton(
                onClick = { 
                    isSeller.value = false
                    navController.navigate("splash") {
                        popUpTo(0) { inclusive = true }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = CircleShape,
                border = BorderStroke(2.dp, OutlineVariant)
            ) {
                Icon(Icons.Filled.Logout, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(stringResource(R.string.logout), style = MaterialTheme.typography.titleLarge)
            }
            
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun BentoCard(
    modifier: Modifier = Modifier,
    title: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconContainerColor: Color,
    onIconColor: Color,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier.clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = SurfaceContainerLowest),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(iconContainerColor),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = onIconColor, modifier = Modifier.size(32.dp))
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = OnSurface, textAlign = TextAlign.Center)
            if (subtitle.isNotEmpty()) {
                Text(subtitle, style = MaterialTheme.typography.labelLarge, color = OnSurfaceVariant)
            }
        }
    }
}

@Composable
fun BentoCardSmall(
    modifier: Modifier = Modifier,
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier.clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = SurfaceContainerLowest),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(icon, contentDescription = null, tint = OnSurfaceVariant, modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.height(8.dp))
            Text(title, style = MaterialTheme.typography.bodyLarge, color = OnSurface)
        }
    }
}

@Composable
fun ProfileListItem(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconContainerColor: Color,
    badgeCount: Int = 0,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = SurfaceContainerLowest),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(iconContainerColor),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(icon, contentDescription = null, tint = OnSurfaceVariant)
                }
                Text(title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = OnSurface)
            }
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                if (badgeCount > 0) {
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(Color(0xFFBA1A1A))
                            .padding(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Text(badgeCount.toString(), color = Color.White, style = MaterialTheme.typography.labelLarge)
                    }
                }
                Icon(Icons.Filled.ChevronRight, contentDescription = null, tint = OnSurfaceVariant)
            }
        }
    }
}
