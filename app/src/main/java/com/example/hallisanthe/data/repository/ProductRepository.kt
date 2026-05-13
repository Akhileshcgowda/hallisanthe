package com.example.hallisanthe.data.repository

import com.example.hallisanthe.data.Product
import com.example.hallisanthe.data.local.dao.ProductDao
import com.example.hallisanthe.data.local.entity.ProductEntity
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

class ProductRepository(
    private val productDao: ProductDao,
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) {

    private val productsCollection = firestore.collection("products")

    private fun firestoreAllProducts(): Flow<List<Product>> = callbackFlow {
        trySend(emptyList())
        val listener = productsCollection.addSnapshotListener { snapshot, error ->
            if (error != null) {
                trySend(emptyList())
                return@addSnapshotListener
            }
            val products = snapshot?.documents?.mapNotNull { it.toObject(Product::class.java) } ?: emptyList()
            trySend(products)
        }
        awaitClose { listener.remove() }
    }

    fun getAllProducts(): Flow<List<Product>> = combine(
        productDao.getAllProducts().map { list -> list.map { it.toDomain() } },
        firestoreAllProducts()
    ) { room, firestore ->
        (room + firestore).distinctBy { it.id }
    }

    fun getProductsByCategory(category: String): Flow<List<Product>> = combine(
        productDao.getProductsByCategory(category).map { list -> list.map { it.toDomain() } },
        callbackFlow {
            trySend(emptyList())
            val query = productsCollection.whereEqualTo("category", category)
            val listener = query.addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(emptyList())
                    return@addSnapshotListener
                }
                val products = snapshot?.documents?.mapNotNull { it.toObject(Product::class.java) } ?: emptyList()
                trySend(products)
            }
            awaitClose { listener.remove() }
        }
    ) { room, firestore ->
        (room + firestore).distinctBy { it.id }
    }

    fun getProductsByArtisan(artisanId: String): Flow<List<Product>> {
        return productDao.getProductsByArtisan(artisanId).map { list -> list.map { it.toDomain() } }
    }

    fun searchProducts(query: String): Flow<List<Product>> {
        return productDao.searchProducts(query).map { list -> list.map { it.toDomain() } }
    }

    fun getFeaturedProducts(): Flow<List<Product>> = combine(
        productDao.getFeaturedProducts().map { list -> list.map { it.toDomain() } },
        callbackFlow {
            trySend(emptyList())
            val query = productsCollection.whereEqualTo("isFeatured", true)
            val listener = query.addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(emptyList())
                    return@addSnapshotListener
                }
                val products = snapshot?.documents?.mapNotNull { it.toObject(Product::class.java) } ?: emptyList()
                trySend(products)
            }
            awaitClose { listener.remove() }
        }
    ) { room, firestore ->
        (room + firestore).distinctBy { it.id }
    }

    suspend fun getProductById(id: String): Product? {
        return try {
            withTimeoutOrNull(3000L) {
                val doc = productsCollection.document(id).get().await()
                doc.toObject(Product::class.java)
            }
        } catch (e: Exception) {
            null
        } ?: productDao.getProductById(id)?.toDomain()
    }

    suspend fun insertProduct(product: Product, artisanId: String) {
        productDao.insertProduct(product.toEntity(artisanId))
        withTimeoutOrNull(3000L) {
            try {
                productsCollection.document(product.id).set(product).await()
            } catch (e: Exception) {
                // Local insert succeeded; Firestore sync is best-effort
            }
        }
    }

    suspend fun insertProducts(products: List<Pair<Product, String>>) {
        productDao.insertProducts(products.map { it.first.toEntity(it.second) })
        withTimeoutOrNull(3000L) {
            try {
                val batch = firestore.batch()
                products.forEach { (product, _) ->
                    batch.set(productsCollection.document(product.id), product)
                }
                batch.commit().await()
            } catch (e: Exception) {
                // Local insert succeeded; Firestore sync is best-effort
            }
        }
    }

    suspend fun updateProduct(product: Product, artisanId: String) {
        productDao.updateProduct(product.toEntity(artisanId))
        withTimeoutOrNull(3000L) {
            try {
                productsCollection.document(product.id).set(product).await()
            } catch (e: Exception) {
                // Local update succeeded; Firestore sync is best-effort
            }
        }
    }

    suspend fun deleteProduct(product: Product, artisanId: String) {
        productDao.deleteProduct(product.toEntity(artisanId))
        withTimeoutOrNull(3000L) {
            try {
                productsCollection.document(product.id).delete().await()
            } catch (e: Exception) {
                // Local delete succeeded; Firestore sync is best-effort
            }
        }
    }

    suspend fun deleteProductById(id: String) {
        productDao.deleteProductById(id)
        withTimeoutOrNull(3000L) {
            try {
                productsCollection.document(id).delete().await()
            } catch (e: Exception) {
                // Local delete succeeded; Firestore sync is best-effort
            }
        }
    }

    suspend fun getProductCount(): Int {
        return productDao.getProductCount()
    }

    suspend fun seedProducts(products: List<ProductEntity>) {
        productDao.insertProducts(products)
        withTimeoutOrNull(3000L) {
            try {
                val batch = firestore.batch()
                products.forEach { entity ->
                    val product = entity.toDomain()
                    batch.set(productsCollection.document(entity.id), product)
                }
                batch.commit().await()
            } catch (e: Exception) {
                // Local seed succeeded; Firestore sync is best-effort
            }
        }
    }
}
