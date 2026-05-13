package com.example.hallisanthe.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.compose.ui.res.stringResource
import com.example.hallisanthe.R
import com.example.hallisanthe.data.Inquiry
import com.example.hallisanthe.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InquiriesScreen(navController: NavController, viewModel: com.example.hallisanthe.data.viewmodel.SellerDashboardViewModel) {
    var searchQuery by remember { mutableStateOf("") }
    val inquiries by viewModel.inquiries.collectAsState(initial = emptyList())
    
    val filteredInquiries = inquiries.filter { 
        it.customerName.contains(searchQuery, ignoreCase = true) || 
        it.productName.contains(searchQuery, ignoreCase = true) 
    }

    Scaffold(
        containerColor = Background,
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("santhe_ai_assistant") },
                containerColor = Primary,
                contentColor = OnPrimary,
                shape = CircleShape
            ) {
                Icon(Icons.Filled.AutoAwesome, contentDescription = stringResource(R.string.ai_title))
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Search & Filter
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text(stringResource(R.string.search_by_name)) },
                    leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null, tint = Outline) },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(28.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = SurfaceContainerLowest,
                        unfocusedContainerColor = SurfaceContainerLowest,
                        focusedBorderColor = Primary,
                        unfocusedBorderColor = OutlineVariant
                    ),
                    singleLine = true
                )
                Surface(
                    modifier = Modifier.size(56.dp),
                    shape = CircleShape,
                    color = SurfaceContainerLow,
                    border = androidx.compose.foundation.BorderStroke(1.dp, OutlineVariant),
                    onClick = { /* Filter Logic */ }
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(Icons.Filled.FilterList, contentDescription = stringResource(R.string.filter), tint = OnSurface)
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
            
            // Stats Row
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                InquiryStatChip(stringResource(R.string.all), inquiries.size, true)
                InquiryStatChip(stringResource(R.string.unread), inquiries.count { it.isUnread }, false)
                InquiryStatChip(stringResource(R.string.replied), 0, false)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Inquiries List
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(bottom = 100.dp)
            ) {
                items(filteredInquiries) { inquiry ->
                    InquiryCard(inquiry, onRead = { viewModel.markInquiryAsRead(inquiry.id) })
                }
            }
        }
    }
}

@Composable
fun InquiryStatChip(label: String, count: Int, isSelected: Boolean) {
    Surface(
        color = if (isSelected) Primary else SurfaceContainerLow,
        shape = CircleShape,
        border = if (isSelected) null else androidx.compose.foundation.BorderStroke(1.dp, OutlineVariant)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(label, style = MaterialTheme.typography.labelLarge, color = if (isSelected) OnPrimary else OnSurfaceVariant)
            Surface(
                color = if (isSelected) OnPrimary.copy(alpha = 0.2f) else OutlineVariant,
                shape = CircleShape
            ) {
                Text(count.toString(), modifier = Modifier.padding(horizontal = 6.dp), style = MaterialTheme.typography.labelSmall, color = if (isSelected) OnPrimary else OnSurface)
            }
        }
    }
}

@Composable
fun InquiryCard(inquiry: Inquiry, onRead: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (inquiry.isUnread) SurfaceContainerLow else SurfaceContainerLowest
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = if (inquiry.isUnread) 4.dp else 1.dp),
        shape = RoundedCornerShape(20.dp),
        border = if (inquiry.isUnread) null else androidx.compose.foundation.BorderStroke(1.dp, OutlineVariant)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onRead() }
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.Top
        ) {
            // Avatar
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(if (inquiry.isUnread) Primary.copy(alpha = 0.1f) else SurfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = inquiry.customerName.take(1),
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = if (inquiry.isUnread) Primary else OnSurfaceVariant
                )
                if (inquiry.isUnread) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .size(14.dp)
                            .clip(CircleShape)
                            .background(Primary)
                            .border(2.dp, SurfaceContainerLow, CircleShape)
                    )
                }
            }

            // Content
            Column(modifier = Modifier.weight(1f)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text(inquiry.customerName, style = MaterialTheme.typography.titleLarge, color = OnSurface, fontWeight = if (inquiry.isUnread) FontWeight.Bold else FontWeight.SemiBold)
                    Text(inquiry.time, style = MaterialTheme.typography.labelSmall, color = OnSurfaceVariant)
                }
                
                Surface(
                    modifier = Modifier.padding(vertical = 4.dp),
                    color = TertiaryContainer.copy(alpha = 0.3f),
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Row(modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        Icon(Icons.Filled.Inventory2, contentDescription = null, tint = Tertiary, modifier = Modifier.size(12.dp))
                        Text(inquiry.productName, style = MaterialTheme.typography.labelSmall, color = Tertiary, fontWeight = FontWeight.Bold)
                    }
                }

                Text(
                    text = inquiry.lastMessage,
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (inquiry.isUnread) OnSurface else OnSurfaceVariant,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 20.sp
                )
                
                if (inquiry.isUnread) {
                    Spacer(Modifier.height(12.dp))
                    Button(
                        onClick = { onRead() },
                        modifier = Modifier.height(36.dp),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 0.dp),
                        shape = CircleShape,
                        colors = ButtonDefaults.buttonColors(containerColor = Secondary)
                    ) {
                        Text(stringResource(R.string.reply_now), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
