package com.example.hallisanthe.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.hallisanthe.data.local.entity.ArtisanEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ArtisanDao {

    @Query("SELECT * FROM artisans ORDER BY name ASC")
    fun getAllArtisans(): Flow<List<ArtisanEntity>>

    @Query("SELECT * FROM artisans WHERE id = :id")
    suspend fun getArtisanById(id: String): ArtisanEntity?

    @Query("SELECT * FROM artisans WHERE email = :email LIMIT 1")
    suspend fun getArtisanByEmail(email: String): ArtisanEntity?

    @Query("SELECT * FROM artisans WHERE village = :village ORDER BY name ASC")
    fun getArtisansByVillage(village: String): Flow<List<ArtisanEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArtisan(artisan: ArtisanEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArtisans(artisans: List<ArtisanEntity>)

    @Update
    suspend fun updateArtisan(artisan: ArtisanEntity)

    @Delete
    suspend fun deleteArtisan(artisan: ArtisanEntity)

    @Query("DELETE FROM artisans WHERE id = :id")
    suspend fun deleteArtisanById(id: String)
}
