package com.example.hallisanthe.data

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel

class WishlistViewModel : ViewModel() {
    val wishlistItems = mutableStateListOf<Product>()

    fun toggleWishlist(product: Product) {
        if (wishlistItems.any { it.id == product.id }) {
            wishlistItems.removeAll { it.id == product.id }
        } else {
            wishlistItems.add(product)
        }
    }

    fun isInWishlist(productId: String): Boolean {
        return wishlistItems.any { it.id == productId }
    }

    fun removeItem(productId: String) {
        wishlistItems.removeAll { it.id == productId }
    }
}
