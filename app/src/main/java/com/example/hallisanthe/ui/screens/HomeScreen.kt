package com.example.hallisanthe.ui.screens

import com.example.hallisanthe.R
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddShoppingCart
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.ScatterPlot
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.hallisanthe.data.CartViewModel
import com.example.hallisanthe.data.WishlistViewModel
import com.example.hallisanthe.data.viewmodel.AuthViewModel
import com.example.hallisanthe.data.viewmodel.ProductCatalogViewModel
import com.example.hallisanthe.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController, cartViewModel: CartViewModel, wishlistViewModel: WishlistViewModel, catalogViewModel: ProductCatalogViewModel, authViewModel: AuthViewModel) {
    val context = androidx.compose.ui.platform.LocalContext.current
    var selectedCategoryId by remember { mutableStateOf("all") }
    var searchQuery by remember { mutableStateOf("") }

    val allProducts by catalogViewModel.allProducts.collectAsState(initial = emptyList())
    val categories by catalogViewModel.allCategories.collectAsState(initial = emptyList())

    val filteredProducts = remember(selectedCategoryId, searchQuery, allProducts) {
        allProducts.filter { product ->
            (selectedCategoryId == "all" || product.category == selectedCategoryId) &&
            (searchQuery.isEmpty() || product.name.contains(searchQuery, ignoreCase = true) ||
             product.artisan.contains(searchQuery, ignoreCase = true))
        }
    }

    Box(modifier = Modifier.fillMaxSize().background(Background)) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 80.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item(span = { GridItemSpan(maxLineSpan) }) {
                // Greeting
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    val currentUser by authViewModel.currentUser.collectAsState()
                    val userName = currentUser?.displayName ?: stringResource(R.string.greeting_guest)
                    Column {
                        Text(
                            "${stringResource(R.string.greeting_namaskara)}, $userName! 🙏",
                            style = MaterialTheme.typography.headlineMedium,
                            color = Primary,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            stringResource(R.string.home_greeting_subtitle),
                            fontSize = 16.sp,
                            color = OnSurfaceVariant
                        )
                    }
                }
            }

            item(span = { GridItemSpan(maxLineSpan) }) {
                // Search Bar
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(4.dp, RoundedCornerShape(12.dp), spotColor = Primary),
                    placeholder = { Text(stringResource(R.string.search_hint), color = OnSurfaceVariant) },
                    leadingIcon = { Icon(Icons.Filled.Search, contentDescription = stringResource(R.string.search), tint = OnSurfaceVariant) },
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedContainerColor = SurfaceContainerLowest,
                        focusedContainerColor = SurfaceContainerLowest,
                        unfocusedBorderColor = Color(0xFFD9D4CD),
                        focusedBorderColor = Primary
                    ),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )
            }

            item(span = { GridItemSpan(maxLineSpan) }) {
                // Horizontal Categories
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(categories.size) { index ->
                        val category = categories[index]
                        val isSelected = selectedCategoryId == category.id
                        Text(
                            category.name,
                            modifier = Modifier
                                .shadow(if (isSelected) 4.dp else 2.dp, CircleShape, spotColor = Primary)
                                .background(if (isSelected) Primary else SurfaceContainerLowest, CircleShape)
                                .border(1.dp, if (isSelected) Primary else OutlineVariant, CircleShape)
                                .clickable { selectedCategoryId = category.id }
                                .padding(horizontal = 16.dp, vertical = 8.dp),
                            color = if (isSelected) OnPrimary else OnSurface,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 12.sp
                        )
                    }
                }
            }

            item(span = { GridItemSpan(maxLineSpan) }) {
                // Featured Banner
                if (selectedCategoryId == "all" || selectedCategoryId == "textiles") {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(21f / 9f)
                            .shadow(4.dp, RoundedCornerShape(12.dp), spotColor = Primary)
                            .clip(RoundedCornerShape(12.dp))
                    ) {
                        AsyncImage(
                            model = "https://lh3.googleusercontent.com/aida-public/AB6AXuCXqI5h8Ayw-ubyU6YWmfRRVvv0hpjrns4dfLN0KzdogXdtfUYpTUidmfivGplZenG6_cDvzp1W-qQ1PKVoG6ACi20FOYnKhQEixUaTKmXr6BrGkujnnDhIYIPDySRggJx8xKEG-NWpiUAbbTcVvAyqyLmzirYbQ3PU82sSnWwJY6ial104obw8xom_tU6JSPzs2k47GgSE-4ffzau7lEL82lBl38hySulFmWkbEYn2zO94U_f5t4-WSQ9RKcOeudIvBB1rh-RcaZo",
                            contentDescription = "Fresh from the Loom",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    Brush.horizontalGradient(
                                        colors = listOf(InverseSurface.copy(alpha = 0.8f), Color.Transparent)
                                    )
                                )
                        )
                        Column(
                            modifier = Modifier
                                .fillMaxHeight()
                                .padding(16.dp)
                                .fillMaxWidth(0.6f),
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                stringResource(R.string.home_banner_title),
                                style = MaterialTheme.typography.titleLarge,
                                color = SurfaceContainerLowest,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(Modifier.height(4.dp))
                            Text(
                                stringResource(R.string.home_banner_subtitle),
                                fontSize = 14.sp,
                                color = SurfaceContainerLow
                            )
                            Spacer(Modifier.height(8.dp))
                            Text(
                                stringResource(R.string.explore),
                                modifier = Modifier
                                    .background(SecondaryContainer, RoundedCornerShape(24.dp))
                                    .padding(horizontal = 16.dp, vertical = 8.dp)
                                    .clickable { selectedCategoryId = "textiles" },
                                color = Color(0xFF2A1800),
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            }

            item(span = { GridItemSpan(maxLineSpan) }) {
                // Kolam Divider
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    HorizontalDivider(color = OutlineVariant, thickness = 2.dp)
                    Icon(
                        Icons.Filled.ScatterPlot,
                        contentDescription = null,
                        tint = OutlineVariant,
                        modifier = Modifier.background(Background).padding(horizontal = 8.dp)
                    )
                }
            }

            item(span = { GridItemSpan(maxLineSpan) }) {
                // Products Section Header
                Row(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        if (selectedCategoryId == "all") stringResource(R.string.home_trending) else "${categories.find { it.id == selectedCategoryId }?.name} Crafts",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = OnBackground
                    )
                    if (filteredProducts.size > 4) {
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.clickable { /* Could navigate to a full list screen */ }) {
                            Text(stringResource(R.string.view_all), fontSize = 12.sp, color = Primary, fontWeight = FontWeight.SemiBold)
                            Icon(Icons.Filled.ChevronRight, contentDescription = null, tint = Primary, modifier = Modifier.size(16.dp))
                        }
                    }
                }
            }

            if (filteredProducts.isEmpty()) {
                item(span = { GridItemSpan(maxLineSpan) }) {
                    Box(modifier = Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
                        Text(stringResource(R.string.home_no_crafts_found), color = OnSurfaceVariant)
                    }
                }
            } else {
                items(filteredProducts.size) { index ->
                    ProductCardMatched(filteredProducts[index], cartViewModel, wishlistViewModel, navController)
                }
            }
        }

        // Floating Action Button
        FloatingActionButton(
            onClick = { navController.navigate("santhe_ai_assistant") },
            containerColor = Primary,
            contentColor = OnPrimary,
            shape = RoundedCornerShape(24.dp),
            elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 4.dp),
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 24.dp, end = 16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Icon(Icons.Filled.SmartToy, contentDescription = stringResource(R.string.home_ai_fab))
                Spacer(Modifier.width(8.dp))
                Text(stringResource(R.string.home_ai_fab), fontWeight = FontWeight.SemiBold, fontSize = 12.sp)
            }
        }
    }
}

@Composable
fun ProductCardMatched(product: com.example.hallisanthe.data.Product, cartViewModel: CartViewModel, wishlistViewModel: WishlistViewModel, navController: NavController) {
    val isInWishlist = wishlistViewModel.isInWishlist(product.id)
    
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(4.dp, RoundedCornerShape(16.dp), spotColor = Primary)
            .background(SurfaceContainerLowest, RoundedCornerShape(16.dp))
            .clickable { navController.navigate("product/${product.id}") }
    ) {
        Box(modifier = Modifier.fillMaxWidth().aspectRatio(1f)) {
            AsyncImage(
                model = product.imageUrl,
                contentDescription = product.name,
                modifier = Modifier.fillMaxSize().clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)),
                contentScale = ContentScale.Crop
            )
            Box(
                modifier = Modifier
                    .padding(8.dp)
                    .align(Alignment.TopEnd)
                    .background(SurfaceContainerLowest.copy(alpha = 0.8f), CircleShape)
                    .padding(6.dp)
                    .clickable { wishlistViewModel.toggleWishlist(product) }
            ) {
                Icon(
                    if (isInWishlist) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                    contentDescription = stringResource(R.string.wishlist),
                    tint = if (isInWishlist) Error else OnSurfaceVariant,
                    modifier = Modifier.size(20.dp)
                )
            }
            if (product.isFeatured) {
                Row(
                    modifier = Modifier
                        .padding(8.dp)
                        .align(Alignment.BottomStart)
                        .background(TertiaryContainer, RoundedCornerShape(12.dp))
                        .padding(horizontal = 8.dp, vertical = 2.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Filled.Verified, contentDescription = stringResource(R.string.verified), tint = OnTertiaryContainer, modifier = Modifier.size(12.dp))
                    Spacer(Modifier.width(4.dp))
                    Text(stringResource(R.string.verified), color = OnTertiaryContainer, fontSize = 10.sp, fontWeight = FontWeight.SemiBold)
                }
            }
        }
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                product.name,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
                color = OnBackground,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                AsyncImage(
                    model = "https://lh3.googleusercontent.com/aida-public/AB6AXuCdlFQUqK7bmC9tclF7wLHYu4YD7yUIY_S5eiFEeooGPZn9S7f7AGCsJi2FDDqMrat4TxyDr6g8gLA20vXGFoGQXtcdnSEf4Qp8Z42ldX517pwCJUSs74EIA67xV5TczoTjR53nkCd1Cgf-SC82OSApW_RQDdw0OE-3geB4eMgzoIe8wAJwgGzRxLj0LEaBKS21FlcmpILTeEDDnioxSAjtGoQbI4h3gP86xagKSRE6PdUI1vCxGHOkMWN_OBXG-o7iJ89r2uWHlGE",
                    contentDescription = null,
                    modifier = Modifier.size(20.dp).clip(CircleShape).border(1.dp, Primary, CircleShape),
                    contentScale = ContentScale.Crop
                )
                Spacer(Modifier.width(4.dp))
                Text("${product.artisan} • ${product.location.take(8)}", fontSize = 11.sp, color = OnSurfaceVariant, maxLines = 1, overflow = TextOverflow.Ellipsis)
            }
            Spacer(Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("₹ ${product.price}", color = Primary, fontWeight = FontWeight.SemiBold, fontSize = 12.sp)
                Box(
                    modifier = Modifier
                        .shadow(4.dp, RoundedCornerShape(8.dp), spotColor = Primary)
                        .background(SecondaryContainer, RoundedCornerShape(8.dp))
                        .clickable { cartViewModel.addItem(product) }
                        .padding(6.dp)
                ) {
                    Icon(Icons.Filled.AddShoppingCart, contentDescription = stringResource(R.string.add), tint = Color(0xFF2A1800), modifier = Modifier.size(18.dp))
                }
            }
        }
    }
}
