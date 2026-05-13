package com.example.hallisanthe.data.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hallisanthe.data.Inquiry
import com.example.hallisanthe.data.Notification
import com.example.hallisanthe.data.SellerDashboardMetrics
import com.example.hallisanthe.data.local.entity.EnquiryEntity
import com.example.hallisanthe.data.repository.AuthRepository
import com.example.hallisanthe.data.repository.EnquiryRepository
import com.example.hallisanthe.data.repository.ProductRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.util.UUID

@OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
class SellerDashboardViewModel(
    private val enquiryRepository: EnquiryRepository,
    private val productRepository: ProductRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _metrics = MutableStateFlow(
        SellerDashboardMetrics(
            totalSales = "₹0",
            activeListings = 0,
            pendingInquiries = 0,
            profileViews = 0,
            salesTrend = 0.0
        )
    )
    val metrics: StateFlow<SellerDashboardMetrics> = _metrics.asStateFlow()

    // React to auth changes so metrics/flows update when user logs in/out
    private val artisanIdFlow: Flow<String> = authRepository.currentUser.map { it?.uid ?: "" }

    val inquiries: Flow<List<Inquiry>> = artisanIdFlow.flatMapLatest { id ->
        if (id.isNotBlank()) enquiryRepository.getEnquiriesForArtisan(id) else flowOf(emptyList())
    }

    val notifications: Flow<List<Notification>> = artisanIdFlow.flatMapLatest { id ->
        if (id.isNotBlank()) enquiryRepository.getNotificationsForArtisan(id) else flowOf(emptyList())
    }

    init {
        viewModelScope.launch {
            artisanIdFlow.flatMapLatest { id ->
                if (id.isBlank()) {
                    flowOf(
                        SellerDashboardMetrics(
                            totalSales = "₹0",
                            activeListings = 0,
                            pendingInquiries = 0,
                            profileViews = 0,
                            salesTrend = 0.0
                        )
                    )
                } else {
                    combine(
                        productRepository.getProductsByArtisan(id),
                        enquiryRepository.getEnquiriesForArtisan(id)
                    ) { products, enquiries ->
                        SellerDashboardMetrics(
                            totalSales = "₹0",
                            activeListings = products.size,
                            pendingInquiries = enquiries.count { it.isUnread },
                            profileViews = 0,
                            salesTrend = 0.0
                        )
                    }
                }
            }.collect { _metrics.value = it }
        }
    }

    fun markInquiryAsRead(id: String) {
        viewModelScope.launch {
            enquiryRepository.markAsRead(id)
        }
    }

    fun dismissNotification(id: String) {
        val enquiryId = id.removePrefix("n_")
        viewModelScope.launch {
            enquiryRepository.deleteEnquiryById(enquiryId)
        }
    }

    fun clearAllNotifications() {
        viewModelScope.launch {
            // In a real app, this would clear all notifications for the artisan
        }
    }

    fun markNotificationAsRead(id: String) {
        val enquiryId = id.removePrefix("n_")
        viewModelScope.launch {
            enquiryRepository.markAsRead(enquiryId)
        }
    }

    fun sendEnquiry(productId: String, productName: String, buyerName: String, message: String) {
        viewModelScope.launch {
            val artisanId = authRepository.userId ?: "a1"
            val enquiry = EnquiryEntity(
                id = UUID.randomUUID().toString(),
                buyerId = "b_${UUID.randomUUID().toString().take(4)}",
                artisanId = artisanId,
                productId = productId,
                buyerName = buyerName,
                productName = productName,
                message = message,
                isUnread = true
            )
            enquiryRepository.insertEnquiry(enquiry)
        }
    }
}
