package com.example.hallisanthe.data.repository

import com.example.hallisanthe.data.Inquiry
import com.example.hallisanthe.data.Notification
import com.example.hallisanthe.data.local.dao.EnquiryDao
import com.example.hallisanthe.data.local.entity.EnquiryEntity
import com.example.hallisanthe.data.mapper.toInquiry
import com.example.hallisanthe.data.mapper.toNotification
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withTimeoutOrNull

class EnquiryRepository(
    private val enquiryDao: EnquiryDao,
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) {

    private val enquiriesCollection = firestore.collection("enquiries")

    private fun firestoreAllEnquiries(): Flow<List<EnquiryEntity>> = callbackFlow {
        trySend(emptyList())
        val listener = enquiriesCollection.addSnapshotListener { snapshot, error ->
            if (error != null) {
                trySend(emptyList())
                return@addSnapshotListener
            }
            val enquiries = snapshot?.documents?.mapNotNull { it.toObject(EnquiryEntity::class.java) } ?: emptyList()
            trySend(enquiries)
        }
        awaitClose { listener.remove() }
    }

    fun getAllEnquiries(): Flow<List<Inquiry>> = combine(
        enquiryDao.getAllEnquiries().map { list -> list.map { it.toInquiry() } },
        firestoreAllEnquiries().map { list -> list.map { it.toInquiry() } }
    ) { room, firestore -> (room + firestore).distinctBy { it.id } }

    fun getEnquiriesForArtisan(artisanId: String): Flow<List<Inquiry>> = combine(
        enquiryDao.getEnquiriesForArtisan(artisanId).map { list -> list.map { it.toInquiry() } },
        callbackFlow {
            trySend(emptyList())
            val query = enquiriesCollection.whereEqualTo("artisanId", artisanId)
            val listener = query.addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(emptyList())
                    return@addSnapshotListener
                }
                val enquiries = snapshot?.documents?.mapNotNull { it.toObject(EnquiryEntity::class.java) } ?: emptyList()
                trySend(enquiries.map { it.toInquiry() })
            }
            awaitClose { listener.remove() }
        }
    ) { room, firestore -> (room + firestore).distinctBy { it.id } }

    fun getEnquiriesForProduct(productId: String): Flow<List<Inquiry>> = combine(
        enquiryDao.getEnquiriesForProduct(productId).map { list -> list.map { it.toInquiry() } },
        callbackFlow {
            trySend(emptyList())
            val query = enquiriesCollection.whereEqualTo("productId", productId)
            val listener = query.addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(emptyList())
                    return@addSnapshotListener
                }
                val enquiries = snapshot?.documents?.mapNotNull { it.toObject(EnquiryEntity::class.java) } ?: emptyList()
                trySend(enquiries.map { it.toInquiry() })
            }
            awaitClose { listener.remove() }
        }
    ) { room, firestore -> (room + firestore).distinctBy { it.id } }

    fun getUnreadEnquiriesForArtisan(artisanId: String): Flow<List<Inquiry>> = combine(
        enquiryDao.getUnreadEnquiriesForArtisan(artisanId).map { list -> list.map { it.toInquiry() } },
        callbackFlow {
            trySend(emptyList())
            val query = enquiriesCollection
                .whereEqualTo("artisanId", artisanId)
                .whereEqualTo("isUnread", true)
            val listener = query.addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(emptyList())
                    return@addSnapshotListener
                }
                val enquiries = snapshot?.documents?.mapNotNull { it.toObject(EnquiryEntity::class.java) } ?: emptyList()
                trySend(enquiries.map { it.toInquiry() })
            }
            awaitClose { listener.remove() }
        }
    ) { room, firestore -> (room + firestore).distinctBy { it.id } }

    fun getNotificationsForArtisan(artisanId: String): Flow<List<Notification>> = combine(
        enquiryDao.getEnquiriesForArtisan(artisanId).map { list -> list.map { it.toNotification() } },
        callbackFlow {
            trySend(emptyList())
            val query = enquiriesCollection.whereEqualTo("artisanId", artisanId)
            val listener = query.addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(emptyList())
                    return@addSnapshotListener
                }
                val enquiries = snapshot?.documents?.mapNotNull { it.toObject(EnquiryEntity::class.java) } ?: emptyList()
                trySend(enquiries.map { it.toNotification() })
            }
            awaitClose { listener.remove() }
        }
    ) { room, firestore -> (room + firestore).distinctBy { it.id } }

    suspend fun getUnreadEnquiryCount(artisanId: String): Int {
        return try {
            withTimeoutOrNull(3000L) {
                val snapshot = enquiriesCollection
                    .whereEqualTo("artisanId", artisanId)
                    .whereEqualTo("isUnread", true)
                    .get().await()
                snapshot.size()
            }
        } catch (e: Exception) {
            null
        } ?: enquiryDao.getUnreadEnquiryCount(artisanId)
    }

    suspend fun insertEnquiry(enquiry: EnquiryEntity) {
        enquiryDao.insertEnquiry(enquiry)
        withTimeoutOrNull(3000L) {
            try {
                enquiriesCollection.document(enquiry.id).set(enquiry).await()
            } catch (e: Exception) {
                // Local insert succeeded; Firestore sync is best-effort
            }
        }
    }

    suspend fun markAsRead(id: String) {
        enquiryDao.markAsRead(id)
        withTimeoutOrNull(3000L) {
            try {
                enquiriesCollection.document(id).update("isUnread", false).await()
            } catch (e: Exception) {
                // Local update succeeded; Firestore sync is best-effort
            }
        }
    }

    suspend fun deleteEnquiry(enquiry: EnquiryEntity) {
        enquiryDao.deleteEnquiry(enquiry)
        withTimeoutOrNull(3000L) {
            try {
                enquiriesCollection.document(enquiry.id).delete().await()
            } catch (e: Exception) {
                // Local delete succeeded; Firestore sync is best-effort
            }
        }
    }

    suspend fun deleteEnquiryById(id: String) {
        enquiryDao.deleteEnquiryById(id)
        withTimeoutOrNull(3000L) {
            try {
                enquiriesCollection.document(id).delete().await()
            } catch (e: Exception) {
                // Local delete succeeded; Firestore sync is best-effort
            }
        }
    }
}
