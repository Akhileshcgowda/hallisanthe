package com.example.hallisanthe.ui.screens

import androidx.compose.ui.res.stringResource
import com.example.hallisanthe.R
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Help
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.LocalMall
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.hallisanthe.data.LanguageManager
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.hallisanthe.data.viewmodel.AuthViewModel
import com.example.hallisanthe.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(navController: NavController, isSeller: MutableState<Boolean>, authViewModel: AuthViewModel) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val savedLang by LanguageManager.getLanguageFlow(context).collectAsState(initial = "en")
    val currentLangName = LanguageManager.getDisplayName(savedLang)
    Scaffold(
        containerColor = Background,
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("santhe_ai_assistant") },
                containerColor = SecondaryContainer,
                contentColor = Color(0xFF2A1800), // OnSecondaryContainer
                shape = RoundedCornerShape(16.dp),
                elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 4.dp),
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                Icon(Icons.Filled.SmartToy, contentDescription = stringResource(R.string.ai_title), modifier = Modifier.size(28.dp))
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Profile Header Card
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(4.dp, RoundedCornerShape(12.dp), spotColor = Primary)
                    .background(Primary, RoundedCornerShape(12.dp))
            ) {
                // Kolam Pattern Background
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .kolamBackground(Color.White.copy(alpha = 0.1f))
                )

                Row(
                    modifier = Modifier.padding(24.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    val currentUser by authViewModel.currentUser.collectAsState()
                    val userPhotoUrl = currentUser?.photoUrl?.toString() ?: ""
                    val userName = currentUser?.displayName ?: stringResource(R.string.greeting_guest)
                    val userEmail = currentUser?.email ?: ""

                    Box(
                        modifier = Modifier
                            .size(96.dp)
                            .border(4.dp, OnPrimary, CircleShape)
                            .clip(CircleShape)
                    ) {
                        if (userPhotoUrl.isNotBlank()) {
                            AsyncImage(
                                model = userPhotoUrl,
                                contentDescription = stringResource(R.string.profile_picture),
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            Box(
                                modifier = Modifier.fillMaxSize().background(OnPrimary.copy(alpha = 0.3f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    userName.take(1).uppercase(),
                                    style = MaterialTheme.typography.headlineLarge,
                                    color = OnPrimary,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                    Column {
                        Text(
                            userName,
                            style = MaterialTheme.typography.headlineMedium,
                            color = OnPrimary,
                            fontWeight = FontWeight.Bold
                        )
                        if (userEmail.isNotBlank()) {
                            Text(
                                userEmail,
                                fontSize = 14.sp,
                                color = Color(0xFFFFB595)
                            )
                        }
                        Spacer(Modifier.height(8.dp))

                    }
                }
            }

            // Role Toggle
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .background(Color(0xFFFFF0E6), RoundedCornerShape(24.dp))
                    .padding(4.dp)
            ) {
                // Buyer Mode
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .background(if (!isSeller.value) Color(0xFF993300) else Color.Transparent, RoundedCornerShape(20.dp))
                        .clickable {
                            isSeller.value = false
                            navController.navigate("home") { popUpTo(0) }
                        },
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text("🛍️", fontSize = 18.sp, modifier = Modifier.padding(end = 8.dp).let { if(isSeller.value) it.alpha(0.6f) else it })
                    Text(
                        stringResource(R.string.buyer_mode),
                        color = if (!isSeller.value) Color.White else OnSurfaceVariant,
                        fontWeight = if (!isSeller.value) FontWeight.Bold else FontWeight.SemiBold,
                        fontSize = 12.sp
                    )
                }

                // Seller Mode
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .background(if (isSeller.value) Color(0xFF993300) else Color.Transparent, RoundedCornerShape(20.dp))
                        .clickable {
                            isSeller.value = true
                            navController.navigate("seller_home") { popUpTo(0) }
                        },
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text("🏪", fontSize = 18.sp, modifier = Modifier.padding(end = 8.dp).let { if(!isSeller.value) it.alpha(0.6f) else it })
                    Text(
                        stringResource(R.string.seller_mode),
                        color = if (isSeller.value) Color.White else OnSurfaceVariant,
                        fontWeight = if (isSeller.value) FontWeight.Bold else FontWeight.SemiBold,
                        fontSize = 12.sp
                    )
                }
            }

            // Menu List
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                ProfileMenuItem(stringResource(R.string.my_orders), stringResource(R.string.my_orders_desc), Icons.Filled.LocalMall, onClick = { navController.navigate("orders") })
                ProfileMenuItem(stringResource(R.string.wishlist), stringResource(R.string.wishlist_desc), Icons.Filled.Favorite, onClick = { navController.navigate("wishlist") })
                ProfileMenuItem(stringResource(R.string.notifications), stringResource(R.string.notifications_desc), Icons.Filled.Notifications, hasNotification = true, onClick = { navController.navigate("notifications") })
                ProfileMenuItem(stringResource(R.string.language), currentLangName, Icons.Filled.Language, onClick = { navController.navigate("language") })
                ProfileMenuItem(stringResource(R.string.help_support), stringResource(R.string.help_support_desc), Icons.AutoMirrored.Filled.Help, onClick = { navController.navigate("help") })
            }

            // Spacer before logout
            Spacer(modifier = Modifier.height(16.dp))

            // Decorative Divider
            Box(modifier = Modifier.fillMaxWidth().height(24.dp).padding(vertical = 8.dp), contentAlignment = Alignment.Center) {
                androidx.compose.foundation.Canvas(modifier = Modifier.fillMaxWidth(0.75f).height(1.dp)) {
                    drawLine(
                        color = Color.LightGray,
                        start = androidx.compose.ui.geometry.Offset(0f, 0f),
                        end = androidx.compose.ui.geometry.Offset(size.width, 0f),
                        pathEffect = androidx.compose.ui.graphics.PathEffect.dashPathEffect(floatArrayOf(5f, 5f), 0f)
                    )
                }
            }

            // Logout Button
            Button(
                onClick = {
                    authViewModel.signOut {
                        isSeller.value = false
                        navController.navigate("splash") {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                border = androidx.compose.foundation.BorderStroke(1.dp, Error.copy(alpha = 0.5f)),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
            ) {
                Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = null, modifier = Modifier.size(18.dp), tint = Error)
                Spacer(Modifier.width(8.dp))
                Text(stringResource(R.string.logout), fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = Error)
            }
        }
    }
}

fun Modifier.alpha(alpha: Float): Modifier = this

@Composable
fun ProfileMenuItem(title: String, subtitle: String, icon: ImageVector, hasNotification: Boolean = false, isFullWidth: Boolean = false, onClick: () -> Unit = {}) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(2.dp, RoundedCornerShape(12.dp), spotColor = Primary)
            .background(Color.White, RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(Color(0xFFFFF0E6), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = title, tint = Color(0xFF7C2E00))
                if (hasNotification) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(top = 8.dp, end = 8.dp)
                            .size(8.dp)
                            .background(Error, CircleShape)
                    )
                }
            }
            Column {
                Text(title, style = MaterialTheme.typography.titleMedium, color = OnSurface, fontWeight = FontWeight.SemiBold)
                Text(subtitle, fontSize = 14.sp, color = OnSurfaceVariant)
            }
        }
        Icon(Icons.Filled.ChevronRight, contentDescription = null, tint = OutlineVariant)
    }
}
