package com.example.hallisanthe.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.with
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.hallisanthe.R
import com.example.hallisanthe.ui.theme.*

data class OnboardingPage(
    val title: String,
    val description: String,
    val imageRes: Int
)

val onboardingPages = listOf(
    OnboardingPage(
        title = "Heritage at your Doorstep",
        description = "Bring the rich culture of Karnataka into your home with curated traditional crafts.",
        imageRes = R.drawable.onboarding_heritage
    ),
    OnboardingPage(
        title = "Support Local Artisans",
        description = "Every purchase directly supports rural families and preserves ancient craftsmanship skills.",
        imageRes = R.drawable.onboarding_artisan
    ),
    OnboardingPage(
        title = "Discover Local Gems",
        description = "Find authentic handmade products straight from Karnataka's finest artisans.",
        imageRes = R.drawable.onboarding_discover
    )
)

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun OnboardingScreen(navController: NavController) {
    var currentPage by remember { mutableStateOf(0) }
    val page = onboardingPages[currentPage]

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
    ) {
        // Kolam background pattern
        Box(
            modifier = Modifier
                .fillMaxSize()
                .kolamBackground(OutlineVariant.copy(alpha = 0.3f))
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.weight(1f))

            // Animated Content for the main illustration and text
            AnimatedContent(
                targetState = currentPage,
                transitionSpec = {
                    fadeIn(animationSpec = tween(500)) with fadeOut(animationSpec = tween(500))
                }
            ) { targetPage ->
                val targetData = onboardingPages[targetPage]
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    // Main Illustration
                    Box(
                        modifier = Modifier
                            .size(320.dp)
                            .shadow(elevation = 12.dp, shape = RoundedCornerShape(12.dp), spotColor = Primary, ambientColor = Primary)
                            .background(SurfaceContainerLowest, RoundedCornerShape(12.dp))
                            .padding(4.dp)
                    ) {
                        Image(
                            painter = painterResource(id = targetData.imageRes),
                            contentDescription = targetData.title,
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(RoundedCornerShape(8.dp)),
                            contentScale = ContentScale.Crop
                        )
                    }

                    Spacer(modifier = Modifier.height(40.dp))

                    // Text
                    Text(
                        text = targetData.title,
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.Bold,
                        color = OnBackground,
                        textAlign = TextAlign.Center
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Text(
                        text = targetData.description,
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center,
                        color = OnSurfaceVariant,
                        modifier = Modifier.width(320.dp),
                        lineHeight = 24.sp
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Footer
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Pager Indicators
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    onboardingPages.forEachIndexed { index, _ ->
                        val isSelected = index == currentPage
                        Box(
                            modifier = Modifier
                                .size(width = if (isSelected) 24.dp else 8.dp, height = 8.dp)
                                .background(if (isSelected) Primary else SurfaceVariant, CircleShape)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(40.dp))

                // Action Button
                Button(
                    onClick = { 
                        if (currentPage < onboardingPages.size - 1) {
                            currentPage++
                        } else {
                            navController.navigate("login") 
                        }
                    },
                    modifier = Modifier
                        .width(320.dp)
                        .height(60.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Primary, contentColor = OnPrimary),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            if (currentPage < onboardingPages.size - 1) stringResource(R.string.next) else stringResource(R.string.get_started),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                        if (currentPage < onboardingPages.size - 1) {
                            Spacer(Modifier.width(8.dp))
                            Icon(Icons.Filled.ArrowForward, contentDescription = null)
                        }
                    }
                }
                
                if (currentPage < onboardingPages.size - 1) {
                    TextButton(
                        onClick = { navController.navigate("login") },
                        modifier = Modifier.padding(top = 8.dp)
                    ) {
                        Text(stringResource(R.string.skip), color = OnSurfaceVariant, fontWeight = FontWeight.Medium)
                    }
                } else {
                    Spacer(Modifier.height(48.dp))
                }
            }
        }
    }
}

// Re-importing KolamBackground since it was part of the file
fun Modifier.kolamBackground(color: Color): Modifier = this.drawBehind {
    val radius = 1.dp.toPx()
    val step = 16.dp.toPx()
    val width = size.width
    val height = size.height

    var y = 0f
    while (y < height) {
        var x = 0f
        while (x < width) {
            drawCircle(color = color, radius = radius, center = Offset(x, y))
            x += step
        }
        y += step
    }
}
