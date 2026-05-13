package com.example.hallisanthe.data.repository

import com.example.hallisanthe.data.Order
import com.example.hallisanthe.data.local.dao.OrderDao
import com.example.hallisanthe.data.local.entity.OrderEntity
import com.example.hallisanthe.data.local.entity.OrderItemSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withTimeoutOrNull
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

@OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
class OrderRepository(
    private val orderDao: OrderDao,
    private val firestore: FirebaseFirestore
) {

    fun getOrdersForUser(userId: String): Flow<List<OrderEntity>> {
        val roomFlow = orderDao.getOrdersForUser(userId)
        val firestoreFlow = callbackFlow {
            val listener = firestore.collection("orders")
                .whereEqualTo("userId", userId)
                .orderBy("timestamp")
                .addSnapshotListener { snapshot, error ->
                    if (error != null) {
                        trySend(emptyList())
                        return@addSnapshotListener
                    }
                    val orders = snapshot?.documents?.mapNotNull { doc ->
                        try {
                            val id = doc.getString("id") ?: doc.id
                            val itemsJson = doc.getString("itemsJson") ?: "[]"
                            val total = doc.getLong("total")?.toInt() ?: 0
                            val status = doc.getString("status") ?: "Pending"
                            val timestamp = doc.getLong("timestamp") ?: System.currentTimeMillis()
                            val paymentMethod = doc.getString("paymentMethod") ?: ""
                            val shippingName = doc.getString("shippingName") ?: ""
                            val shippingAddress = doc.getString("shippingAddress") ?: ""
                            val shippingCity = doc.getString("shippingCity") ?: ""
                            OrderEntity(
                                id = id,
                                userId = userId,
                                itemsJson = itemsJson,
                                total = total,
                                status = status,
                                timestamp = timestamp,
                                paymentMethod = paymentMethod,
                                shippingName = shippingName,
                                shippingAddress = shippingAddress,
                                shippingCity = shippingCity
                            )
                        } catch (_: Exception) { null }
                    } ?: emptyList()
                    trySend(orders)
                }
            awaitClose { listener.remove() }
        }

        return roomFlow.flatMapLatest { roomOrders ->
            firestoreFlow.map { firestoreOrders ->
                val merged = (roomOrders + firestoreOrders)
                    .associateBy { it.id }
                    .values
                    .sortedByDescending { it.timestamp }
                    .toList()
                merged
            }
        }
    }

    suspend fun placeOrder(
        userId: String,
        items: List<OrderItemSnapshot>,
        total: Int,
        paymentMethod: String,
        shippingName: String,
        shippingAddress: String,
        shippingCity: String
    ): Result<String> {
        val orderId = UUID.randomUUID().toString()
        val gson = com.google.gson.Gson()
        val itemsJson = gson.toJson(items)
        val order = OrderEntity(
            id = orderId,
            userId = userId,
            itemsJson = itemsJson,
            total = total,
            status = "Pending",
            timestamp = System.currentTimeMillis(),
            paymentMethod = paymentMethod,
            shippingName = shippingName,
            shippingAddress = shippingAddress,
            shippingCity = shippingCity
        )

        return try {
            // Local-first: write to Room immediately
            orderDao.insertOrder(order)

            // Best-effort Firestore sync with timeout
            // Silently ignore Firestore failures (offline, permission denied, etc.)
            try {
                withTimeoutOrNull(3000L) {
                    firestore.collection("orders").document(orderId).set(
                        mapOf(
                            "id" to orderId,
                            "userId" to userId,
                            "itemsJson" to itemsJson,
                            "total" to total,
                            "status" to "Pending",
                            "timestamp" to order.timestamp,
                            "paymentMethod" to paymentMethod,
                            "shippingName" to shippingName,
                            "shippingAddress" to shippingAddress,
                            "shippingCity" to shippingCity
                        )
                    ).await()
                }
            } catch (_: Exception) {
                // Firestore sync failed; order is still saved locally
            }
            Result.success(orderId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
