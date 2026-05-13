package com.example.hallisanthe.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "artisans")
data class ArtisanEntity(
    @PrimaryKey val id: String,
    val name: String,
    val village: String,
    val phone: String = "",
    val email: String = "",
    val profileImage: String = "",
    val bio: String = "",
    val craft: String = ""
)
