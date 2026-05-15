package com.example.hallisanthe.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class WishlistViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var viewModel: WishlistViewModel

    private val testProducts = listOf(
        Product("1", "Product 1", "Artisan 1", "Loc 1", 100, "", "cat1"),
        Product("2", "Product 2", "Artisan 2", "Loc 2", 200, "", "cat2"),
        Product("3", "Product 3", "Artisan 3", "Loc 3", 300, "", "cat3")
    )

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = WishlistViewModel()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `toggleWishlist should add product when not present`() {
        val product = testProducts[0]

        viewModel.toggleWishlist(product)

        assertEquals(1, viewModel.wishlistItems.size)
        assertEquals(product.id, viewModel.wishlistItems[0].id)
    }

    @Test
    fun `toggleWishlist should remove product when present`() {
        val product = testProducts[0]
        viewModel.toggleWishlist(product)

        viewModel.toggleWishlist(product)

        assertTrue(viewModel.wishlistItems.isEmpty())
    }

    @Test
    fun `toggleWishlist should not add duplicate product`() {
        val product = testProducts[0]
        viewModel.toggleWishlist(product)

        viewModel.toggleWishlist(product)
        viewModel.toggleWishlist(product)

        assertEquals(1, viewModel.wishlistItems.size)
    }

    @Test
    fun `isInWishlist should return true for added product`() {
        val product = testProducts[0]
        viewModel.toggleWishlist(product)

        val result = viewModel.isInWishlist(product.id)

        assertTrue(result)
    }

    @Test
    fun `isInWishlist should return false for non-added product`() {
        val result = viewModel.isInWishlist("non-existent-id")

        assertFalse(result)
    }

    @Test
    fun `removeItem should remove specific product`() {
        val product1 = testProducts[0]
        val product2 = testProducts[1]
        viewModel.toggleWishlist(product1)
        viewModel.toggleWishlist(product2)

        viewModel.removeItem(product1.id)

        assertEquals(1, viewModel.wishlistItems.size)
        assertEquals(product2.id, viewModel.wishlistItems[0].id)
    }

    @Test
    fun `wishlistItems size should track correctly`() {
        assertEquals(0, viewModel.wishlistItems.size)

        viewModel.toggleWishlist(testProducts[0])
        assertEquals(1, viewModel.wishlistItems.size)

        viewModel.toggleWishlist(testProducts[1])
        assertEquals(2, viewModel.wishlistItems.size)

        viewModel.removeItem(testProducts[0].id)
        assertEquals(1, viewModel.wishlistItems.size)
    }
}
