package com.example.hallisanthe.data.local.entity

data class OrderItemSnapshot(
    val productId: String = "",
    val name: String = "",
    val price: Int = 0,
    val imageUrl: String = "",
    val quantity: Int = 1
)
