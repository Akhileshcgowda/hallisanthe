package com.example.hallisanthe.data

data class UserContext(
    val userName: String = "",
    val userEmail: String = "",
    val userPhone: String = "",
    val userId: String = "",
    val cartSummary: String = "",
    val wishlistSummary: String = "",
    val recentOrders: String = "",
    val sellerListings: String = "",
    val sellerInquiries: String = "",
    val artisanProfile: String = "",
    val isSeller: Boolean = false,
    val allProductsSummary: String = "",
    val allArtisansSummary: String = "",
    val language: String = "en"  // en, kn, hi
)
