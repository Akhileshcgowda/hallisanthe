package com.example.hallisanthe.ui.screens

import com.example.hallisanthe.R
import android.app.Activity
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.hallisanthe.data.viewmodel.AuthViewModel
import com.example.hallisanthe.ui.theme.*
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginRegisterScreen(navController: NavController, isSeller: MutableState<Boolean>, authViewModel: AuthViewModel) {
    var isLoginTab by remember { mutableStateOf(true) }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var fullName by remember { mutableStateOf("") }
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
    ) {
        // Kolam background pattern
        Box(
            modifier = Modifier
                .fillMaxSize()
                .kolamBackground(OutlineVariant.copy(alpha = 0.08f))
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .imePadding()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .widthIn(max = 440.dp)
                    .shadow(12.dp, RoundedCornerShape(12.dp), spotColor = Primary)
                    .background(SurfaceContainerLowest, RoundedCornerShape(12.dp))
                    .padding(24.dp)
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    // Header Identity
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        androidx.compose.foundation.Image(
                            painter = androidx.compose.ui.res.painterResource(id = com.example.hallisanthe.R.drawable.logo),
                            contentDescription = "Logo",
                            modifier = Modifier
                                .size(80.dp)
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                        Text(
                            text = stringResource(R.string.login_register_title),
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = OnSurface
                        )
                    }

                    // Segmented Tabs
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(SurfaceContainerHigh, RoundedCornerShape(8.dp))
                            .padding(4.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .background(
                                    if (isLoginTab) SurfaceContainerLowest else Color.Transparent,
                                    RoundedCornerShape(4.dp)
                                )
                                .shadow(if (isLoginTab) 1.dp else 0.dp, RoundedCornerShape(4.dp))
                                .clickable { isLoginTab = true }
                                .padding(vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                stringResource(R.string.login_tab),
                                fontWeight = FontWeight.SemiBold,
                                color = if (isLoginTab) Primary else OnSurfaceVariant,
                                fontSize = 12.sp
                            )
                        }
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .background(
                                    if (!isLoginTab) SurfaceContainerLowest else Color.Transparent,
                                    RoundedCornerShape(4.dp)
                                )
                                .shadow(if (!isLoginTab) 1.dp else 0.dp, RoundedCornerShape(4.dp))
                                .clickable { isLoginTab = false }
                                .padding(vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                stringResource(R.string.register_tab),
                                fontWeight = FontWeight.SemiBold,
                                color = if (!isLoginTab) Primary else OnSurfaceVariant,
                                fontSize = 12.sp
                            )
                        }
                    }

                    // Role Selector
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(
                            stringResource(R.string.role_prompt),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = OnSurface,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                            // Buyer Card
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(if (!isSeller.value) Color(0xFFFFDBCD) else SurfaceContainerLowest)
                                    .border(
                                        width = if (!isSeller.value) 2.dp else 1.dp,
                                        color = if (!isSeller.value) Primary else OutlineVariant,
                                        shape = RoundedCornerShape(12.dp)
                                    )
                                    .clickable { isSeller.value = false }
                                    .padding(16.dp)
                            ) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Icon(
                                        Icons.Filled.ShoppingBag,
                                        contentDescription = "Buyer",
                                        tint = if (!isSeller.value) Primary else OnSurfaceVariant,
                                        modifier = Modifier.padding(bottom = 4.dp)
                                    )
                                    Text(
                                        stringResource(R.string.role_buyer),
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 12.sp,
                                        color = if (!isSeller.value) Color(0xFF360F00) else OnSurface,
                                        modifier = Modifier.padding(bottom = 4.dp)
                                    )
                                    Text(
                                        stringResource(R.string.role_buyer_desc),
                                        fontSize = 11.sp,
                                        color = if (!isSeller.value) Color(0xFF7C2E00) else OnSurfaceVariant,
                                        textAlign = TextAlign.Center,
                                        lineHeight = 14.sp
                                    )
                                }
                            }
                            
                            // Seller Card
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(if (isSeller.value) Color(0xFFFFDBCD) else SurfaceContainerLowest)
                                    .border(
                                        width = if (isSeller.value) 2.dp else 1.dp,
                                        color = if (isSeller.value) Primary else OutlineVariant,
                                        shape = RoundedCornerShape(12.dp)
                                    )
                                    .clickable { isSeller.value = true }
                                    .padding(16.dp)
                            ) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Icon(
                                        Icons.Filled.Storefront,
                                        contentDescription = "Seller",
                                        tint = if (isSeller.value) Primary else OnSurfaceVariant,
                                        modifier = Modifier.padding(bottom = 4.dp)
                                    )
                                    Text(
                                        stringResource(R.string.role_seller),
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 12.sp,
                                        color = if (isSeller.value) Color(0xFF360F00) else OnSurface,
                                        modifier = Modifier.padding(bottom = 4.dp)
                                    )
                                    Text(
                                        stringResource(R.string.role_seller_desc),
                                        fontSize = 11.sp,
                                        color = if (isSeller.value) Color(0xFF7C2E00) else OnSurfaceVariant,
                                        textAlign = TextAlign.Center,
                                        lineHeight = 14.sp
                                    )
                                }
                            }
                        }
                    }

                    // Error message
                    if (errorMessage != null) {
                        Text(
                            errorMessage ?: "",
                            color = androidx.compose.ui.graphics.Color.Red,
                            fontSize = 12.sp,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    // Fields
                    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        if (!isLoginTab) {
                            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                Text(stringResource(R.string.full_name), fontWeight = FontWeight.SemiBold, fontSize = 12.sp, color = OnSurface)
                                OutlinedTextField(
                                    value = fullName,
                                    onValueChange = { fullName = it },
                                    modifier = Modifier.fillMaxWidth(),
                                    placeholder = { Text(stringResource(R.string.full_name_hint), color = OutlineVariant) },
                                    shape = RoundedCornerShape(12.dp),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedContainerColor = SurfaceContainerLowest,
                                        unfocusedContainerColor = SurfaceContainerLowest,
                                        focusedBorderColor = Primary,
                                        unfocusedBorderColor = Outline
                                    ),
                                    singleLine = true
                                )
                            }
                        }

                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text(stringResource(R.string.email), fontWeight = FontWeight.SemiBold, fontSize = 12.sp, color = OnSurface)
                            OutlinedTextField(
                                value = email,
                                onValueChange = { email = it },
                                modifier = Modifier.fillMaxWidth(),
                                placeholder = { Text(stringResource(R.string.email_hint), color = OutlineVariant) },
                                shape = RoundedCornerShape(12.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedContainerColor = SurfaceContainerLowest,
                                    unfocusedContainerColor = SurfaceContainerLowest,
                                    focusedBorderColor = Primary,
                                    unfocusedBorderColor = Outline
                                ),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                                singleLine = true
                            )
                        }

                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text(stringResource(R.string.password), fontWeight = FontWeight.SemiBold, fontSize = 12.sp, color = OnSurface)
                                Text(
                                    stringResource(R.string.forgot_password),
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 12.sp,
                                    color = Primary,
                                    modifier = Modifier.clickable { showForgotDialog = true; forgotEmail = email; forgotMessage = null }
                                )
                            }
                            OutlinedTextField(
                                value = password,
                                onValueChange = { password = it },
                                modifier = Modifier.fillMaxWidth(),
                                placeholder = { Text(stringResource(R.string.password_hint), color = OutlineVariant) },
                                trailingIcon = {
                                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                        Icon(
                                            imageVector = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                                            contentDescription = if (passwordVisible) stringResource(R.string.hide_password) else stringResource(R.string.show_password),
                                            tint = OnSurfaceVariant
                                        )
                                    }
                                },
                                visualTransformation = if (passwordVisible) androidx.compose.ui.text.input.VisualTransformation.None else PasswordVisualTransformation(),
                                shape = RoundedCornerShape(12.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedContainerColor = SurfaceContainerLowest,
                                    unfocusedContainerColor = SurfaceContainerLowest,
                                    focusedBorderColor = Primary,
                                    unfocusedBorderColor = Outline
                                ),
                                singleLine = true
                            )
                        }

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
                                if (!isLoginTab && fullName.isBlank()) {
                                    errorMessage = context.getString(R.string.error_full_name_required)
                                    return@Button
                                }
                                errorMessage = null
                                isLoading = true
                                if (isLoginTab) {
                                    authViewModel.signInWithEmailPassword(email, password) { success, error ->
                                        isLoading = false
                                        if (success) {
                                            Toast.makeText(context, context.getString(R.string.welcome_back), Toast.LENGTH_SHORT).show()
                                            if (isSeller.value) navController.navigate("seller_home") { popUpTo(0) }
                                            else navController.navigate("home") { popUpTo(0) }
                                        } else {
                                            errorMessage = error ?: context.getString(R.string.login_failed)
                                        }
                                    }
                                } else {
                                    authViewModel.signUpWithEmailPassword(email, password, fullName) { success, error ->
                                        isLoading = false
                                        if (success) {
                                            Toast.makeText(context, context.getString(R.string.account_created), Toast.LENGTH_SHORT).show()
                                            if (isSeller.value) navController.navigate("seller_home") { popUpTo(0) }
                                            else navController.navigate("home") { popUpTo(0) }
                                        } else {
                                            errorMessage = error ?: context.getString(R.string.registration_failed)
                                        }
                                    }
                                }
                            },
                            enabled = !isLoading,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp),
                            shape = CircleShape,
                            colors = ButtonDefaults.buttonColors(containerColor = Primary),
                            elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
                        ) {
                            if (isLoading) {
                                CircularProgressIndicator(modifier = Modifier.size(18.dp), color = androidx.compose.ui.graphics.Color.White, strokeWidth = 2.dp)
                            } else {
                                Text(if (isLoginTab) stringResource(R.string.login) else stringResource(R.string.create_account), fontWeight = FontWeight.SemiBold, fontSize = 12.sp)
                                Spacer(Modifier.width(4.dp))
                                Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, modifier = Modifier.size(18.dp))
                            }
                        }
                    }

                    // Divider
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                        HorizontalDivider(modifier = Modifier.weight(1f), color = OutlineVariant)
                        Text(stringResource(R.string.or), color = OnSurfaceVariant, fontSize = 14.sp, modifier = Modifier.padding(horizontal = 8.dp))
                        HorizontalDivider(modifier = Modifier.weight(1f), color = OutlineVariant)
                    }

                    // Google Sign-In Button
                    val googleSignInLauncher = rememberLauncherForActivityResult(
                        contract = ActivityResultContracts.StartActivityForResult()
                    ) { result ->
                        if (result.resultCode != Activity.RESULT_OK) {
                            Log.w("GoogleSignIn", "Sign-in cancelled or failed. resultCode=${result.resultCode}")
                            if (result.resultCode == Activity.RESULT_CANCELED) {
                                Toast.makeText(context, "Sign-in cancelled", Toast.LENGTH_SHORT).show()
                            }
                            return@rememberLauncherForActivityResult
                        }
                        val data = result.data
                        if (data == null) {
                            Toast.makeText(context, "Google sign-in failed: no data returned", Toast.LENGTH_LONG).show()
                            return@rememberLauncherForActivityResult
                        }
                        val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                        try {
                            val account = task.getResult(ApiException::class.java)
                            val idToken = account?.idToken
                            Log.d("GoogleSignIn", "account=${account?.email}, idToken present=${idToken != null}")
                            if (idToken != null) {
                                authViewModel.signInWithGoogle(idToken) { success, error ->
                                    if (success) {
                                        Toast.makeText(context, "Welcome ${authViewModel.currentUser.value?.displayName ?: ""}!", Toast.LENGTH_SHORT).show()
                                        if (isSeller.value) {
                                            navController.navigate("seller_home") { popUpTo(0) }
                                        } else {
                                            navController.navigate("home") { popUpTo(0) }
                                        }
                                    } else {
                                        Log.e("GoogleSignIn", "Firebase auth failed: $error")
                                        Toast.makeText(context, "Sign-in failed: $error", Toast.LENGTH_LONG).show()
                                    }
                                }
                            } else {
                                Toast.makeText(context, "Google sign-in failed: No ID token returned. Ensure Google Sign-In is enabled in Firebase Console.", Toast.LENGTH_LONG).show()
                            }
                        } catch (e: ApiException) {
                            val statusCode = e.statusCode
                            val message = when (statusCode) {
                                10 -> "Developer error (code 10).\n\nCAUSE: Your app's SHA-1 fingerprint is not registered in Firebase Console.\n\nFIX: Go to Firebase Console > Project Settings > Your App > Add SHA-1 fingerprint, then re-download google-services.json."
                                12500 -> "Sign-in failed (code 12500). Google Sign-In may not be enabled for this app in Google Cloud Console, or the OAuth consent screen is not configured."
                                12501 -> "Sign-in cancelled by user (code 12501)."
                                12502 -> "Sign-in in progress (code 12502)."
                                else -> "Google sign-in error (code $statusCode): ${e.message}"
                            }
                            Log.e("GoogleSignIn", "ApiException statusCode=$statusCode", e)
                            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
                        }
                    }

                    val webClientId = "1071393718537-5ol918ggpvh5qqngdivkbojs1qes2erp.apps.googleusercontent.com"

                    OutlinedButton(
                        onClick = {
                            if (webClientId.isBlank()) {
                                Toast.makeText(context, "Google Sign-In not configured. Go to Firebase Console > Authentication > Sign-in method > Google, enable it, then download a new google-services.json", Toast.LENGTH_LONG).show()
                                return@OutlinedButton
                            }
                            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                                .requestIdToken(webClientId)
                                .requestEmail()
                                .build()
                            val googleSignInClient = GoogleSignIn.getClient(context, gso)
                            googleSignInClient.signOut().addOnCompleteListener {
                                val signInIntent = googleSignInClient.signInIntent
                                googleSignInLauncher.launch(signInIntent)
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        shape = CircleShape,
                        colors = ButtonDefaults.outlinedButtonColors(containerColor = SurfaceContainerLowest),
                        border = BorderStroke(1.dp, Outline)
                    ) {
                        Text(stringResource(R.string.continue_with_google), color = OnSurface, fontWeight = FontWeight.SemiBold, fontSize = 12.sp)
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
                            placeholder = { Text(stringResource(R.string.email)) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
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
                                forgotMessage = "Please enter your email"
                                return@Button
                            }
                            forgotLoading = true
                            forgotMessage = null
                            authViewModel.sendPasswordResetEmail(forgotEmail) { success, error ->
                                forgotLoading = false
                                if (success) {
                                    forgotMessage = "Sent! Check your inbox."
                                } else {
                                    forgotMessage = error ?: "Failed to send reset email"
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
