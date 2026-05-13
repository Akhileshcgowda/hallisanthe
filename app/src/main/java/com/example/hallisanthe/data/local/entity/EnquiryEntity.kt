package com.example.hallisanthe.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "enquiries",
    foreignKeys = [
        ForeignKey(
            entity = ProductEntity::class,
            parentColumns = ["id"],
            childColumns = ["productId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = ArtisanEntity::class,
            parentColumns = ["id"],
            childColumns = ["artisanId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("productId"), Index("artisanId")]
)
data class EnquiryEntity(
    @PrimaryKey val id: String = "",
    val buyerId: String = "",
    val artisanId: String = "",
    val productId: String = "",
    val buyerName: String = "",
    val productName: String = "",
    val message: String = "",
    val timestamp: Long = System.currentTimeMillis(),
    val isUnread: Boolean = true
)
