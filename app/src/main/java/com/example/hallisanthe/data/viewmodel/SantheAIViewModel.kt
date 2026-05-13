package com.example.hallisanthe.data.viewmodel

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.hallisanthe.data.CartItem
import com.example.hallisanthe.data.ChatMessage
import com.example.hallisanthe.data.Product
import com.example.hallisanthe.data.UserContext
import com.example.hallisanthe.data.repository.ArtisanRepository
import com.example.hallisanthe.data.LanguageManager
import com.example.hallisanthe.data.repository.AuthRepository
import com.example.hallisanthe.data.repository.EnquiryRepository
import com.example.hallisanthe.data.repository.GenAIRepository
import com.example.hallisanthe.data.repository.OrderRepository
import com.example.hallisanthe.data.repository.ProductRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
class SantheAIViewModel(
    application: Application,
    private val genAIRepository: GenAIRepository,
    private val authRepository: AuthRepository,
    private val orderRepository: OrderRepository,
    private val productRepository: ProductRepository,
    private val enquiryRepository: EnquiryRepository,
    private val artisanRepository: ArtisanRepository
) : AndroidViewModel(application) {

    // Separate chat histories for buyer and seller modes
    private val _buyerMessages = MutableStateFlow<List<ChatMessage>>(emptyList())
    private val _sellerMessages = MutableStateFlow<List<ChatMessage>>(emptyList())

    // Current mode: false = buyer, true = seller
    private val _currentIsSeller = MutableStateFlow(false)
    val currentIsSeller: StateFlow<Boolean> = _currentIsSeller.asStateFlow()

    // Expose the active chat based on current mode
    val messages: StateFlow<List<ChatMessage>> = _currentIsSeller.flatMapLatest { isSeller ->
        if (isSeller) _sellerMessages else _buyerMessages
    }.stateIn(viewModelScope, kotlinx.coroutines.flow.SharingStarted.Eagerly, emptyList())

    private val _isTyping = MutableStateFlow(false)
    val isTyping: StateFlow<Boolean> = _isTyping.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _isUsingFallback = MutableStateFlow(false)
    val isUsingFallback: StateFlow<Boolean> = _isUsingFallback.asStateFlow()

    // In-memory cart/wishlist snapshots updated from UI
    private var currentCartItems: List<CartItem> = emptyList()
    private var currentWishlistItems: List<Product> = emptyList()

    init {
        val lang = try {
            kotlinx.coroutines.runBlocking {
                LanguageManager.getLanguageFlow(getApplication()).first()
            }
        } catch (_: Exception) { "en" }
        _buyerMessages.value = listOf(
            ChatMessage(text = buyerGreeting(lang), isUser = false)
        )
        _sellerMessages.value = listOf(
            ChatMessage(text = sellerGreeting(lang), isUser = false)
        )
    }

    fun switchMode(isSeller: Boolean) {
        if (_currentIsSeller.value != isSeller) {
            _currentIsSeller.value = isSeller
        }
    }

    private fun buyerGreeting(lang: String): String = when (lang) {
        "kn" -> "ನಮಸ್ಕಾರ! ನಾನು ಸಂತೆ AI, ನಿಮ್ಮ ಖರೀದಿದಾರ ಸಹಾಯಕ. ಕುಶಲ ಕಲೆಯ ಉತ್ಪನ್ನಗಳನ್ನು ಹುಡುಕಲು, ಆರ್ಡರ್‌ಗಳನ್ನು ಟ್ರ್ಯಾಕ್ ಮಾಡಲು, ಮತ್ತು ಮಾರುಕಟ್ಟೆಯನ್ನು ನ್ಯಾವಿಗೇಟ್ ಮಾಡಲು ನಾನು ನಿಮಗೆ ಸಹಾಯ ಮಾಡಬಹುದು. ನಾನು ಇಂದು ನಿಮಗೆ ಹೇಗೆ ಸಹಾಯ ಮಾಡಬಹುದು?"
        "hi" -> "नमस्ते! मैं संतए AI हूँ, आपका खरीदार सहायक। मैं आपको हस्तकला उत्पाद खोजने, ऑर्डर ट्रैक करने, और बाज़ार में मार्गदर्शन करने में मदद कर सकता हूँ। मैं आज आपकी कैसे सहायता कर सकता हूँ?"
        else -> "Namaskara! I am Santhe AI, your buyer assistant. I can help you find artisan products, track orders, explain crafts, and navigate the marketplace. How can I assist you today?"
    }

    private fun sellerGreeting(lang: String): String = when (lang) {
        "kn" -> "ನಮಸ್ಕಾರ! ನಾನು ಸಂತೆ AI, ನಿಮ್ಮ ವ್ಯಾಪಾರಿ ಸಹಾಯಕ. ನಿಮ್ಮ ಪಟ್ಟಿಯನ್ನು ನಿರ್ವಹಿಸಲು, ಖರೀದಿದಾರರ ಪ್ರಶ್ನೆಗಳಿಗೆ ಉತ್ತರಿಸಲು, ಬೆಲೆ ನಿರ್ಧರಿಸಲು, ಮತ್ತು ನಿಮ್ಮ ಅಂಗಡಿಯನ್ನು ಬೆಳೆಸಲು ನಾನು ನಿಮಗೆ ಸಹಾಯ ಮಾಡಬಹುದು. ನಾನು ಇಂದು ನಿಮಗೆ ಹೇಗೆ ಸಹಾಯ ಮಾಡಬಹುದು?"
        "hi" -> "नमस्ते! मैं संतए AI हूँ, आपका विक्रेता सहायक। मैं आपको अपनी लिस्टिंग प्रबंधित करने, खरीदारों के प्रश्नों का उत्तर देने, मूल्य निर्धारण करने, और अपनी दुकान बढ़ाने में मदद कर सकता हूँ। मैं आज आपकी कैसे सहायता कर सकता हूँ?"
        else -> "Namaskara! I am Santhe AI, your seller assistant. I can help you manage your listings, respond to buyer inquiries, set pricing, and grow your shop. How can I assist you today?"
    }

    fun updateCartAndWishlist(cart: List<CartItem>, wishlist: List<Product>) {
        currentCartItems = cart
        currentWishlistItems = wishlist
    }

    fun sendMessage(userText: String) {
        if (userText.isBlank()) return

        val userMessage = ChatMessage(text = userText.trim(), isUser = true)
        if (_currentIsSeller.value) {
            _sellerMessages.value = _sellerMessages.value + userMessage
        } else {
            _buyerMessages.value = _buyerMessages.value + userMessage
        }
        _error.value = null
        _isTyping.value = true
        _isUsingFallback.value = false

        viewModelScope.launch {
            val userContext = buildUserContext()

            val activeMessages = if (_currentIsSeller.value) _sellerMessages.value else _buyerMessages.value
            val conversationHistory = activeMessages
                .drop(1) // skip hardcoded greeting
                .filter { it.text.isNotBlank() }
                .map { msg ->
                    val role = if (msg.isUser) "user" else "model"
                    role to msg.text
                }

            val result = genAIRepository.chat(conversationHistory, userContext)

            _isTyping.value = false
            result.onSuccess { reply ->
                val isFallback = genAIRepository.isFallbackReply(reply)
                _isUsingFallback.value = isFallback
                if (isFallback) {
                    Toast.makeText(
                        getApplication(),
                        "Using offline mode. Add GEMINI_API_KEY to local.properties for AI replies.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                if (_currentIsSeller.value) {
                    _sellerMessages.value = _sellerMessages.value + ChatMessage(text = reply, isUser = false)
                } else {
                    _buyerMessages.value = _buyerMessages.value + ChatMessage(text = reply, isUser = false)
                }
            }.onFailure { exception ->
                _error.value = exception.message
                val errorMsg = ChatMessage(
                    text = "I'm having trouble connecting right now. Please check your internet or try again shortly.",
                    isUser = false
                )
                if (_currentIsSeller.value) {
                    _sellerMessages.value = _sellerMessages.value + errorMsg
                } else {
                    _buyerMessages.value = _buyerMessages.value + errorMsg
                }
            }
        }
    }

    private suspend fun buildUserContext(): UserContext {
        val user = authRepository.currentUser.value
        val userId = user?.uid ?: authRepository.userId
        val userName = user?.displayName ?: authRepository.userName ?: ""
        val userEmail = user?.email ?: authRepository.userEmail ?: ""
        val userPhone = user?.phoneNumber ?: ""

        // Use the screen mode as the authoritative role flag
        val isSellerMode = _currentIsSeller.value
        android.util.Log.d("SantheAI", "buildUserContext: isSellerMode=$isSellerMode, userId=$userId, email=$userEmail")

        // Cart summary
        val cartSummary = if (currentCartItems.isNotEmpty()) {
            currentCartItems.joinToString(", ") { "${it.product.name} x${it.quantity} (₹${it.product.price * it.quantity})" } +
                    " | Total: ₹${currentCartItems.sumOf { it.product.price * it.quantity }}"
        } else ""

        // Wishlist summary
        val wishlistSummary = if (currentWishlistItems.isNotEmpty()) {
            currentWishlistItems.joinToString(", ") { it.name }
        } else ""

        // Orders
        var recentOrders = ""
        if (!userId.isNullOrBlank()) {
            try {
                val orders = orderRepository.getOrdersForUser(userId).first()
                recentOrders = orders.take(3).joinToString(" | ") { order ->
                    val items = try {
                        com.google.gson.Gson().fromJson(order.itemsJson, Array<com.example.hallisanthe.data.local.entity.OrderItemSnapshot>::class.java)
                            ?.joinToString(", ") { "${it.name} x${it.quantity}" } ?: ""
                    } catch (_: Exception) { "" }
                    "Order ${order.id.takeLast(6)}: ₹${order.total}, Status: ${order.status}, Items: $items"
                }
            } catch (e: Exception) {
                android.util.Log.w("SantheAI", "Order fetch failed: ${e.message}")
            }
        }

        // Seller data — try ID first, then email fallback, then use found artisan's real ID for products/enquiries
        var artisanProfile = ""
        var sellerListings = ""
        var sellerInquiries = ""
        var resolvedArtisanId: String? = null

        if (!userId.isNullOrBlank()) {
            // Try 1: Look up artisan by Firebase UID (for real registered sellers)
            try {
                val artisan = artisanRepository.getArtisanById(userId)
                if (artisan != null) {
                    resolvedArtisanId = artisan.id
                    artisanProfile = "Name: ${artisan.name}, Craft: ${artisan.craft}, Village: ${artisan.location}, Phone: ${artisan.phone}, Email: ${artisan.email}, Bio: ${artisan.bio}"
                    android.util.Log.d("SantheAI", "Found artisan by ID: ${artisan.name}, id=${artisan.id}")
                }
            } catch (e: Exception) {
                android.util.Log.w("SantheAI", "getArtisanById failed: ${e.message}")
            }

            // Try 2: Fallback to email lookup (seeded/demo data uses fixed IDs, not Firebase UIDs)
            if (resolvedArtisanId == null && userEmail.isNotBlank()) {
                try {
                    val artisanByEmail = artisanRepository.getArtisanByEmail(userEmail)
                    if (artisanByEmail != null) {
                        resolvedArtisanId = artisanByEmail.id
                        artisanProfile = "Name: ${artisanByEmail.name}, Craft: ${artisanByEmail.craft}, Village: ${artisanByEmail.location}, Phone: ${artisanByEmail.phone}, Email: ${artisanByEmail.email}, Bio: ${artisanByEmail.bio}"
                        android.util.Log.d("SantheAI", "Found artisan by EMAIL fallback: ${artisanByEmail.name}, id=${artisanByEmail.id}")
                    } else {
                        android.util.Log.d("SantheAI", "No artisan found by ID or email")
                    }
                } catch (e: Exception) {
                    android.util.Log.w("SantheAI", "getArtisanByEmail failed: ${e.message}")
                }
            }

            // Fetch seller listings using resolved artisan ID (NOT the Firebase UID)
            if (resolvedArtisanId != null) {
                try {
                    val products = productRepository.getProductsByArtisan(resolvedArtisanId).first()
                    sellerListings = products.take(5).joinToString(", ") { "${it.name} (₹${it.price}, ${it.category})" }
                    if (products.size > 5) sellerListings += " and ${products.size - 5} more"
                    android.util.Log.d("SantheAI", "Found ${products.size} products for artisan $resolvedArtisanId")
                } catch (e: Exception) {
                    android.util.Log.w("SantheAI", "Product fetch failed: ${e.message}")
                }

                try {
                    val enquiries = enquiryRepository.getEnquiriesForArtisan(resolvedArtisanId).first()
                    sellerInquiries = enquiries.take(5).joinToString(", ") {
                        "From ${it.customerName} about ${it.productName}: ${it.lastMessage}"
                    }
                    if (enquiries.isNotEmpty()) sellerInquiries += " | ${enquiries.count { it.isUnread }} unread"
                    android.util.Log.d("SantheAI", "Found ${enquiries.size} enquiries for artisan $resolvedArtisanId")
                } catch (e: Exception) {
                    android.util.Log.w("SantheAI", "Enquiry fetch failed: ${e.message}")
                }
            } else {
                android.util.Log.w("SantheAI", "No resolved artisan ID — seller listings and inquiries will be empty")
            }
        }

        // All marketplace products
        var allProductsSummary = ""
        try {
            val allProducts = productRepository.getAllProducts().first()
            allProductsSummary = allProducts.take(20).joinToString(" | ") {
                "${it.name} (₹${it.price}, ${it.category}) by ${it.artisan} from ${it.location}"
            }
            if (allProducts.size > 20) allProductsSummary += " | ...and ${allProducts.size - 20} more products"
        } catch (_: Exception) { }

        // All artisans / customers on the platform
        var allArtisansSummary = ""
        try {
            val allArtisans = artisanRepository.getAllArtisans().first()
            allArtisansSummary = allArtisans.joinToString(" | ") {
                "${it.name} (${it.craft}, ${it.location})"
            }
        } catch (_: Exception) { }

        // Read user's selected language
        val language = try {
            LanguageManager.getLanguageFlow(getApplication()).first()
        } catch (_: Exception) { "en" }

        android.util.Log.d("SantheAI", "UserContext built: profile=${artisanProfile.isNotBlank()}, listings=${sellerListings.isNotBlank()}, inquiries=${sellerInquiries.isNotBlank()}, lang=$language")

        return UserContext(
            userName = userName,
            userEmail = userEmail,
            userPhone = userPhone,
            userId = userId ?: "",
            cartSummary = cartSummary,
            wishlistSummary = wishlistSummary,
            recentOrders = recentOrders,
            sellerListings = sellerListings,
            sellerInquiries = sellerInquiries,
            artisanProfile = artisanProfile,
            isSeller = isSellerMode,
            allProductsSummary = allProductsSummary,
            allArtisansSummary = allArtisansSummary,
            language = language
        )
    }

    fun clearChat() {
        val lang = try {
            kotlinx.coroutines.runBlocking {
                LanguageManager.getLanguageFlow(getApplication()).first()
            }
        } catch (_: Exception) { "en" }
        if (_currentIsSeller.value) {
            _sellerMessages.value = listOf(
                ChatMessage(text = sellerGreeting(lang), isUser = false)
            )
        } else {
            _buyerMessages.value = listOf(
                ChatMessage(text = buyerGreeting(lang), isUser = false)
            )
        }
        _error.value = null
    }

    fun dismissError() {
        _error.value = null
    }
}
