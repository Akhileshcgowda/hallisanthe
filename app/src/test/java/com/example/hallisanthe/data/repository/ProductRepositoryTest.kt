package com.example.hallisanthe.data.repository

import com.example.hallisanthe.data.Product
import com.example.hallisanthe.data.local.dao.ProductDao
import com.example.hallisanthe.data.local.entity.ProductEntity
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class ProductRepositoryTest {

    private lateinit var repository: ProductRepository
    private lateinit var productDao: ProductDao

    private val testEntities = listOf(
        ProductEntity("1", "Product 1", "Artisan 1", "Loc 1", 100, "", "cat1", artisanId = "a1"),
        ProductEntity("2", "Product 2", "Artisan 2", "Loc 2", 200, "", "cat2", artisanId = "a2"),
        ProductEntity("3", "Product 3", "Artisan 1", "Loc 1", 300, "", "cat1", artisanId = "a1")
    )

    @Before
    fun setup() {
        productDao = mockk(relaxed = true)
        repository = ProductRepository(productDao)
    }

    @Test
    fun `getProductsByArtisan should return products for specific artisan`() = runTest {
        val artisanProducts = testEntities.filter { it.artisanId == "a1" }
        coEvery { productDao.getProductsByArtisan("a1") } returns flowOf(artisanProducts)

        val result = repository.getProductsByArtisan("a1").first()

        assertEquals(2, result.size)
        assertTrue(result.all { it.artisan == "Artisan 1" })
    }

    @Test
    fun `searchProducts should return matching products`() = runTest {
        coEvery { productDao.searchProducts("Product") } returns flowOf(testEntities)

        val result = repository.searchProducts("Product").first()

        assertEquals(3, result.size)
    }

    @Test
    fun `getProductById should return product from local database`() = runTest {
        val entity = testEntities[0]
        coEvery { productDao.getProductById("1") } returns entity

        val result = repository.getProductById("1")

        assertNotNull(result)
        assertEquals("1", result?.id)
        assertEquals("Product 1", result?.name)
    }

    @Test
    fun `getProductById should return null when not found`() = runTest {
        coEvery { productDao.getProductById("999") } returns null

        val result = repository.getProductById("999")

        assertNull(result)
    }

    @Test
    fun `insertProduct should call dao insert`() = runTest {
        val product = Product("4", "New Product", "Artisan", "Loc", 150, "", "cat", artisanId = "a1")

        repository.insertProduct(product, "a1")

        coVerify { productDao.insertProduct(any()) }
    }

    @Test
    fun `updateProduct should call dao update`() = runTest {
        val product = Product("1", "Updated Product", "Artisan", "Loc", 150, "", "cat", artisanId = "a1")

        repository.updateProduct(product, "a1")

        coVerify { productDao.updateProduct(any()) }
    }

    @Test
    fun `deleteProduct should call dao delete`() = runTest {
        val product = Product("1", "Product", "Artisan", "Loc", 100, "", "cat", artisanId = "a1")

        repository.deleteProduct(product, "a1")

        coVerify { productDao.deleteProduct(any()) }
    }

    @Test
    fun `deleteProductById should call dao delete by id`() = runTest {
        repository.deleteProductById("1")

        coVerify { productDao.deleteProductById("1") }
    }

    @Test
    fun `getProductCount should return count from dao`() = runTest {
        coEvery { productDao.getProductCount() } returns 10

        val count = repository.getProductCount()

        assertEquals(10, count)
    }

    @Test
    fun `seedProducts should insert all products`() = runTest {
        repository.seedProducts(testEntities)

        coVerify { productDao.insertProducts(testEntities) }
    }

    @Test
    fun `insertProducts should handle batch insert`() = runTest {
        val products = listOf(
            Product("1", "P1", "A1", "L1", 100, "", "cat") to "a1",
            Product("2", "P2", "A2", "L2", 200, "", "cat") to "a2"
        )

        repository.insertProducts(products)

        coVerify { productDao.insertProducts(any()) }
    }
}
