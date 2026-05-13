package com.example.hallisanthe.data

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel

class CartViewModel : ViewModel() {
    val cartItems = mutableStateListOf<CartItem>()

    val itemCount: Int get() = cartItems.sumOf { it.quantity }

    val subtotal: Int get() = cartItems.sumOf { it.product.price * it.quantity }

    val total: Int get() = subtotal

    fun addItem(product: com.example.hallisanthe.data.Product) {
        val existing = cartItems.firstOrNull { it.product.id == product.id }
        if (existing != null) {
            val index = cartItems.indexOf(existing)
            cartItems[index] = existing.copy(quantity = existing.quantity + 1)
        } else {
            cartItems.add(CartItem(product, quantity = 1))
        }
    }

    fun removeItem(productId: String) {
        cartItems.removeAll { it.product.id == productId }
    }

    fun increaseQty(productId: String) {
        val index = cartItems.indexOfFirst { it.product.id == productId }
        if (index >= 0) {
            val item = cartItems[index]
            cartItems[index] = item.copy(quantity = item.quantity + 1)
        }
    }

    fun decreaseQty(productId: String) {
        val index = cartItems.indexOfFirst { it.product.id == productId }
        if (index >= 0) {
            val item = cartItems[index]
            if (item.quantity > 1) {
                cartItems[index] = item.copy(quantity = item.quantity - 1)
            } else {
                cartItems.removeAt(index)
            }
        }
    }

    fun clearCart() {
        cartItems.clear()
    }
}
