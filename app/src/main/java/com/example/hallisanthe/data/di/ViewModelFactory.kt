package com.example.hallisanthe.data.di

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.hallisanthe.data.CartViewModel
import com.example.hallisanthe.data.WishlistViewModel
import com.example.hallisanthe.data.repository.ArtisanRepository
import com.example.hallisanthe.data.repository.AuthRepository
import com.example.hallisanthe.data.repository.CategoryRepository
import com.example.hallisanthe.data.repository.EnquiryRepository
import com.example.hallisanthe.data.repository.GenAIRepository
import com.example.hallisanthe.data.repository.OrderRepository
import com.example.hallisanthe.data.repository.ProductRepository
import com.example.hallisanthe.data.viewmodel.AuthViewModel
import com.example.hallisanthe.data.viewmodel.OrderViewModel
import com.example.hallisanthe.data.viewmodel.ProductCatalogViewModel
import com.example.hallisanthe.data.viewmodel.SantheAIViewModel
import com.example.hallisanthe.data.viewmodel.SellerDashboardViewModel

@Suppress("UNCHECKED_CAST")
class AppViewModelFactory(
    private val application: Application,
    private val productRepository: ProductRepository,
    private val artisanRepository: ArtisanRepository,
    private val categoryRepository: CategoryRepository,
    private val enquiryRepository: EnquiryRepository,
    private val orderRepository: OrderRepository,
    private val genAIRepository: GenAIRepository,
    private val authRepository: AuthRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when (modelClass) {
            ProductCatalogViewModel::class.java ->
                ProductCatalogViewModel(productRepository, categoryRepository) as T

            SellerDashboardViewModel::class.java ->
                SellerDashboardViewModel(enquiryRepository, productRepository, authRepository) as T

            CartViewModel::class.java ->
                CartViewModel() as T

            WishlistViewModel::class.java ->
                WishlistViewModel() as T

            AuthViewModel::class.java ->
                AuthViewModel(authRepository) as T

            OrderViewModel::class.java ->
                OrderViewModel(orderRepository, authRepository) as T

            SantheAIViewModel::class.java ->
                SantheAIViewModel(
                    application,
                    genAIRepository,
                    authRepository,
                    orderRepository,
                    productRepository,
                    enquiryRepository,
                    artisanRepository
                ) as T

            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}
