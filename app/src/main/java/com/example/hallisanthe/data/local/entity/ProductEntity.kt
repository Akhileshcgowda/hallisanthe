package com.example.hallisanthe.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products")
data class ProductEntity(
    @PrimaryKey val id: String,
    val name: String,
    val price: Int,
    val originalPrice: Int? = null,
    val category: String,
    val imageUrl: String,
    val artisanId: String,
    val artisanName: String,
    val location: String,
    val description: String = "",
    val heritage: String = "",
    val tags: String = "",
    val isVerified: Boolean = true,
    val isFeatured: Boolean = false,
    val timestamp: Long = System.currentTimeMillis()
)
