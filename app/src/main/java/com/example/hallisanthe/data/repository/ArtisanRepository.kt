package com.example.hallisanthe.data.repository

import com.example.hallisanthe.data.Artisan
import com.example.hallisanthe.data.local.dao.ArtisanDao
import com.example.hallisanthe.data.local.entity.ArtisanEntity
import com.example.hallisanthe.data.mapper.toDomain
import com.example.hallisanthe.data.mapper.toEntity
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withTimeoutOrNull

class ArtisanRepository(
    private val artisanDao: ArtisanDao,
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) {

    private val artisansCollection = firestore.collection("artisans")

    fun getAllArtisans(): Flow<List<Artisan>> = combine(
        artisanDao.getAllArtisans().map { list -> list.map { it.toDomain() } },
        callbackFlow {
            trySend(emptyList())
            val listener = artisansCollection.addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(emptyList())
                    return@addSnapshotListener
                }
                val artisans = snapshot?.documents?.mapNotNull { it.toObject(Artisan::class.java) } ?: emptyList()
                trySend(artisans)
            }
            awaitClose { listener.remove() }
        }
    ) { room, firestore ->
        (room + firestore).distinctBy { it.id }
    }

    fun getArtisansByVillage(village: String): Flow<List<Artisan>> = combine(
        artisanDao.getArtisansByVillage(village).map { list -> list.map { it.toDomain() } },
        callbackFlow {
            trySend(emptyList())
            val query = artisansCollection.whereEqualTo("location", village)
            val listener = query.addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(emptyList())
                    return@addSnapshotListener
                }
                val artisans = snapshot?.documents?.mapNotNull { it.toObject(Artisan::class.java) } ?: emptyList()
                trySend(artisans)
            }
            awaitClose { listener.remove() }
        }
    ) { room, firestore ->
        (room + firestore).distinctBy { it.id }
    }

    suspend fun getArtisanById(id: String): Artisan? {
        return try {
            withTimeoutOrNull(3000L) {
                val doc = artisansCollection.document(id).get().await()
                doc.toObject(Artisan::class.java)
            }
        } catch (e: Exception) {
            null
        } ?: artisanDao.getArtisanById(id)?.toDomain()
    }

    suspend fun getArtisanByEmail(email: String): Artisan? {
        return try {
            withTimeoutOrNull(3000L) {
                val query = artisansCollection.whereEqualTo("email", email).get().await()
                query.documents.firstOrNull()?.toObject(Artisan::class.java)
            }
        } catch (e: Exception) {
            null
        } ?: artisanDao.getArtisanByEmail(email)?.toDomain()
    }

    suspend fun insertArtisan(artisan: Artisan) {
        artisanDao.insertArtisan(artisan.toEntity())
        withTimeoutOrNull(3000L) {
            try {
                artisansCollection.document(artisan.id).set(artisan).await()
            } catch (e: Exception) {
                // Local insert succeeded; Firestore sync is best-effort
            }
        }
    }

    suspend fun insertArtisans(artisans: List<Artisan>) {
        artisanDao.insertArtisans(artisans.map { it.toEntity() })
        withTimeoutOrNull(3000L) {
            try {
                val batch = firestore.batch()
                artisans.forEach { artisan ->
                    batch.set(artisansCollection.document(artisan.id), artisan)
                }
                batch.commit().await()
            } catch (e: Exception) {
                // Local insert succeeded; Firestore sync is best-effort
            }
        }
    }

    suspend fun updateArtisan(artisan: Artisan) {
        artisanDao.updateArtisan(artisan.toEntity())
        withTimeoutOrNull(3000L) {
            try {
                artisansCollection.document(artisan.id).set(artisan).await()
            } catch (e: Exception) {
                // Local update succeeded; Firestore sync is best-effort
            }
        }
    }

    suspend fun deleteArtisan(artisan: Artisan) {
        artisanDao.deleteArtisan(artisan.toEntity())
        withTimeoutOrNull(3000L) {
            try {
                artisansCollection.document(artisan.id).delete().await()
            } catch (e: Exception) {
                // Local delete succeeded; Firestore sync is best-effort
            }
        }
    }

    suspend fun deleteArtisanById(id: String) {
        artisanDao.deleteArtisanById(id)
        withTimeoutOrNull(3000L) {
            try {
                artisansCollection.document(id).delete().await()
            } catch (e: Exception) {
                // Local delete succeeded; Firestore sync is best-effort
            }
        }
    }

    suspend fun seedArtisans(artisans: List<ArtisanEntity>) {
        artisanDao.insertArtisans(artisans)
        withTimeoutOrNull(3000L) {
            try {
                val batch = firestore.batch()
                artisans.forEach { entity ->
                    val artisan = entity.toDomain()
                    batch.set(artisansCollection.document(entity.id), artisan)
                }
                batch.commit().await()
            } catch (e: Exception) {
                // Local seed succeeded; Firestore sync is best-effort
            }
        }
    }
}
