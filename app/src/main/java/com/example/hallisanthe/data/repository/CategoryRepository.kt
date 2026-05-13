package com.example.hallisanthe.data.repository

import com.example.hallisanthe.data.Category
import com.example.hallisanthe.data.local.dao.CategoryDao
import com.example.hallisanthe.data.local.entity.CategoryEntity
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

class CategoryRepository(
    private val categoryDao: CategoryDao,
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) {

    private val categoriesCollection = firestore.collection("categories")

    fun getAllCategories(): Flow<List<Category>> = combine(
        categoryDao.getAllCategories().map { list -> list.map { it.toDomain() } },
        callbackFlow {
            trySend(emptyList())
            val listener = categoriesCollection.addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(emptyList())
                    return@addSnapshotListener
                }
                val categories = snapshot?.documents?.mapNotNull { it.toObject(Category::class.java) } ?: emptyList()
                trySend(categories)
            }
            awaitClose { listener.remove() }
        }
    ) { room, firestore ->
        (room + firestore).distinctBy { it.id }
    }

    suspend fun getCategoryById(id: String): Category? {
        return try {
            withTimeoutOrNull(3000L) {
                val doc = categoriesCollection.document(id).get().await()
                doc.toObject(Category::class.java)
            }
        } catch (e: Exception) {
            null
        } ?: categoryDao.getCategoryById(id)?.toDomain()
    }

    suspend fun insertCategory(category: Category) {
        categoryDao.insertCategory(category.toEntity())
        withTimeoutOrNull(3000L) {
            try {
                categoriesCollection.document(category.id).set(category).await()
            } catch (e: Exception) {
                // Local insert succeeded; Firestore sync is best-effort
            }
        }
    }

    suspend fun insertCategories(categories: List<Category>) {
        categoryDao.insertCategories(categories.map { it.toEntity() })
        withTimeoutOrNull(3000L) {
            try {
                val batch = firestore.batch()
                categories.forEach { category ->
                    batch.set(categoriesCollection.document(category.id), category)
                }
                batch.commit().await()
            } catch (e: Exception) {
                // Local insert succeeded; Firestore sync is best-effort
            }
        }
    }

    suspend fun deleteCategoryById(id: String) {
        categoryDao.deleteCategoryById(id)
        withTimeoutOrNull(3000L) {
            try {
                categoriesCollection.document(id).delete().await()
            } catch (e: Exception) {
                // Local delete succeeded; Firestore sync is best-effort
            }
        }
    }

    suspend fun seedCategories(categories: List<CategoryEntity>) {
        categoryDao.insertCategories(categories)
        withTimeoutOrNull(3000L) {
            try {
                val batch = firestore.batch()
                categories.forEach { entity ->
                    val category = entity.toDomain()
                    batch.set(categoriesCollection.document(entity.id), category)
                }
                batch.commit().await()
            } catch (e: Exception) {
                // Local seed succeeded; Firestore sync is best-effort
            }
        }
    }
}
