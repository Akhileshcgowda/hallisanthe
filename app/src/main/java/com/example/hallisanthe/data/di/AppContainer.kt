package com.example.hallisanthe.data.di

import android.content.Context
import com.example.hallisanthe.data.local.database.HalliSantheDatabase
import com.example.hallisanthe.data.remote.api.GeminiApiService
import com.example.hallisanthe.data.repository.ArtisanRepository
import com.example.hallisanthe.data.repository.AuthRepository
import com.example.hallisanthe.data.repository.CategoryRepository
import com.example.hallisanthe.data.repository.EnquiryRepository
import com.example.hallisanthe.data.repository.GenAIRepository
import com.example.hallisanthe.data.repository.OrderRepository
import com.example.hallisanthe.data.repository.ProductRepository
import com.google.firebase.firestore.FirebaseFirestore

class AppContainer(private val context: Context) {

    private val database by lazy {
        HalliSantheDatabase.getDatabase(context)
    }

    private val firestore: FirebaseFirestore by lazy {
        FirebaseFirestore.getInstance()
    }

    val productRepository: ProductRepository by lazy {
        ProductRepository(database.productDao(), firestore)
    }

    val artisanRepository: ArtisanRepository by lazy {
        ArtisanRepository(database.artisanDao(), firestore)
    }

    val categoryRepository: CategoryRepository by lazy {
        CategoryRepository(database.categoryDao(), firestore)
    }

    val enquiryRepository: EnquiryRepository by lazy {
        EnquiryRepository(database.enquiryDao(), firestore)
    }

    val orderRepository: OrderRepository by lazy {
        OrderRepository(database.orderDao(), firestore)
    }

    val genAIRepository: GenAIRepository by lazy {
        GenAIRepository(GeminiApiService.create())
    }

    val authRepository: AuthRepository by lazy {
        AuthRepository()
    }
}
