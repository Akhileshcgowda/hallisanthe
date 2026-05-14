package com.example.hallisanthe.ui.screens

import com.example.hallisanthe.R
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.LocalMall
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.filled.LocalMall
import androidx.navigation.NavController
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import coil.compose.AsyncImage
import com.example.hallisanthe.ui.components.ProductImage
import com.example.hallisanthe.data.local.entity.OrderEntity
import com.example.hallisanthe.data.local.entity.OrderItemSnapshot
import com.example.hallisanthe.data.viewmodel.OrderViewModel
import com.example.hallisanthe.ui.theme.*
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyOrdersScreen(navController: NavController, orderViewModel: OrderViewModel) {
    val orders by orderViewModel.orders.collectAsState(initial = emptyList())

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
    ) {
        if (orders.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(Icons.Filled.LocalMall, contentDescription = null, modifier = Modifier.size(64.dp), tint = OnSurfaceVariant.copy(alpha = 0.3f))
                Spacer(modifier = Modifier.height(16.dp))
                Text(stringResource(R.string.no_orders_yet), style = MaterialTheme.typography.titleMedium, color = OnSurfaceVariant)
                Text(stringResource(R.string.orders_appear_here), style = MaterialTheme.typography.bodyMedium, color = OnSurfaceVariant.copy(alpha = 0.7f))
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(orders) { order ->
                    OrderItemCard(order = order)
                }
            }
        }
    }
}

@Composable
fun OrderItemCard(order: OrderEntity) {
    val dateFormatter = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault())
    val dateStr = dateFormatter.format(Date(order.timestamp))
    val gson = com.google.gson.Gson()
    val items: List<OrderItemSnapshot> = try {
        val type = object : com.google.gson.reflect.TypeToken<List<OrderItemSnapshot>>() {}.type
        gson.fromJson(order.itemsJson, type) ?: emptyList()
    } catch (_: Exception) { emptyList() }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(text = "#${order.id.takeLast(8).uppercase()}", fontWeight = FontWeight.Bold, color = Primary)
                    Text(text = dateStr, fontSize = 12.sp, color = OnSurfaceVariant)
                }
                Surface(
                    color = if (order.status == "Delivered") Color(0xFFE8F5E9) else Color(0xFFFFF3E0),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(
                        text = order.status,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                        color = if (order.status == "Delivered") Color(0xFF2E7D32) else Color(0xFFE65100),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Spacer(Modifier.height(16.dp))
            HorizontalDivider(color = Color.LightGray.copy(alpha = 0.5f))
            Spacer(Modifier.height(16.dp))

            items.forEach { item ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    ProductImage(
                        imageUrl = item.imageUrl,
                        contentDescription = null,
                        modifier = Modifier.size(60.dp),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(Modifier.width(16.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = item.name, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                        Text(text = stringResource(R.string.qty, item.quantity), fontSize = 12.sp, color = OnSurfaceVariant)
                    }
                    Text(text = "₹ ${item.price * item.quantity}", fontWeight = FontWeight.Bold)
                }
                Spacer(Modifier.height(8.dp))
            }

            Spacer(Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = stringResource(R.string.total_amount), fontWeight = FontWeight.SemiBold)
                Text(text = "₹ ${order.total}", fontWeight = FontWeight.ExtraBold, fontSize = 18.sp, color = OnBackground)
            }
        }
    }
}
