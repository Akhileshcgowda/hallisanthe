package com.example.hallisanthe.ui.screens

import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.platform.LocalContext
import android.widget.Toast
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.hallisanthe.data.repository.GenAIRepository
import com.example.hallisanthe.data.repository.ProductRepository
import androidx.compose.ui.res.stringResource
import com.example.hallisanthe.R
import com.example.hallisanthe.data.viewmodel.AuthViewModel
import com.example.hallisanthe.ui.theme.*
import com.example.hallisanthe.ui.utils.ImageCompressor
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddProductScreen(
    navController: NavController,
    genAIRepository: GenAIRepository,
    productRepository: ProductRepository,
    authViewModel: AuthViewModel
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val currentUser by authViewModel.currentUser.collectAsState()

    var productName by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var sellerName by remember { mutableStateOf(currentUser?.displayName ?: "") }
    var expanded by remember { mutableStateOf(false) }
    var isUploading by remember { mutableStateOf(false) }
    var uploadError by remember { mutableStateOf<String?>(null) }
    val categories = listOf("Textiles & Handlooms", "Woodwork & Carving", "Pottery & Ceramics", "Jewelry")

    // Image selection
    var selectedImageUri by remember { mutableStateOf<android.net.Uri?>(null) }
    var showImagePickerDialog by remember { mutableStateOf(false) }
    var cameraImageUri by remember { mutableStateOf<android.net.Uri?>(null) }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let { selectedImageUri = it }
        showImagePickerDialog = false
    }

    fun createImageFileUri(): android.net.Uri {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val imageFileName = "JPEG_${timeStamp}_"
        val storageDir = context.getExternalFilesDir(android.os.Environment.DIRECTORY_PICTURES)
        val imageFile = File.createTempFile(imageFileName, ".jpg", storageDir)
        return FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            imageFile
        )
    }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            cameraImageUri?.let { selectedImageUri = it }
        }
        showImagePickerDialog = false
    }

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            cameraImageUri = createImageFileUri()
            cameraLauncher.launch(cameraImageUri!!)
        } else {
            Toast.makeText(context, context.getString(R.string.camera_permission_required), Toast.LENGTH_SHORT).show()
            showImagePickerDialog = false
        }
    }

    fun openCamera() {
        when {
            ContextCompat.checkSelfPermission(context, android.Manifest.permission.CAMERA) == android.content.pm.PackageManager.PERMISSION_GRANTED -> {
                cameraImageUri = createImageFileUri()
                cameraLauncher.launch(cameraImageUri!!)
            }
            else -> {
                cameraPermissionLauncher.launch(android.Manifest.permission.CAMERA)
            }
        }
    }

    Scaffold(
        containerColor = Background,
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("santhe_ai_assistant") },
                containerColor = SecondaryContainer,
                contentColor = OnSecondaryContainer,
                shape = CircleShape,
                modifier = Modifier.padding(bottom = 80.dp)
            ) {
                Icon(Icons.Filled.AutoAwesome, contentDescription = stringResource(R.string.ai_title))
            }
        },
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(SurfaceContainerLowest)
                    .border(1.dp, OutlineVariant)
                    .padding(16.dp)
            ) {
                Button(
                    onClick = {
                        uploadError = null
                        if (productName.isBlank() || category.isBlank() || price.isBlank() || location.isBlank() || description.isBlank()) {
                            uploadError = context.getString(R.string.error_fill_all_fields)
                            return@Button
                        }
                        val priceValue = price.toIntOrNull()
                        if (priceValue == null || priceValue <= 0) {
                            uploadError = context.getString(R.string.error_valid_price)
                            return@Button
                        }
                        val artisanId = currentUser?.uid
                        if (artisanId == null) {
                            uploadError = context.getString(R.string.error_login_to_add)
                            return@Button
                        }
                        coroutineScope.launch {
                            isUploading = true
                            uploadError = null
                            try {
                                val imageUrl = if (selectedImageUri != null) {
                                    ImageCompressor.compress(context, selectedImageUri!!).toString()
                                } else {
                                    ""
                                }
                                val product = com.example.hallisanthe.data.Product(
                                    id = System.currentTimeMillis().toString(),
                                    name = productName.trim(),
                                    artisan = sellerName.trim().ifBlank { currentUser?.displayName ?: "Unknown" },
                                    location = location.trim(),
                                    price = priceValue,
                                    imageUrl = imageUrl,
                                    category = category.lowercase().replace(" & ", "_").replace(" ", "_"),
                                    description = description.trim()
                                )
                                productRepository.insertProduct(product, artisanId)
                                Toast.makeText(context, context.getString(R.string.product_uploaded), Toast.LENGTH_SHORT).show()
                                navController.navigateUp()
                            } catch (e: Exception) {
                                Log.e("AddProduct", "Upload failed", e)
                                uploadError = "${context.getString(R.string.upload_failed)}: ${e.localizedMessage ?: context.getString(R.string.error_generic)}"
                            } finally {
                                isUploading = false
                            }
                        }
                    },
                    enabled = !isUploading,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(24.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Primary)
                ) {
                    if (isUploading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = Color.White,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text(stringResource(R.string.upload_product), fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Error banner
            if (uploadError != null) {
                Text(
                    uploadError ?: "",
                    color = androidx.compose.ui.graphics.Color.Red,
                    fontSize = 14.sp,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // Image Upload Box
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16f / 9f)
                    .clip(RoundedCornerShape(16.dp))
                    .background(SurfaceContainer)
                    .clickable { showImagePickerDialog = true },
                contentAlignment = Alignment.Center
            ) {
                Box(modifier = Modifier.matchParentSize().padding(2.dp).border(2.dp, Outline, RoundedCornerShape(16.dp)))
                if (selectedImageUri != null) {
                    AsyncImage(
                        model = selectedImageUri,
                        contentDescription = stringResource(R.string.selected_product_image),
                        modifier = Modifier.fillMaxSize(),
                        contentScale = androidx.compose.ui.layout.ContentScale.Crop
                    )
                } else {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .clip(CircleShape)
                                .background(Surface)
                                .padding(8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Filled.PhotoCamera, contentDescription = stringResource(R.string.upload_image), modifier = Modifier.size(32.dp), tint = Primary)
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(stringResource(R.string.upload_image), style = MaterialTheme.typography.titleLarge, color = OnSurface)
                        Text(stringResource(R.string.upload_image_desc), style = MaterialTheme.typography.bodyMedium, color = OnSurfaceVariant)
                    }
                }
            }

            // Image Picker Dialog
            if (showImagePickerDialog) {
                AlertDialog(
                    onDismissRequest = { showImagePickerDialog = false },
                    title = { Text(stringResource(R.string.select_image)) },
                    text = { Text(stringResource(R.string.select_image_desc)) },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                openCamera()
                            }
                        ) {
                            Icon(Icons.Filled.PhotoCamera, contentDescription = null, modifier = Modifier.padding(end = 8.dp))
                            Text(stringResource(R.string.camera))
                        }
                    },
                    dismissButton = {
                        TextButton(
                            onClick = {
                                galleryLauncher.launch("image/*")
                            }
                        ) {
                            Icon(Icons.Filled.PhotoLibrary, contentDescription = null, modifier = Modifier.padding(end = 8.dp))
                            Text(stringResource(R.string.gallery))
                        }
                    }
                )
            }

            // Kolam Divider (Simple text for now)
            Box(modifier = Modifier.fillMaxWidth().height(48.dp), contentAlignment = Alignment.Center) {
                Text("◆ ◇ ◆ ◇ ◆", color = Outline, letterSpacing = 8.sp, fontSize = 14.sp)
            }

            // Form Fields
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                // Product Name
                OutlinedTextField(
                    value = productName,
                    onValueChange = { productName = it },
                    label = { Text(stringResource(R.string.product_name)) },
                    placeholder = { Text(stringResource(R.string.product_name_hint)) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                // Category
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded }
                ) {
                    OutlinedTextField(
                        readOnly = true,
                        value = category,
                        onValueChange = { },
                        label = { Text(stringResource(R.string.product_category)) },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                        },
                        colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
                        modifier = Modifier.menuAnchor().fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        categories.forEach { selectionOption ->
                            DropdownMenuItem(
                                text = { Text(selectionOption) },
                                onClick = {
                                    category = selectionOption
                                    expanded = false
                                }
                            )
                        }
                    }
                }

                // Price & Location
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    OutlinedTextField(
                        value = price,
                        onValueChange = { price = it },
                        label = { Text(stringResource(R.string.product_price)) },
                        placeholder = { Text(stringResource(R.string.price_hint)) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp)
                    )
                    OutlinedTextField(
                        value = location,
                        onValueChange = { location = it },
                        label = { Text(stringResource(R.string.product_location)) },
                        placeholder = { Text(stringResource(R.string.location_hint)) },
                        leadingIcon = { Icon(Icons.Filled.LocationOn, contentDescription = null) },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp)
                    )
                }

                // Description
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text(stringResource(R.string.product_description)) },
                    placeholder = { Text(stringResource(R.string.description_hint)) },
                    modifier = Modifier.fillMaxWidth().height(120.dp),
                    shape = RoundedCornerShape(12.dp),
                    maxLines = 4
                )

                // Seller Name
                OutlinedTextField(
                    value = sellerName,
                    onValueChange = { sellerName = it },
                    label = { Text(stringResource(R.string.seller_name)) },
                    placeholder = { Text(stringResource(R.string.seller_name_hint)) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )
            }
        }
    }
}
