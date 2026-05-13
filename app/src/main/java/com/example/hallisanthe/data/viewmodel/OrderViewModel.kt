package com.example.hallisanthe.data.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hallisanthe.data.local.entity.OrderEntity
import com.example.hallisanthe.data.local.entity.OrderItemSnapshot
import com.example.hallisanthe.data.repository.AuthRepository
import com.example.hallisanthe.data.repository.OrderRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class OrderViewModel(
    private val orderRepository: OrderRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _placeOrderResult = MutableStateFlow<Result<String>?>(null)
    val placeOrderResult: StateFlow<Result<String>?> = _placeOrderResult.asStateFlow()

    private val _isPlacingOrder = MutableStateFlow(false)
    val isPlacingOrder: StateFlow<Boolean> = _isPlacingOrder.asStateFlow()

    val orders: Flow<List<OrderEntity>> = authRepository.currentUser.map { it?.uid ?: "" }
        .flatMapLatest { userId ->
            if (userId.isBlank()) flowOf(emptyList())
            else orderRepository.getOrdersForUser(userId)
        }

    fun placeOrder(
        items: List<OrderItemSnapshot>,
        total: Int,
        paymentMethod: String,
        shippingName: String,
        shippingAddress: String,
        shippingCity: String
    ) {
        viewModelScope.launch {
            _isPlacingOrder.value = true
            val userId = authRepository.userId
            if (userId == null) {
                _placeOrderResult.value = Result.failure(Exception("User not logged in"))
                _isPlacingOrder.value = false
                return@launch
            }
            val result = orderRepository.placeOrder(
                userId = userId,
                items = items,
                total = total,
                paymentMethod = paymentMethod,
                shippingName = shippingName,
                shippingAddress = shippingAddress,
                shippingCity = shippingCity
            )
            _placeOrderResult.value = result
            _isPlacingOrder.value = false
        }
    }

    fun clearPlaceOrderResult() {
        _placeOrderResult.value = null
    }
}
