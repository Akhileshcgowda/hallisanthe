package com.example.hallisanthe.ui.screens

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
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
import com.example.hallisanthe.data.CartViewModel
import com.example.hallisanthe.data.WishlistViewModel
import com.example.hallisanthe.data.viewmodel.SantheAIViewModel
import com.example.hallisanthe.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SantheAIAssistantScreen(
    navController: NavController,
    viewModel: SantheAIViewModel,
    cartViewModel: CartViewModel? = null,
    wishlistViewModel: WishlistViewModel? = null,
    isSeller: Boolean = false
) {
    var messageText by remember { mutableStateOf("") }
    val messages by viewModel.messages.collectAsState()
    val isTyping by viewModel.isTyping.collectAsState()
    val listState = androidx.compose.foundation.lazy.rememberLazyListState()

    // Switch to buyer or seller chat mode on launch
    LaunchedEffect(isSeller) {
        viewModel.switchMode(isSeller)
    }

    // Sync cart and wishlist data into AI ViewModel so replies can reference them
    LaunchedEffect(
        cartViewModel?.cartItems?.size,
        wishlistViewModel?.wishlistItems?.size
    ) {
        viewModel.updateCartAndWishlist(
            cart = cartViewModel?.cartItems?.toList() ?: emptyList(),
            wishlist = wishlistViewModel?.wishlistItems?.toList() ?: emptyList()
        )
    }

    // Auto-scroll to bottom when new messages arrive
    LaunchedEffect(messages.size, isTyping) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    Scaffold(
        containerColor = Background,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Icon(Icons.Filled.AutoAwesome, contentDescription = null, tint = Primary)
                        Text(stringResource(R.string.ai_title), fontWeight = FontWeight.Bold, color = Primary)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(R.string.back))
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.clearChat() }) {
                        Icon(Icons.Filled.Refresh, contentDescription = stringResource(R.string.ai_clear_chat), tint = OnSurfaceVariant)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = SurfaceContainerLow)
            )
        },
        bottomBar = {
            Surface(
                tonalElevation = 2.dp,
                shadowElevation = 8.dp,
                color = Surface,
                modifier = Modifier.imePadding()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = messageText,
                        onValueChange = { messageText = it },
                        placeholder = { Text(stringResource(R.string.ai_placeholder)) },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(24.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = SurfaceContainerLowest,
                            unfocusedContainerColor = SurfaceContainerLowest,
                            focusedBorderColor = Primary
                        ),
                        maxLines = 4
                    )
                    IconButton(
                        onClick = {
                            if (messageText.isNotBlank()) {
                                viewModel.sendMessage(messageText)
                                messageText = ""
                            }
                        },
                        modifier = Modifier
                            .size(56.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(Primary),
                        colors = IconButtonDefaults.iconButtonColors(contentColor = OnPrimary)
                    ) {
                        Icon(Icons.AutoMirrored.Filled.Send, contentDescription = stringResource(R.string.send))
                    }
                }
            }
        }
    ) { paddingValues ->
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    Surface(
                        color = SurfaceContainerLow,
                        shape = CircleShape,
                        border = BorderStroke(1.dp, OutlineVariant)
                    ) {
                        Text(
                            stringResource(R.string.ai_today),
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
                            style = MaterialTheme.typography.labelLarge,
                            color = OnSurfaceVariant
                        )
                    }
                }
            }

            // Quick suggestion chips (only show when chat is nearly empty)
            if (messages.size <= 2) {
                item {
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.padding(bottom = 8.dp)
                    ) {
                        item {
                            SuggestionChip(stringResource(R.string.suggestion_upload)) {
                                viewModel.sendMessage("How do I upload a product?")
                            }
                        }
                        item {
                            SuggestionChip(stringResource(R.string.suggestion_silk)) {
                                viewModel.sendMessage("How do I find silk sarees?")
                            }
                        }
                        item {
                            SuggestionChip(stringResource(R.string.suggestion_track)) {
                                viewModel.sendMessage("How do I track my order?")
                            }
                        }
                        item {
                            SuggestionChip(stringResource(R.string.suggestion_settings)) {
                                viewModel.sendMessage("How do I update shop settings?")
                            }
                        }
                    }
                }
            }

            items(messages, key = { it.id }) { msg ->
                ChatBubble(isAi = !msg.isUser, message = msg.text)
            }

            if (isTyping) {
                item {
                    Row(
                        modifier = Modifier.padding(start = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(SecondaryContainer),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Filled.AutoAwesome,
                                contentDescription = null,
                                tint = OnSecondaryContainer,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Box(
                            modifier = Modifier
                                .size(60.dp, 36.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Surface)
                                .border(1.dp, OutlineVariant, RoundedCornerShape(12.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                repeat(3) { index ->
                                    val alpha by rememberInfiniteTransition(label = "typing").animateFloat(
                                        initialValue = 0.3f,
                                        targetValue = 1f,
                                        animationSpec = infiniteRepeatable(
                                            animation = tween(600, delayMillis = index * 150),
                                            repeatMode = RepeatMode.Reverse
                                        ),
                                        label = "dot$index"
                                    )
                                    Box(
                                        modifier = Modifier
                                            .size(6.dp)
                                            .clip(CircleShape)
                                            .background(Primary.copy(alpha = alpha))
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ChatBubble(isAi: Boolean, message: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isAi) Arrangement.Start else Arrangement.End,
        verticalAlignment = Alignment.Top
    ) {
        if (isAi) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(SecondaryContainer)
                    .border(1.dp, Secondary, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Filled.AutoAwesome,
                    contentDescription = null,
                    tint = OnSecondaryContainer,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
        }

        Column(
            modifier = Modifier.weight(1f, fill = false),
            horizontalAlignment = if (isAi) Alignment.Start else Alignment.End
        ) {
            Text(
                text = if (isAi) stringResource(R.string.ai_title) else stringResource(R.string.ai_you),
                style = MaterialTheme.typography.labelLarge,
                color = OnSurfaceVariant,
                modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
            )
            Surface(
                color = if (isAi) Surface else Primary,
                shape = RoundedCornerShape(
                    topStart = if (isAi) 4.dp else 16.dp,
                    topEnd = if (isAi) 16.dp else 4.dp,
                    bottomStart = 16.dp,
                    bottomEnd = 16.dp
                ),
                border = if (isAi) BorderStroke(1.dp, OutlineVariant) else null,
                shadowElevation = 2.dp
            ) {
                Text(
                    text = message,
                    modifier = Modifier.padding(12.dp),
                    style = MaterialTheme.typography.bodyLarge,
                    color = if (isAi) OnSurface else OnPrimary
                )
            }
        }

        if (!isAi) {
            Spacer(modifier = Modifier.width(8.dp))
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(SurfaceVariant)
            )
        }
    }
}

@Composable
fun SuggestionChip(label: String, onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        shape = CircleShape,
        border = BorderStroke(1.dp, PrimaryFixedDim),
        color = Surface,
        shadowElevation = 1.dp
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(Icons.Filled.Lightbulb, contentDescription = null, modifier = Modifier.size(16.dp), tint = Secondary)
            Text(label, style = MaterialTheme.typography.labelLarge, color = Secondary)
        }
    }
}
