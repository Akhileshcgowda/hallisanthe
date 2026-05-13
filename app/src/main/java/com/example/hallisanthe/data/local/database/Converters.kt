package com.example.hallisanthe.data.local.database

import androidx.room.TypeConverter
import com.example.hallisanthe.data.local.entity.OrderItemSnapshot
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {
    private val gson = Gson()

    @TypeConverter
    fun fromOrderItemSnapshotList(value: List<OrderItemSnapshot>): String {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toOrderItemSnapshotList(value: String): List<OrderItemSnapshot> {
        val type = object : TypeToken<List<OrderItemSnapshot>>() {}.type
        return gson.fromJson(value, type) ?: emptyList()
    }
}
