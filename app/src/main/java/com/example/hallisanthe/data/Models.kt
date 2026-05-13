package com.example.hallisanthe.data

data class Product(
    val id: String = "",
    val name: String = "",
    val artisan: String = "",
    val location: String = "",
    val price: Int = 0,
    val originalPrice: Int? = null,
    val imageUrl: String = "",
    val category: String = "",
    val tags: List<String> = emptyList(),
    val isVerified: Boolean = true,
    val isFeatured: Boolean = false,
    val description: String = "",
    val heritage: String = ""
)

data class Artisan(
    val id: String = "",
    val name: String = "",
    val location: String = "",
    val avatarUrl: String = "",
    val bio: String = "",
    val craft: String = "",
    val phone: String = "",
    val email: String = ""
)

data class CartItem(
    val product: Product,
    var quantity: Int = 1
)

data class Category(
    val id: String = "",
    val name: String = "",
    val imageUrl: String = "",
    val icon: String = ""
)

data class Order(
    val id: String,
    val date: String,
    val status: String,
    val items: List<CartItem>,
    val total: Int
)

data class Notification(
    val id: String,
    val title: String,
    val message: String,
    val time: String,
    val isRead: Boolean = false,
    val type: String = "info" // promo, order, info
)

data class Inquiry(
    val id: String,
    val customerName: String,
    val productName: String,
    val lastMessage: String,
    val time: String,
    val isUnread: Boolean,
    val customerAvatarUrl: String = ""
)

data class SellerDashboardMetrics(
    val totalSales: String,
    val activeListings: Int,
    val pendingInquiries: Int,
    val profileViews: Int,
    val salesTrend: Double // Percentage
)
