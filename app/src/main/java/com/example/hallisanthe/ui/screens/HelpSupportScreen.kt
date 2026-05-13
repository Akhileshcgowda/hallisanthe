package com.example.hallisanthe.ui.screens

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.compose.ui.res.stringResource
import com.example.hallisanthe.R
import com.example.hallisanthe.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HelpSupportScreen(navController: NavController) {
    Scaffold(
        containerColor = Background
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // FAQ Section Header
            Column {
                Text(stringResource(R.string.faq), style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = OnSurface)
                Box(modifier = Modifier.padding(top = 4.dp).width(40.dp).height(3.dp).background(Secondary, CircleShape))
            }
            
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                FAQItem(stringResource(R.string.faq_question_1), stringResource(R.string.faq_answer_1))
                FAQItem(stringResource(R.string.faq_question_2), stringResource(R.string.faq_answer_2))
                FAQItem(stringResource(R.string.faq_question_3), stringResource(R.string.faq_answer_3))
            }

            // Kolam Divider
            Box(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp), contentAlignment = Alignment.Center) {
                Text("◆ ◇ ◆ ◇ ◆", color = OutlineVariant, letterSpacing = 8.sp, fontSize = 14.sp)
            }

            Text(stringResource(R.string.contact_us), style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = OnSurface)
            
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                ContactMethod(Icons.Filled.Email, stringResource(R.string.email_us), "support@hallisanthe.com", SecondaryContainer)
                ContactMethod(Icons.Filled.Call, stringResource(R.string.call_us), "+91 80 1234 5678", PrimaryContainer)
                ContactMethod(Icons.Filled.Chat, stringResource(R.string.live_chat), "Available 9 AM - 6 PM", TertiaryContainer)
            }

            Spacer(Modifier.height(8.dp))
            
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { },
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceContainerLow),
                border = androidx.compose.foundation.BorderStroke(1.dp, OutlineVariant)
            ) {
                Row(
                    modifier = Modifier.padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(SurfaceVariant),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Filled.Description, contentDescription = null, tint = OnSurfaceVariant)
                    }
                    Column {
                        Text(stringResource(R.string.legal_info), style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                        Text(stringResource(R.string.read_terms), style = MaterialTheme.typography.bodySmall, color = OnSurfaceVariant)
                    }
                    Spacer(Modifier.weight(1f))
                    Icon(Icons.Filled.ChevronRight, contentDescription = null, tint = Outline)
                }
            }
            
            Spacer(Modifier.height(32.dp))
        }
    }
}

@Composable
fun FAQItem(question: String, answer: String) {
    var expanded by androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf(false) }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { expanded = !expanded },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceContainerLowest),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                Text(text = question, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = OnSurface, modifier = Modifier.weight(1f))
                Icon(
                    if (expanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                    contentDescription = null,
                    tint = Secondary
                )
            }
            if (expanded) {
                Spacer(Modifier.height(8.dp))
                Text(text = answer, style = MaterialTheme.typography.bodyMedium, color = OnSurfaceVariant)
            }
        }
    }
}

@Composable
fun ContactMethod(icon: ImageVector, title: String, value: String, iconBg: Color) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceContainerLowest),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, OutlineVariant.copy(alpha = 0.5f))
    ) {
        Row(
            modifier = Modifier
                .clickable { }
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(iconBg.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = iconBg.copy(alpha = 0.8f).toDarker())
            }
            Column {
                Text(text = title, style = MaterialTheme.typography.labelLarge, color = OnSurfaceVariant)
                Text(text = value, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = OnSurface)
            }
            Spacer(Modifier.weight(1f))
            Icon(Icons.Filled.ChevronRight, contentDescription = null, tint = OutlineVariant)
        }
    }
}

// Helper to darken a color slightly for better contrast on light backgrounds
@Composable
fun Color.toDarker(): Color {
    return this
}
