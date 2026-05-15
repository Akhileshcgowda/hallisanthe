package com.example.hallisanthe.data.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.hallisanthe.data.Category
import com.example.hallisanthe.data.Product
import com.example.hallisanthe.data.repository.CategoryRepository
import com.example.hallisanthe.data.repository.ProductRepository
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class ProductCatalogViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var viewModel: ProductCatalogViewModel
    private lateinit var productRepository: ProductRepository
    private lateinit var categoryRepository: CategoryRepository

    private val testProducts = listOf(
        Product("1", "Product 1", "Artisan 1", "Location 1", 100, "", "category1"),
        Product("2", "Product 2", "Artisan 2", "Location 2", 200, "", "category2"),
        Product("3", "Product 3", "Artisan 1", "Location 1", 300, "", "category1")
    )

    private val testCategories = listOf(
        Category("all", "All", "", "category"),
        Category("category1", "Category 1", "", "category"),
        Category("category2", "Category 2", "", "category")
    )

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        productRepository = mockk(relaxed = true)
        categoryRepository = mockk(relaxed = true)

        every { productRepository.getAllProducts() } returns flowOf(testProducts)
        every { productRepository.getFeaturedProducts() } returns flowOf(testProducts.take(2))
        every { categoryRepository.getAllCategories() } returns flowOf(testCategories)

        viewModel = ProductCatalogViewModel(productRepository, categoryRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `allProducts should emit products from repository`() = runTest {
        val products = viewModel.allProducts.first()
        
        assertEquals(3, products.size)
        assertEquals("Product 1", products[0].name)
    }

    @Test
    fun `featuredProducts should emit featured products`() = runTest {
        val featured = viewModel.featuredProducts.first()
        
        assertEquals(2, featured.size)
    }

    @Test
    fun `allCategories should emit categories from repository`() = runTest {
        val categories = viewModel.allCategories.first()
        
        assertEquals(3, categories.size)
    }

    @Test
    fun `setSearchQuery should update search query state`() {
        viewModel.setSearchQuery("test query")
        
        assertEquals("test query", viewModel.searchQuery.value)
    }

    @Test
    fun `setCategory should update selected category`() {
        viewModel.setCategory("category1")
        
        assertEquals("category1", viewModel.selectedCategory.value)
    }

    @Test
    fun `searchResults should filter products by name`() = runTest {
        viewModel.setSearchQuery("Product 1")
        
        val results = viewModel.searchResults.first()
        
        assertEquals(1, results.size)
        assertEquals("Product 1", results[0].name)
    }

    @Test
    fun `searchResults should filter products by description`() = runTest {
        val productsWithDesc = listOf(
            Product("1", "Product 1", "Artisan", "Loc", 100, "", "cat", description = "Amazing product"),
            Product("2", "Product 2", "Artisan", "Loc", 100, "", "cat", description = "Another item")
        )
        every { productRepository.getAllProducts() } returns flowOf(productsWithDesc)
        viewModel = ProductCatalogViewModel(productRepository, categoryRepository)
        
        viewModel.setSearchQuery("Amazing")
        val results = viewModel.searchResults.first()
        
        assertEquals(1, results.size)
        assertEquals("Product 1", results[0].name)
    }

    @Test
    fun `searchResults should return all products when query is blank`() = runTest {
        viewModel.setSearchQuery("")
        
        val results = viewModel.searchResults.first()
        
        assertEquals(3, results.size)
    }

    @Test
    fun `filteredProducts should show all when category is all`() = runTest {
        viewModel.setCategory("all")
        
        val products = viewModel.filteredProducts.first()
        
        assertEquals(3, products.size)
    }

    @Test
    fun `filteredProducts should filter by category`() = runTest {
        viewModel.setCategory("category1")
        
        val products = viewModel.filteredProducts.first()
        
        assertEquals(2, products.size)
        assertTrue(products.all { it.category == "category1" })
    }

    @Test
    fun `getProductById should return product with matching id`() = runTest {
        val product = viewModel.getProductById("2").first()
        
        assertNotNull(product)
        assertEquals("2", product?.id)
        assertEquals("Product 2", product?.name)
    }

    @Test
    fun `getProductById should return null for non-existent id`() = runTest {
        val product = viewModel.getProductById("999").first()
        
        assertNull(product)
    }
}
