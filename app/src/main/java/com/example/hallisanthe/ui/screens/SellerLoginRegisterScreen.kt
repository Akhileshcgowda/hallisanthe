package com.example.hallisanthe.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.compose.ui.res.stringResource
import com.example.hallisanthe.R
import com.example.hallisanthe.data.viewmodel.AuthViewModel
import com.example.hallisanthe.ui.theme.*

@Composable
fun SellerLoginRegisterScreen(navController: NavController, isSeller: MutableState<Boolean>? = null, authViewModel: AuthViewModel) {
    var fullName by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var shopName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLogin by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var passwordVisible by remember { mutableStateOf(false) }
    var showForgotDialog by remember { mutableStateOf(false) }
    var forgotEmail by remember { mutableStateOf("") }
    var forgotLoading by remember { mutableStateOf(false) }
    var forgotMessage by remember { mutableStateOf<String?>(null) }
    val context = androidx.compose.ui.platform.LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .verticalScroll(rememberScrollState())
            .imePadding(),
        contentAlignment = Alignment.TopCenter
    ) {
        // Decorative Orbs
        Box(
            modifier = Modifier
                .align(Alignment.TopStart)
                .offset(x = (-100).dp, y = (-100).dp)
                .size(300.dp)
                .background(PrimaryFixedDim.copy(alpha = 0.3f), CircleShape)
        )
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .offset(x = 100.dp, y = 100.dp)
                .size(400.dp)
                .background(SecondaryFixedDim.copy(alpha = 0.3f), CircleShape)
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceContainerLowest),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                // Header
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    // Logo Placeholder
                    Box(modifier = Modifier.size(56.dp).background(SurfaceContainerLow, CircleShape), contentAlignment = Alignment.Center) {
                        Icon(Icons.Filled.Storefront, contentDescription = null, tint = Primary, modifier = Modifier.size(32.dp))
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(stringResource(R.string.join_santhe), style = MaterialTheme.typography.headlineMedium, color = OnSurface, fontWeight = FontWeight.Bold)
                }

                // Tabs
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(SurfaceVariant)
                        .padding(4.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(6.dp))
                            .background(if (!isLogin) Color.Transparent else SurfaceContainerLowest)
                            .clickable { isLogin = true }
                            .padding(vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(stringResource(R.string.login_tab), style = MaterialTheme.typography.labelLarge, color = if (!isLogin) OnSurfaceVariant else Primary)
                    }
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(6.dp))
                            .background(if (isLogin) Color.Transparent else SurfaceContainerLowest)
                            .clickable { isLogin = false }
                            .padding(vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(stringResource(R.string.register_tab), style = MaterialTheme.typography.labelLarge, color = if (isLogin) OnSurfaceVariant else Primary)
                    }
                }

                // Role Selection
                Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(stringResource(R.string.choose_role), style = MaterialTheme.typography.titleMedium, color = OnSurface)
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        RoleCard(
                            modifier = Modifier.weight(1f),
                            title = stringResource(R.string.i_am_buyer),
                            icon = Icons.Filled.ShoppingBag,
                            isActive = false,
                            onClick = { }
                        )
                        RoleCard(
                            modifier = Modifier.weight(1f),
                            title = stringResource(R.string.i_am_seller),
                            icon = Icons.Filled.Storefront,
                            isActive = true,
                            onClick = { }
                        )
                    }
                }

                // Error
                if (errorMessage != null) {
                    Text(
                        errorMessage ?: "",
                        color = androidx.compose.ui.graphics.Color.Red,
                        fontSize = 12.sp,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                // Form
                Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    if (!isLogin) {
                        OutlinedTextField(
                            value = fullName,
                            onValueChange = { fullName = it },
                            label = { Text(stringResource(R.string.full_name)) },
                            placeholder = { Text(stringResource(R.string.full_name_hint)) },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        )
                    }

                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text(stringResource(R.string.email)) },
                        placeholder = { Text(stringResource(R.string.email_hint)) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text(stringResource(R.string.password)) },
                        placeholder = { Text(stringResource(R.string.min_6_chars)) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        trailingIcon = {
                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(
                                    imageVector = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                                    contentDescription = if (passwordVisible) stringResource(R.string.hide_password) else stringResource(R.string.show_password),
                                    tint = OnSurfaceVariant
                                )
                            }
                        },
                        visualTransformation = if (passwordVisible) androidx.compose.ui.text.input.VisualTransformation.None else androidx.compose.ui.text.input.PasswordVisualTransformation()
                    )

                    if (isLogin) {
                        Text(
                            stringResource(R.string.forgot_password),
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 12.sp,
                            color = Primary,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { showForgotDialog = true; forgotEmail = email; forgotMessage = null },
                            textAlign = TextAlign.End
                        )
                    }

                    if (!isLogin) {
                        Column {
                            Text(stringResource(R.string.mobile_number), style = MaterialTheme.typography.labelLarge, color = OnSurfaceVariant, modifier = Modifier.padding(bottom = 4.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Surface(
                                    modifier = Modifier.height(56.dp).clip(RoundedCornerShape(topStart = 12.dp, bottomStart = 12.dp)).border(1.dp, Outline, RoundedCornerShape(topStart = 12.dp, bottomStart = 12.dp)),
                                    color = SurfaceVariant
                                ) {
                                    Box(modifier = Modifier.padding(horizontal = 16.dp), contentAlignment = Alignment.Center) {
                                        Text(stringResource(R.string.india_code), style = MaterialTheme.typography.bodyLarge, color = OnSurfaceVariant)
                                    }
                                }
                                OutlinedTextField(
                                    value = phoneNumber,
                                    onValueChange = { phoneNumber = it },
                                    placeholder = { Text(stringResource(R.string.mobile_hint)) },
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(topEnd = 12.dp, bottomEnd = 12.dp),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = Primary,
                                        unfocusedBorderColor = Outline
                                    )
                                )
                            }
                        }

                        OutlinedTextField(
                            value = shopName,
                            onValueChange = { shopName = it },
                            label = { Text(stringResource(R.string.shop_name_artisan)) },
                            placeholder = { Text(stringResource(R.string.shop_name_hint)) },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Button(
                        onClick = {
                            if (email.isBlank() || password.isBlank()) {
                                errorMessage = context.getString(R.string.error_email_password_required)
                                return@Button
                            }
                            if (password.length < 6) {
                                errorMessage = context.getString(R.string.error_password_short)
                                return@Button
                            }
                            if (!isLogin && fullName.isBlank()) {
                                errorMessage = context.getString(R.string.error_full_name_required)
                                return@Button
                            }
                            errorMessage = null
                            isLoading = true
                            if (isLogin) {
                                authViewModel.signInWithEmailPassword(email, password) { success, error ->
                                    isLoading = false
                                    if (success) {
                                        isSeller?.value = true
                                        android.widget.Toast.makeText(context, context.getString(R.string.welcome_back), android.widget.Toast.LENGTH_SHORT).show()
                                        navController.navigate("seller_home") {
                                            popUpTo(0) { inclusive = true }
                                        }
                                    } else {
                                        errorMessage = error ?: context.getString(R.string.login_failed)
                                    }
                                }
                            } else {
                                authViewModel.signUpWithEmailPassword(email, password, fullName) { success, error ->
                                    isLoading = false
                                    if (success) {
                                        isSeller?.value = true
                                        android.widget.Toast.makeText(context, context.getString(R.string.seller_account_created), android.widget.Toast.LENGTH_SHORT).show()
                                        navController.navigate("seller_home") {
                                            popUpTo(0) { inclusive = true }
                                        }
                                    } else {
                                        errorMessage = error ?: context.getString(R.string.registration_failed)
                                    }
                                }
                            }
                        },
                        enabled = !isLoading,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(24.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Primary)
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(modifier = Modifier.size(24.dp), color = androidx.compose.ui.graphics.Color.White, strokeWidth = 2.dp)
                        } else {
                            Text(if (isLogin) stringResource(R.string.login_as_seller) else stringResource(R.string.create_seller_account), fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
                        }
                    }
                }
            }
        }

        // Forgot Password Dialog
        if (showForgotDialog) {
            AlertDialog(
                onDismissRequest = { showForgotDialog = false; forgotMessage = null },
                title = { Text(stringResource(R.string.reset_password)) },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(stringResource(R.string.reset_password_desc), fontSize = 14.sp)
                        OutlinedTextField(
                            value = forgotEmail,
                            onValueChange = { forgotEmail = it; forgotMessage = null },
                            modifier = Modifier.fillMaxWidth(),
                            placeholder = { Text(stringResource(R.string.email_hint)) },
                            keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(keyboardType = androidx.compose.ui.text.input.KeyboardType.Email),
                            singleLine = true,
                            shape = RoundedCornerShape(12.dp)
                        )
                        if (forgotMessage != null) {
                            Text(
                                forgotMessage ?: "",
                                fontSize = 12.sp,
                                color = if (forgotMessage?.startsWith("Sent") == true) androidx.compose.ui.graphics.Color(0xFF2E7D32) else androidx.compose.ui.graphics.Color.Red
                            )
                        }
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            if (forgotEmail.isBlank()) {
                                forgotMessage = context.getString(R.string.error_email_required)
                                return@Button
                            }
                            forgotLoading = true
                            forgotMessage = null
                            authViewModel.sendPasswordResetEmail(forgotEmail) { success, error ->
                                forgotLoading = false
                                if (success) {
                                    forgotMessage = context.getString(R.string.reset_email_sent)
                                } else {
                                    forgotMessage = error ?: context.getString(R.string.reset_email_failed)
                                }
                            }
                        },
                        enabled = !forgotLoading
                    ) {
                        if (forgotLoading) {
                            CircularProgressIndicator(modifier = Modifier.size(16.dp), strokeWidth = 2.dp)
                        } else {
                            Text(stringResource(R.string.send_link))
                        }
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showForgotDialog = false; forgotMessage = null }) {
                        Text(stringResource(R.string.cancel))
                    }
                }
            )
        }
    }
}

@Composable
fun RoleCard(
    modifier: Modifier = Modifier,
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    isActive: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(if (isActive) PrimaryFixedDim.copy(alpha = 0.2f) else Color.Transparent)
            .border(if (isActive) 2.dp else 1.dp, if (isActive) Primary else OutlineVariant, RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(icon, contentDescription = null, tint = if (isActive) Primary else OnSurfaceVariant, modifier = Modifier.size(28.dp))
            Spacer(modifier = Modifier.height(4.dp))
            Text(title, style = MaterialTheme.typography.labelLarge, color = if (isActive) Primary else OnSurface, textAlign = TextAlign.Center)
            if (isActive) {
                Text(stringResource(R.string.setup_shop), style = MaterialTheme.typography.bodySmall, color = Primary.copy(alpha = 0.8f), fontSize = 10.sp)
                Box(
                    modifier = Modifier
                        .align(Alignment.End)
                        .offset(x = 12.dp, y = (-52).dp)
                        .size(20.dp)
                        .clip(CircleShape)
                        .background(Primary),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Filled.Check, contentDescription = null, tint = OnPrimary, modifier = Modifier.size(14.dp))
                }
            }
        }
    }
}
