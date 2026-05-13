package com.example.hallisanthe.ui.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.hallisanthe.data.CartViewModel
import com.example.hallisanthe.data.local.entity.OrderItemSnapshot
import com.example.hallisanthe.data.viewmodel.OrderViewModel
import androidx.compose.ui.res.stringResource
import com.example.hallisanthe.R
import com.example.hallisanthe.ui.theme.*

data class ShippingDetails(val name: String = "", val address: String = "", val city: String = "")

@Composable
fun CheckoutScreen(navController: NavController, cartViewModel: CartViewModel, orderViewModel: OrderViewModel) {
    var step by remember { mutableStateOf(1) } // 1 = Shipping, 2 = Payment, 3 = Review, 4 = Success
    var shipping by remember { mutableStateOf(ShippingDetails()) }
    var paymentMethod by remember { mutableStateOf("UPI / Digital Wallet") }
    val animatedProgress by animateFloatAsState(
        targetValue = step / 3f,
        animationSpec = tween(durationMillis = 500),
        label = "CheckoutProgress"
    )
    val isPlacingOrder by orderViewModel.isPlacingOrder.collectAsState()
    val placeOrderResult by orderViewModel.placeOrderResult.collectAsState()

    LaunchedEffect(placeOrderResult) {
        placeOrderResult?.let { result ->
            if (result.isSuccess) {
                cartViewModel.clearCart()
                orderViewModel.clearPlaceOrderResult()
                step = 4
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .padding(16.dp)
    ) {
        if (step < 4) {
            LinearProgressIndicator(
                progress = { animatedProgress },
                modifier = Modifier.fillMaxWidth().height(8.dp),
                color = Primary,
                trackColor = SurfaceVariant
            )
            Spacer(modifier = Modifier.height(24.dp))
        }

        when (step) {
            1 -> ShippingStep(
                shipping = shipping,
                onShippingChange = { shipping = it },
                onNext = { step = 2 }
            )
            2 -> PaymentStep(
                selectedMethod = paymentMethod,
                onMethodChange = { paymentMethod = it },
                onNext = { step = 3 }
            )
            3 -> ReviewStep(
                cartViewModel = cartViewModel,
                shipping = shipping,
                paymentMethod = paymentMethod,
                orderViewModel = orderViewModel,
                isPlacingOrder = isPlacingOrder,
                placeOrderResult = placeOrderResult
            )
            4 -> SuccessStep(onHome = {
                navController.navigate("home") {
                    popUpTo("home") { inclusive = true }
                }
            })
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShippingStep(shipping: ShippingDetails, onShippingChange: (ShippingDetails) -> Unit, onNext: () -> Unit) {
    Column {
        Text(stringResource(R.string.shipping_info), style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = shipping.name,
            onValueChange = { onShippingChange(shipping.copy(name = it)) },
            label = { Text(stringResource(R.string.full_name_label)) },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = shipping.address,
            onValueChange = { onShippingChange(shipping.copy(address = it)) },
            label = { Text(stringResource(R.string.address_line_1)) },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = shipping.city,
            onValueChange = { onShippingChange(shipping.copy(city = it)) },
            label = { Text(stringResource(R.string.city_town)) },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.weight(1f))
        Button(
            onClick = onNext,
            modifier = Modifier.fillMaxWidth().height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Primary),
            enabled = shipping.name.isNotBlank() && shipping.address.isNotBlank() && shipping.city.isNotBlank()
        ) {
            Text(stringResource(R.string.continue_to_payment), fontSize = 18.sp)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentStep(selectedMethod: String, onMethodChange: (String) -> Unit, onNext: () -> Unit) {
    val upiOption = stringResource(R.string.upi_wallet)
    val cardOption = stringResource(R.string.credit_debit_card)
    val codOption = stringResource(R.string.cash_on_delivery)
    val options = listOf(upiOption, cardOption, codOption)

    var selectedUpiApp by remember { mutableStateOf<String?>(null) }
    var cardNumber by remember { mutableStateOf("") }
    var cardExpiry by remember { mutableStateOf("") }
    var cardCvv by remember { mutableStateOf("") }
    var cardHolder by remember { mutableStateOf("") }

    val upiApps = listOf(
        stringResource(R.string.google_pay),
        stringResource(R.string.phonepe),
        stringResource(R.string.paytm),
        stringResource(R.string.amazon_pay),
        stringResource(R.string.bhim_upi)
    )

    Column {
        Text(stringResource(R.string.payment_method), style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))

        options.forEach { option ->
            val isSelected = option == selectedMethod
            Card(
                modifier = Modifier.fillMaxWidth().clickable {
                    onMethodChange(option)
                    selectedUpiApp = null
                },
                colors = CardDefaults.cardColors(
                    containerColor = if (isSelected) SurfaceVariant else MaterialTheme.colorScheme.surface
                )
            ) {
                Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = isSelected,
                        onClick = {
                            onMethodChange(option)
                            selectedUpiApp = null
                        },
                        colors = RadioButtonDefaults.colors(selectedColor = Primary)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(option, style = MaterialTheme.typography.bodyLarge)
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
        }

        // Conditional payment details
        when (selectedMethod) {
            upiOption -> {
                Spacer(modifier = Modifier.height(8.dp))
                Text(stringResource(R.string.select_upi_app), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.horizontalScroll(rememberScrollState())
                ) {
                    upiApps.forEach { app ->
                        val isAppSelected = selectedUpiApp == app
                        FilterChip(
                            selected = isAppSelected,
                            onClick = { selectedUpiApp = if (isAppSelected) null else app },
                            label = { Text(app, fontSize = 12.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = Primary,
                                selectedLabelColor = OnPrimary
                            )
                        )
                    }
                }
            }
            cardOption -> {
                Spacer(modifier = Modifier.height(8.dp))
                Text(stringResource(R.string.card_number), style = MaterialTheme.typography.labelLarge)
                OutlinedTextField(
                    value = cardNumber,
                    onValueChange = { if (it.length <= 16) cardNumber = it.filter { c -> c.isDigit() } },
                    placeholder = { Text("XXXX XXXX XXXX XXXX") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedTextField(
                        value = cardExpiry,
                        onValueChange = { if (it.length <= 5) cardExpiry = it },
                        label = { Text(stringResource(R.string.card_expiry)) },
                        placeholder = { Text("MM/YY") },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp)
                    )
                    OutlinedTextField(
                        value = cardCvv,
                        onValueChange = { if (it.length <= 3) cardCvv = it.filter { c -> c.isDigit() } },
                        label = { Text(stringResource(R.string.card_cvv)) },
                        placeholder = { Text("123") },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp)
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = cardHolder,
                    onValueChange = { cardHolder = it },
                    label = { Text(stringResource(R.string.card_holder_name)) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )
            }
            codOption -> {
                Spacer(modifier = Modifier.height(8.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = SurfaceContainerLow),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        stringResource(R.string.pay_on_delivery_msg),
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.bodyMedium,
                        color = OnSurfaceVariant
                    )
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))
        Button(onClick = onNext, modifier = Modifier.fillMaxWidth().height(56.dp), colors = ButtonDefaults.buttonColors(containerColor = Primary)) {
            Text(stringResource(R.string.review_order), fontSize = 18.sp)
        }
    }
}

@Composable
fun ReviewStep(
    cartViewModel: CartViewModel,
    shipping: ShippingDetails,
    paymentMethod: String,
    orderViewModel: OrderViewModel,
    isPlacingOrder: Boolean,
    placeOrderResult: Result<String>?
) {
    Column {
        Text(stringResource(R.string.review_your_order), style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))

        Text(stringResource(R.string.items_count, cartViewModel.itemCount), style = MaterialTheme.typography.bodyLarge)
        Text("${stringResource(R.string.total_amount)}: ₹${cartViewModel.total}", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = Primary)
        Text(stringResource(R.string.payment_label, paymentMethod), style = MaterialTheme.typography.bodyMedium, color = OnSurfaceVariant)

        Spacer(modifier = Modifier.height(24.dp))
        Text(stringResource(R.string.shipping_to), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        Text(
            if (shipping.name.isNotBlank()) "${shipping.name}\n${shipping.address}\n${shipping.city}"
            else stringResource(R.string.not_provided),
            style = MaterialTheme.typography.bodyMedium
        )

        if (placeOrderResult != null && placeOrderResult.isFailure) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                placeOrderResult.exceptionOrNull()?.message ?: stringResource(R.string.failed_place_order),
                color = androidx.compose.ui.graphics.Color.Red,
                fontSize = 14.sp
            )
        }

        Spacer(modifier = Modifier.weight(1f))
        Button(
            onClick = {
                val items = cartViewModel.cartItems.map { cartItem ->
                    OrderItemSnapshot(
                        productId = cartItem.product.id,
                        name = cartItem.product.name,
                        price = cartItem.product.price,
                        imageUrl = cartItem.product.imageUrl,
                        quantity = cartItem.quantity
                    )
                }
                orderViewModel.placeOrder(
                    items = items,
                    total = cartViewModel.total,
                    paymentMethod = paymentMethod,
                    shippingName = shipping.name,
                    shippingAddress = shipping.address,
                    shippingCity = shipping.city
                )
            },
            modifier = Modifier.fillMaxWidth().height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Primary),
            enabled = !isPlacingOrder && cartViewModel.cartItems.isNotEmpty()
        ) {
            if (isPlacingOrder) {
                CircularProgressIndicator(modifier = Modifier.size(24.dp), color = androidx.compose.ui.graphics.Color.White, strokeWidth = 2.dp)
            } else {
                Text(stringResource(R.string.place_order), fontSize = 18.sp)
            }
        }
    }
}

@Composable
fun SuccessStep(onHome: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(Icons.Filled.CheckCircle, contentDescription = stringResource(R.string.order_placed_success), tint = Secondary, modifier = Modifier.size(100.dp))
        Spacer(modifier = Modifier.height(24.dp))
        Text(stringResource(R.string.order_placed_success), style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, color = Primary, textAlign = TextAlign.Center)
        Spacer(modifier = Modifier.height(16.dp))
        Text(stringResource(R.string.thank_you_artisans), style = MaterialTheme.typography.bodyLarge, textAlign = TextAlign.Center)
        
        Spacer(modifier = Modifier.height(48.dp))
        Button(onClick = onHome, modifier = Modifier.fillMaxWidth().height(56.dp), colors = ButtonDefaults.buttonColors(containerColor = Primary)) {
            Text(stringResource(R.string.back_to_home), fontSize = 18.sp)
        }
    }
}
