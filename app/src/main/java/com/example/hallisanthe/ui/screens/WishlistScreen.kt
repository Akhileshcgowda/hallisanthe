package com.example.hallisanthe.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.hallisanthe.R
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.hallisanthe.data.CartViewModel
import com.example.hallisanthe.data.WishlistViewModel
import com.example.hallisanthe.ui.theme.Background
import com.example.hallisanthe.ui.theme.BrandCream

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WishlistScreen(navController: NavController, cartViewModel: CartViewModel, wishlistViewModel: WishlistViewModel) {
    val items = wishlistViewModel.wishlistItems

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
    ) {
        if (items.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(stringResource(R.string.wishlist_empty), style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Spacer(Modifier.height(16.dp))
                    Button(onClick = { navController.navigate("home") }) {
                        Text(stringResource(R.string.discover_crafts))
                    }
                }
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(items) { product ->
                    ProductCardMatched(
                        product = product,
                        cartViewModel = cartViewModel,
                        wishlistViewModel = wishlistViewModel,
                        navController = navController
                    )
                }
            }
        }
    }
}
