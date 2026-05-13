package com.example.hallisanthe.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.compose.ui.res.stringResource
import com.example.hallisanthe.R
import com.example.hallisanthe.ui.theme.*

@Composable
fun SellerOnboardingScreen(navController: NavController, isSeller: MutableState<Boolean>? = null) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
    ) {
        // Decorative Pattern (Simplified with a Box for now)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Transparent)
        )

        Column(modifier = Modifier.fillMaxSize()) {
            // Top Section: Image and Logo
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(500.dp)
                    .clip(RoundedCornerShape(bottomStart = 40.dp, bottomEnd = 40.dp))
                    .background(SurfaceContainer)
            ) {
                // Image Placeholder
                Image(
                    painter = painterResource(id = R.drawable.onboarding_artisan),
                    contentDescription = stringResource(R.string.sell_your_craft),
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                // Logo
                Surface(
                    modifier = Modifier
                        .padding(24.dp)
                        .align(Alignment.TopStart),
                    color = Surface.copy(alpha = 0.8f),
                    shape = CircleShape,
                    shadowElevation = 4.dp
                ) {
                    Box(modifier = Modifier.padding(8.dp)) {
                        // Logo Icon Placeholder
                        Icon(Icons.Filled.ArrowForward, contentDescription = null, tint = Primary, modifier = Modifier.size(32.dp))
                    }
                }
            }

            // Bottom Section: Content Card
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 32.dp),
                contentAlignment = Alignment.BottomCenter
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .offset(y = (-48).dp),
                    shape = RoundedCornerShape(32.dp),
                    colors = CardDefaults.cardColors(containerColor = SurfaceContainerLowest),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // Kolam Divider
                        Box(
                            modifier = Modifier
                                .width(64.dp)
                                .height(4.dp)
                                .clip(CircleShape)
                                .background(SurfaceVariant)
                        )

                        Text(
                            stringResource(R.string.sell_your_craft),
                            style = MaterialTheme.typography.headlineLarge,
                            color = Primary,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            stringResource(R.string.seller_onboarding_body),
                            style = MaterialTheme.typography.bodyLarge,
                            color = OnSurfaceVariant,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth(0.9f)
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Button(
                            onClick = { navController.navigate("seller_login_register") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp),
                            shape = RoundedCornerShape(24.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Primary)
                        ) {
                            Text(stringResource(R.string.get_started), fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
                            Spacer(modifier = Modifier.width(8.dp))
                            Icon(Icons.Filled.ArrowForward, contentDescription = null)
                        }

                        TextButton(onClick = { navController.navigate("seller_login_register") }) {
                            Text(
                                stringResource(R.string.already_have_account),
                                style = MaterialTheme.typography.labelLarge,
                                color = Primary,
                                textDecoration = TextDecoration.Underline
                            )
                        }
                    }
                }
            }
        }
    }
}
