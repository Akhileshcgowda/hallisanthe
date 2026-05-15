package com.example.hallisanthe.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.hallisanthe.data.SampleData.products
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
class CartViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var viewModel: CartViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = CartViewModel()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `addItem should add new product to cart`() {
        val product = products[0]
        
        viewModel.addItem(product)
        
        assertEquals(1, viewModel.cartItems.size)
        assertEquals(product.id, viewModel.cartItems[0].product.id)
        assertEquals(1, viewModel.cartItems[0].quantity)
    }

    @Test
    fun `addItem should increase quantity for existing product`() {
        val product = products[0]
        viewModel.addItem(product)
        
        viewModel.addItem(product)
        
        assertEquals(1, viewModel.cartItems.size)
        assertEquals(2, viewModel.cartItems[0].quantity)
    }

    @Test
    fun `removeItem should remove product from cart`() {
        val product = products[0]
        viewModel.addItem(product)
        
        viewModel.removeItem(product.id)
        
        assertTrue(viewModel.cartItems.isEmpty())
    }

    @Test
    fun `increaseQty should increment product quantity`() {
        val product = products[0]
        viewModel.addItem(product)
        
        viewModel.increaseQty(product.id)
        
        assertEquals(2, viewModel.cartItems[0].quantity)
    }

    @Test
    fun `decreaseQty should decrement product quantity`() {
        val product = products[0]
        viewModel.addItem(product)
        viewModel.addItem(product)
        
        viewModel.decreaseQty(product.id)
        
        assertEquals(1, viewModel.cartItems[0].quantity)
    }

    @Test
    fun `decreaseQty should remove item when quantity reaches zero`() {
        val product = products[0]
        viewModel.addItem(product)
        
        viewModel.decreaseQty(product.id)
        
        assertTrue(viewModel.cartItems.isEmpty())
    }

    @Test
    fun `clearCart should remove all items`() {
        viewModel.addItem(products[0])
        viewModel.addItem(products[1])
        
        viewModel.clearCart()
        
        assertTrue(viewModel.cartItems.isEmpty())
    }

    @Test
    fun `itemCount should return total quantity of all items`() {
        viewModel.addItem(products[0])
        viewModel.addItem(products[0])
        viewModel.addItem(products[1])
        
        assertEquals(3, viewModel.itemCount)
    }

    @Test
    fun `subtotal should calculate total price correctly`() {
        val product1 = products[0]
        val product2 = products[1]
        viewModel.addItem(product1)
        viewModel.addItem(product1)
        viewModel.addItem(product2)
        
        val expectedSubtotal = (product1.price * 2) + product2.price
        assertEquals(expectedSubtotal, viewModel.subtotal)
    }

    @Test
    fun `cart should handle multiple different products`() {
        viewModel.addItem(products[0])
        viewModel.addItem(products[1])
        viewModel.addItem(products[2])
        
        assertEquals(3, viewModel.cartItems.size)
        assertEquals(3, viewModel.itemCount)
    }
}
