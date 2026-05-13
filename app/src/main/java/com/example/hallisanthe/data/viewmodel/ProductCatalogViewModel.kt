package com.example.hallisanthe.data.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hallisanthe.data.Category
import com.example.hallisanthe.data.Product
import com.example.hallisanthe.data.repository.CategoryRepository
import com.example.hallisanthe.data.repository.ProductRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class ProductCatalogViewModel(
    private val productRepository: ProductRepository,
    private val categoryRepository: CategoryRepository
) : ViewModel() {

    val allProducts: Flow<List<Product>> = productRepository.getAllProducts()
    val featuredProducts: Flow<List<Product>> = productRepository.getFeaturedProducts()
    val allCategories: Flow<List<Category>> = categoryRepository.getAllCategories()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    val searchResults: Flow<List<Product>> = combine(
        _searchQuery,
        allProducts
    ) { query, products ->
        if (query.isBlank()) products
        else products.filter {
            it.name.contains(query, ignoreCase = true) ||
            it.description.contains(query, ignoreCase = true) ||
            it.artisan.contains(query, ignoreCase = true)
        }
    }

    private val _selectedCategory = MutableStateFlow("all")
    val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()

    val filteredProducts: Flow<List<Product>> = combine(
        _selectedCategory,
        allProducts
    ) { category, products ->
        if (category == "all") products
        else products.filter { it.category == category }
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setCategory(category: String) {
        _selectedCategory.value = category
    }

    fun refreshProducts() {
        viewModelScope.launch {
            // In the future, this will sync with remote/Firebase
        }
    }

    fun getProductById(id: String): Flow<Product?> {
        return allProducts.combine(MutableStateFlow(id)) { products, pid ->
            products.find { it.id == pid }
        }
    }
}
