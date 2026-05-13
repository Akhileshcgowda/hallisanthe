package com.example.hallisanthe.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.hallisanthe.data.local.entity.EnquiryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface EnquiryDao {

    @Query("SELECT * FROM enquiries ORDER BY timestamp DESC")
    fun getAllEnquiries(): Flow<List<EnquiryEntity>>

    @Query("SELECT * FROM enquiries WHERE artisanId = :artisanId ORDER BY timestamp DESC")
    fun getEnquiriesForArtisan(artisanId: String): Flow<List<EnquiryEntity>>

    @Query("SELECT * FROM enquiries WHERE productId = :productId ORDER BY timestamp DESC")
    fun getEnquiriesForProduct(productId: String): Flow<List<EnquiryEntity>>

    @Query("SELECT * FROM enquiries WHERE isUnread = 1 AND artisanId = :artisanId")
    fun getUnreadEnquiriesForArtisan(artisanId: String): Flow<List<EnquiryEntity>>

    @Query("SELECT COUNT(*) FROM enquiries WHERE isUnread = 1 AND artisanId = :artisanId")
    suspend fun getUnreadEnquiryCount(artisanId: String): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEnquiry(enquiry: EnquiryEntity)

    @Update
    suspend fun updateEnquiry(enquiry: EnquiryEntity)

    @Query("UPDATE enquiries SET isUnread = 0 WHERE id = :id")
    suspend fun markAsRead(id: String)

    @Delete
    suspend fun deleteEnquiry(enquiry: EnquiryEntity)

    @Query("DELETE FROM enquiries WHERE id = :id")
    suspend fun deleteEnquiryById(id: String)
}
