package com.example.hallisanthe.ui.screens

import com.example.hallisanthe.R
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.hallisanthe.data.CartViewModel
import com.example.hallisanthe.data.WishlistViewModel
import com.example.hallisanthe.data.viewmodel.ProductCatalogViewModel
import com.example.hallisanthe.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(navController: NavController, productId: String, cartViewModel: CartViewModel, wishlistViewModel: WishlistViewModel, catalogViewModel: ProductCatalogViewModel) {
    val allProducts by catalogViewModel.allProducts.collectAsState(initial = emptyList())
    val product = allProducts.find { it.id == productId }
    val isInWishlist = product?.let { wishlistViewModel.isInWishlist(it.id) } ?: false

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.product_details), style = MaterialTheme.typography.titleLarge) },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(R.string.back), tint = OnSurface)
                    }
                },
                actions = {
                    IconButton(onClick = { product?.let { wishlistViewModel.toggleWishlist(it) } }) {
                        Icon(
                            if (isInWishlist) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                            contentDescription = stringResource(R.string.wishlist),
                            tint = if (isInWishlist) Error else OnSurface
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = BrandCream)
            )
        },
        containerColor = Background
    ) { paddingValues ->
        if (product == null) {
            Box(modifier = Modifier.fillMaxSize().padding(paddingValues), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        stringResource(R.string.product_not_found),
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = Primary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        stringResource(R.string.try_different_keyword),
                        style = MaterialTheme.typography.bodyMedium,
                        color = OnSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    Button(
                        onClick = { navController.navigateUp() },
                        colors = ButtonDefaults.buttonColors(containerColor = Primary)
                    ) {
                        Text(stringResource(R.string.back))
                    }
                }
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState())
            ) {
                // Product Image
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(350.dp)
                        .background(SurfaceVariant)
                ) {
                    AsyncImage(
                        model = product.imageUrl,
                        contentDescription = product.name,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }

                Column(modifier = Modifier.padding(24.dp)) {
                    Text(text = product.name, style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.Bold, color = OnBackground)
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                        Text(text = stringResource(R.string.by_artisan, product.artisan), style = MaterialTheme.typography.titleMedium, color = Primary, fontWeight = FontWeight.SemiBold)
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Filled.Star, contentDescription = "Rating", tint = Secondary, modifier = Modifier.size(20.dp))
                            Text(text = stringResource(R.string.rating_reviews), style = MaterialTheme.typography.bodyMedium, color = OnSurfaceVariant)
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))
                    Text(text = "₹${product.price}", style = MaterialTheme.typography.displaySmall, fontWeight = FontWeight.ExtraBold, color = Primary)

                    Spacer(modifier = Modifier.height(32.dp))
                    Text(text = stringResource(R.string.heritage_craft), style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = if (product.description.isNotEmpty()) product.description else stringResource(R.string.product_desc_fallback, product.location),
                        style = MaterialTheme.typography.bodyLarge,
                        color = OnSurfaceVariant,
                        lineHeight = 24.sp
                    )

                    if (product.heritage.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = product.heritage,
                            style = MaterialTheme.typography.bodyMedium,
                            color = OnSurfaceVariant.copy(alpha = 0.8f),
                            lineHeight = 22.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(40.dp))

                    val addedMsg = stringResource(R.string.added_to_wishlist)
                    val removedMsg = stringResource(R.string.removed_from_wishlist)
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        OutlinedButton(
                            onClick = {
                                wishlistViewModel.toggleWishlist(product)
                                val message = if (wishlistViewModel.isInWishlist(product.id)) addedMsg else removedMsg
                                android.widget.Toast.makeText(navController.context, message, android.widget.Toast.LENGTH_SHORT).show()
                            },
                            modifier = Modifier.weight(1f).height(56.dp),
                            shape = RoundedCornerShape(16.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, if (isInWishlist) Error else Primary)
                        ) {
                            Text(if (isInWishlist) stringResource(R.string.saved) else stringResource(R.string.save_for_later), color = if (isInWishlist) Error else Primary, fontWeight = FontWeight.Bold)
                        }

                        Button(
                            onClick = {
                                cartViewModel.addItem(product)
                                navController.navigate("cart")
                            },
                            modifier = Modifier.weight(1f).height(56.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Primary)
                        ) {
                            Text(stringResource(R.string.buy_now), fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                    Spacer(Modifier.height(24.dp))
                }
            }
        }
    }
}
