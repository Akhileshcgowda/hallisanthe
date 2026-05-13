package com.example.hallisanthe.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import android.widget.Toast
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import com.example.hallisanthe.R
import com.example.hallisanthe.data.repository.ProductRepository
import com.example.hallisanthe.data.viewmodel.AuthViewModel
import com.example.hallisanthe.ui.theme.*
import coil.compose.AsyncImage

data class ListingItem(
    val id: String,
    val title: String,
    val description: String,
    val price: String,
    val imageRes: Int,
    val status: String,
    val statusColor: Color,
    val statusIcon: androidx.compose.ui.graphics.vector.ImageVector
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyListingsScreen(navController: NavController, productRepository: ProductRepository, authViewModel: AuthViewModel) {
    val context = LocalContext.current
    val currentUser by authViewModel.currentUser.collectAsState()
    val artisanId = currentUser?.uid ?: ""
    val products by productRepository.getProductsByArtisan(artisanId).collectAsState(initial = emptyList())

    Scaffold(
        containerColor = Background,
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("santhe_ai_assistant") },
                containerColor = PrimaryContainer,
                contentColor = OnPrimaryContainer,
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
            
            // Kolam Divider
            Box(modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp), contentAlignment = Alignment.Center) {
                Text("◆ ◇ ◆ ◇ ◆", color = Outline, letterSpacing = 8.sp, fontSize = 14.sp)
            }

            if (artisanId.isBlank()) {
                Box(modifier = Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
                    Text(stringResource(R.string.login_to_view_listings), color = OnSurfaceVariant, fontWeight = FontWeight.SemiBold)
                }
            } else if (products.isEmpty()) {
                Box(modifier = Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(stringResource(R.string.no_listings_yet), color = OnSurfaceVariant, fontWeight = FontWeight.SemiBold)
                        Spacer(Modifier.height(8.dp))
                        Text(stringResource(R.string.products_will_appear), color = OnSurfaceVariant, fontSize = 12.sp)
                    }
                }
            } else {
                // Grid of Listings
                LazyVerticalGrid(
                    columns = GridCells.Adaptive(minSize = 300.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(bottom = 80.dp)
                ) {
                    items(products, key = { it.id }) { product ->
                        ListingCard(
                            ListingItem(
                                id = product.id,
                                title = product.name,
                                description = product.description,
                                price = "₹${product.price}",
                                imageRes = R.drawable.my_listings,
                                status = stringResource(R.string.in_stock),
                                statusColor = Tertiary,
                                statusIcon = Icons.Filled.CheckCircle
                            )
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ListingCard(listing: ListingItem) {
    val context = LocalContext.current
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = SurfaceContainerLowest),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            // Image
            Box(
                modifier = Modifier
                    .weight(0.4f)
                    .aspectRatio(1f)
                    .background(SurfaceContainer)
            ) {
                Image(
                    painter = painterResource(id = listing.imageRes),
                    contentDescription = listing.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }
            
            // Details
            Column(
                modifier = Modifier
                    .weight(0.6f)
                    .padding(16.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(bottom = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {
                        Text(
                            text = listing.title,
                            style = MaterialTheme.typography.titleMedium,
                            color = OnSurface,
                            fontWeight = FontWeight.Bold,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.weight(1f)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Row(
                            modifier = Modifier
                                .background(if (listing.status == "In Stock") Tertiary else SurfaceVariant, CircleShape)
                                .then(if (listing.status == "Low Stock") Modifier.border(1.dp, OutlineVariant, CircleShape) else Modifier)
                                .padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                listing.statusIcon, 
                                contentDescription = null, 
                                modifier = Modifier.size(14.dp), 
                                tint = if (listing.status == "In Stock") OnTertiary else OnSurfaceVariant
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                listing.status, 
                                fontSize = 10.sp, 
                                fontWeight = FontWeight.SemiBold,
                                color = if (listing.status == "In Stock") OnTertiary else OnSurfaceVariant
                            )
                        }
                    }
                    Text(
                        text = listing.description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = OnSurfaceVariant,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Text(
                        text = listing.price,
                        style = MaterialTheme.typography.titleLarge,
                        color = Primary,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Actions
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = { Toast.makeText( context, context.getString(R.string.edit_coming_soon), Toast.LENGTH_SHORT).show() },
                        modifier = Modifier.weight(1f).height(36.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = SecondaryContainer, contentColor = OnSecondaryContainer),
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Icon(Icons.Filled.Edit, contentDescription = "Edit", modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(stringResource(R.string.edit), fontSize = 12.sp)
                    }
                    Button(
                        onClick = { Toast.makeText(context, context.getString(R.string.delete_coming_soon), Toast.LENGTH_SHORT).show() },
                        modifier = Modifier.weight(1f).height(36.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent, contentColor = Color(0xFFBA1A1A)),
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Icon(Icons.Filled.Delete, contentDescription = "Delete", modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(stringResource(R.string.delete), fontSize = 12.sp)
                    }
                }
            }
        }
    }
}
