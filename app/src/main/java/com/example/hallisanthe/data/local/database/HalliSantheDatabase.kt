package com.example.hallisanthe.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.hallisanthe.data.local.dao.ArtisanDao
import com.example.hallisanthe.data.local.dao.CategoryDao
import com.example.hallisanthe.data.local.dao.EnquiryDao
import com.example.hallisanthe.data.local.dao.OrderDao
import com.example.hallisanthe.data.local.dao.ProductDao
import com.example.hallisanthe.data.local.entity.ArtisanEntity
import com.example.hallisanthe.data.local.entity.CategoryEntity
import com.example.hallisanthe.data.local.entity.EnquiryEntity
import com.example.hallisanthe.data.local.entity.OrderEntity
import com.example.hallisanthe.data.local.entity.ProductEntity

@Database(
    entities = [ProductEntity::class, ArtisanEntity::class, CategoryEntity::class, EnquiryEntity::class, OrderEntity::class],
    version = 2,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class HalliSantheDatabase : RoomDatabase() {

    abstract fun productDao(): ProductDao
    abstract fun artisanDao(): ArtisanDao
    abstract fun categoryDao(): CategoryDao
    abstract fun enquiryDao(): EnquiryDao
    abstract fun orderDao(): OrderDao

    companion object {
        @Volatile
        private var INSTANCE: HalliSantheDatabase? = null

        fun getDatabase(context: Context): HalliSantheDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    HalliSantheDatabase::class.java,
                    "halli_santhe_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
