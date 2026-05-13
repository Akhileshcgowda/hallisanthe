package com.example.hallisanthe.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "orders")
data class OrderEntity(
    @PrimaryKey val id: String = "",
    val userId: String = "",
    val itemsJson: String = "",
    val total: Int = 0,
    val status: String = "Pending",
    val timestamp: Long = System.currentTimeMillis(),
    val paymentMethod: String = "",
    val shippingName: String = "",
    val shippingAddress: String = "",
    val shippingCity: String = ""
)
