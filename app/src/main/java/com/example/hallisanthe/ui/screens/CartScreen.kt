package com.example.hallisanthe.ui.screens

import com.example.hallisanthe.R
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.hallisanthe.data.CartItem
import com.example.hallisanthe.data.CartViewModel
import com.example.hallisanthe.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(navController: NavController, cartViewModel: CartViewModel) {
    val items = cartViewModel.cartItems
    val total = cartViewModel.total

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            if (items.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(stringResource(R.string.cart_empty), style = MaterialTheme.typography.titleLarge, color = OnSurfaceVariant)
                        Spacer(Modifier.height(16.dp))
                        Button(onClick = { navController.navigate("home") }, colors = ButtonDefaults.buttonColors(containerColor = Primary)) {
                            Text(stringResource(R.string.start_shopping))
                        }
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(items) { item ->
                        CartItemRow(
                            item = item,
                            onIncrease = { cartViewModel.addItem(item.product) },
                            onDecrease = { cartViewModel.decreaseQty(item.product.id) }
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                HorizontalDivider(color = OutlineVariant)
                Spacer(modifier = Modifier.height(16.dp))
                
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    val subtotal = cartViewModel.subtotal
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(stringResource(R.string.subtotal), color = OnSurfaceVariant)
                        Text("₹$subtotal")
                    }
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(stringResource(R.string.shipping), color = OnSurfaceVariant)
                        Text(stringResource(R.string.free), color = Color(0xFF2E7D32), fontWeight = FontWeight.Bold)
                    }
                    Spacer(Modifier.height(8.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(stringResource(R.string.total), style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                        Text("₹$total", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, color = Primary)
                    }
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                
                Button(
                    onClick = { navController.navigate("checkout") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Primary)
                ) {
                    Text(stringResource(R.string.proceed_to_checkout), fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun CartItemRow(item: CartItem, onIncrease: () -> Unit, onDecrease: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            AsyncImage(
                model = item.product.imageUrl,
                contentDescription = item.product.name,
                modifier = Modifier
                    .size(90.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(item.product.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text(item.product.artisan, style = MaterialTheme.typography.bodySmall, color = OnSurfaceVariant)
                Spacer(Modifier.height(8.dp))
                Text("₹${item.product.price * item.quantity}", style = MaterialTheme.typography.titleMedium, color = Primary, fontWeight = FontWeight.ExtraBold)
            }
            
            Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(4.dp)) {
                IconButton(onClick = onIncrease, modifier = Modifier.size(32.dp).background(Primary.copy(alpha = 0.1f), CircleShape)) {
                    Icon(Icons.Default.Add, contentDescription = stringResource(R.string.add), tint = Primary, modifier = Modifier.size(18.dp))
                }
                Text(text = "${item.quantity}", fontWeight = FontWeight.Bold)
                IconButton(onClick = onDecrease, modifier = Modifier.size(32.dp).background(OutlineVariant.copy(alpha = 0.1f), CircleShape)) {
                    Icon(if (item.quantity > 1) Icons.Default.Remove else Icons.Default.Delete, contentDescription = stringResource(R.string.remove), tint = if (item.quantity > 1) Color.DarkGray else Error, modifier = Modifier.size(18.dp))
                }
            }
        }
    }
}
