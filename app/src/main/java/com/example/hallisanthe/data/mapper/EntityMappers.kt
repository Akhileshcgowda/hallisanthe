package com.example.hallisanthe.data.mapper

import com.example.hallisanthe.data.Artisan
import com.example.hallisanthe.data.CartItem
import com.example.hallisanthe.data.Category
import com.example.hallisanthe.data.Inquiry
import com.example.hallisanthe.data.Notification
import com.example.hallisanthe.data.Product
import com.example.hallisanthe.data.SellerDashboardMetrics
import com.example.hallisanthe.data.local.entity.ArtisanEntity
import com.example.hallisanthe.data.local.entity.CategoryEntity
import com.example.hallisanthe.data.local.entity.EnquiryEntity
import com.example.hallisanthe.data.local.entity.ProductEntity

fun ProductEntity.toDomain(): Product = Product(
    id = id,
    name = name,
    artisan = artisanName,
    location = location,
    price = price,
    originalPrice = originalPrice,
    imageUrl = imageUrl,
    category = category,
    tags = if (tags.isNotBlank()) tags.split(",") else emptyList(),
    isVerified = isVerified,
    isFeatured = isFeatured,
    description = description,
    heritage = heritage
)

fun Product.toEntity(artisanId: String): ProductEntity = ProductEntity(
    id = id,
    name = name,
    price = price,
    originalPrice = originalPrice,
    category = category,
    imageUrl = imageUrl,
    artisanId = artisanId,
    artisanName = artisan,
    location = location,
    description = description,
    heritage = heritage,
    tags = tags.joinToString(","),
    isVerified = isVerified,
    isFeatured = isFeatured
)

fun ArtisanEntity.toDomain(): Artisan = Artisan(
    id = id,
    name = name,
    location = village,
    avatarUrl = profileImage,
    bio = bio,
    craft = craft,
    phone = phone,
    email = email
)

fun Artisan.toEntity(): ArtisanEntity = ArtisanEntity(
    id = id,
    name = name,
    village = location,
    profileImage = avatarUrl,
    bio = bio,
    craft = craft,
    phone = phone,
    email = email
)

fun CategoryEntity.toDomain(): Category = Category(
    id = id,
    name = name,
    imageUrl = imageUrl,
    icon = iconUrl
)

fun Category.toEntity(): CategoryEntity = CategoryEntity(
    id = id,
    name = name,
    imageUrl = imageUrl,
    iconUrl = icon
)

fun EnquiryEntity.toInquiry(): Inquiry = Inquiry(
    id = id,
    customerName = buyerName,
    productName = productName,
    lastMessage = message,
    time = formatTimestamp(timestamp),
    isUnread = isUnread
)

fun EnquiryEntity.toNotification(): Notification = Notification(
    id = "n_$id",
    title = "New Inquiry: $productName",
    message = "$buyerName: $message",
    time = formatTimestamp(timestamp),
    isRead = !isUnread,
    type = "info"
)

private fun formatTimestamp(timestamp: Long): String {
    val seconds = (System.currentTimeMillis() - timestamp) / 1000
    return when {
        seconds < 60 -> "Just now"
        seconds < 3600 -> "${seconds / 60}m ago"
        seconds < 86400 -> "${seconds / 3600}h ago"
        seconds < 604800 -> "${seconds / 86400}d ago"
        else -> "${seconds / 604800}w ago"
    }
}
