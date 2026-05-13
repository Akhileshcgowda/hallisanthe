package com.example.hallisanthe.data.repository

import android.util.Log
import com.example.hallisanthe.BuildConfig
import com.example.hallisanthe.data.UserContext
import com.example.hallisanthe.data.remote.api.GeminiApiService
import com.example.hallisanthe.data.remote.dto.GeminiRequest
import com.example.hallisanthe.data.remote.dto.GeminiResponse

class GenAIRepository(private val apiService: GeminiApiService) {

    private val apiKey: String = BuildConfig.GEMINI_API_KEY

    companion object {
        private const val TAG = "SantheAI"
    }

    suspend fun chat(
        history: List<Pair<String, String>>,
        userContext: UserContext = UserContext()
    ): Result<String> {
        Log.d(TAG, "chat() called with ${history.size} history items")
        Log.d(TAG, "API key present: ${apiKey.isNotBlank()}, length: ${apiKey.length}")

        if (apiKey.isBlank() || apiKey == "YOUR_GEMINI_API_KEY") {
            Log.w(TAG, "No API key configured; using fallback replies")
            val lastUserMessage = history.lastOrNull { it.first == "user" }?.second?.lowercase() ?: ""
            return Result.success(fallbackReply(lastUserMessage, userContext))
        }

        return try {
            val systemPrompt = buildSystemPrompt(userContext)

            val exchangeHistory = history.filter { it.second.isNotBlank() }
            Log.d(TAG, "Sending ${exchangeHistory.size} messages to Gemini with user context")

            val contents = mutableListOf<GeminiRequest.Content>()
            contents.add(
                GeminiRequest.Content(
                    role = "user",
                    parts = listOf(GeminiRequest.Content.Part(text = systemPrompt))
                )
            )
            contents.add(
                GeminiRequest.Content(
                    role = "model",
                    parts = listOf(GeminiRequest.Content.Part(text = "Understood. I am Santhe AI, ready to help with Halli Santhe."))
                )
            )
            exchangeHistory.forEach { (role, text) ->
                contents.add(
                    GeminiRequest.Content(
                        role = role,
                        parts = listOf(GeminiRequest.Content.Part(text = text))
                    )
                )
            }

            val request = GeminiRequest(contents = contents)
            val response = apiService.generateContent(apiKey, request)

            if (response.error != null) {
                Log.e(TAG, "Gemini API error: ${response.error.code} - ${response.error.message}")
                return Result.success(fallbackReply(
                    exchangeHistory.lastOrNull { it.first == "user" }?.second?.lowercase() ?: "",
                    userContext
                ))
            }

            val text = response.candidates?.firstOrNull()
                ?.content?.parts?.firstOrNull()?.text

            Log.d(TAG, "Gemini response text length: ${text?.length ?: 0}")

            if (text != null) {
                Result.success(text.trim())
            } else {
                Log.w(TAG, "Empty response from AI (no candidates)")
                Result.failure(Exception("Empty response from AI"))
            }
        } catch (e: Exception) {
            Log.e(TAG, "API call failed: ${e.message}")
            val lastUserMessage = history.lastOrNull { it.first == "user" }?.second?.lowercase() ?: ""
            Result.success(fallbackReply(lastUserMessage, userContext))
        }
    }

    private fun buildSystemPrompt(ctx: UserContext): String {
        val sb = StringBuilder()
        sb.appendLine("You are Santhe AI, a warm and knowledgeable assistant for Halli Santhe — an Indian hyper-local artisan marketplace app that connects village artisans with urban buyers.")
        sb.appendLine()
        sb.appendLine("Personality:")
        sb.appendLine("- Friendly, respectful, and culturally aware.")
        sb.appendLine("- Help buyers discover handmade products (sarees, pottery, woodwork, jewelry, etc.).")
        sb.appendLine("- Help sellers with uploading products, writing descriptions, pricing tips, and shop settings.")
        sb.appendLine("- Keep replies concise (under 80 words) and helpful.")
        sb.appendLine("- If asked about topics outside the app, gently steer back to Halli Santhe.")

        // Language instruction
        when (ctx.language) {
            "kn" -> {
                sb.appendLine()
                sb.appendLine("LANGUAGE INSTRUCTION — CRITICAL:")
                sb.appendLine("- You MUST reply entirely in Kannada (ಕನ್ನಡ) using Kannada script.")
                sb.appendLine("- Use English words only for app-specific terms that have no Kannada equivalent.")
                sb.appendLine("- Example greeting: 'ನಮಸ್ಕಾರ! ನಾನು ಸಂತೆ AI, ನಿಮ್ಮ ಸಹಾಯಕ. ನಿಮಗೆ ಹೇಗೆ ಸಹಾಯ ಮಾಡಬಹುದು?'")
                sb.appendLine("- Keep the tone warm, respectful, and village-friendly.")
            }
            "hi" -> {
                sb.appendLine()
                sb.appendLine("LANGUAGE INSTRUCTION — CRITICAL:")
                sb.appendLine("- You MUST reply entirely in Hindi (हिंदी) using Devanagari script.")
                sb.appendLine("- Use English words only for app-specific terms that have no Hindi equivalent.")
                sb.appendLine("- Example greeting: 'नमस्ते! मैं संतए AI हूँ, आपका सहायक। मैं आपकी कैसे सहायता कर सकता हूँ?'")
                sb.appendLine("- Keep the tone warm, respectful, and village-friendly.")
            }
            else -> {
                sb.appendLine()
                sb.appendLine("LANGUAGE INSTRUCTION — CRITICAL:")
                sb.appendLine("- Reply in simple English with occasional Kannada/Hindi greetings like 'Namaskara' or 'Namaste'.")
            }
        }
        sb.appendLine()
        sb.appendLine("App features you can reference:")
        sb.appendLine("- Home: Browse featured artisan products")
        sb.appendLine("- Search: Find products by category, village, or craft")
        sb.appendLine("- Cart & Checkout: Buy with Cash on Delivery or UPI")
        sb.appendLine("- Add Product: Sellers upload photos, name, price, category, and description")
        sb.appendLine("- My Listings: View and manage seller products")
        sb.appendLine("- Shop Settings: Update shop name, craft type, bio, contact info")
        sb.appendLine("- My Orders: Track buyer purchases")
        sb.appendLine("- Inquiries: Buyer-seller messaging")
        sb.appendLine()

        if (ctx.userName.isNotBlank() || ctx.userEmail.isNotBlank()) {
            sb.appendLine("Current user profile:")
            if (ctx.userName.isNotBlank()) sb.appendLine("- Name: ${ctx.userName}")
            if (ctx.userEmail.isNotBlank()) sb.appendLine("- Email: ${ctx.userEmail}")
            if (ctx.userPhone.isNotBlank()) sb.appendLine("- Phone: ${ctx.userPhone}")
            sb.appendLine()
        }

        if (ctx.cartSummary.isNotBlank()) {
            sb.appendLine("Current cart:")
            sb.appendLine(ctx.cartSummary)
            sb.appendLine()
        }

        if (ctx.wishlistSummary.isNotBlank()) {
            sb.appendLine("Wishlist:")
            sb.appendLine(ctx.wishlistSummary)
            sb.appendLine()
        }

        if (ctx.recentOrders.isNotBlank()) {
            sb.appendLine("Recent orders:")
            sb.appendLine(ctx.recentOrders)
            sb.appendLine()
        }

        if (ctx.isSeller) {
            sb.appendLine("ROLE: You are speaking to a SELLER / ARTISAN on Halli Santhe.")
            sb.appendLine("Your primary job is to help this artisan manage their shop, optimize listings, respond to buyer inquiries, set pricing, and grow their business.")
            sb.appendLine("DO: Give seller-focused advice — listing tips, pricing strategies, inquiry responses, shop settings, craft descriptions.")
            sb.appendLine("DO NOT: Give buyer advice (like 'browse products', 'add to cart', 'track your order') unless the seller explicitly asks about the buyer experience.")
            sb.appendLine()
            if (ctx.artisanProfile.isNotBlank()) {
                sb.appendLine("Seller profile:")
                sb.appendLine(ctx.artisanProfile)
                sb.appendLine()
            }
            if (ctx.sellerListings.isNotBlank()) {
                sb.appendLine("My listings:")
                sb.appendLine(ctx.sellerListings)
                sb.appendLine()
            }
            if (ctx.sellerInquiries.isNotBlank()) {
                sb.appendLine("Buyer inquiries:")
                sb.appendLine(ctx.sellerInquiries)
                sb.appendLine()
            }
        } else {
            sb.appendLine("ROLE: You are speaking to a BUYER / CUSTOMER on Halli Santhe.")
            sb.appendLine("Your primary job is to help this customer discover handmade artisan products, track orders, understand crafts, and navigate the marketplace.")
            sb.appendLine("DO: Give buyer-focused advice — product discovery, order tracking, cart help, craft explanations.")
            sb.appendLine("DO NOT: Give seller advice (like 'upload a product', 'manage your shop') unless the buyer explicitly asks about selling.")
            sb.appendLine()
        }

        if (ctx.allProductsSummary.isNotBlank()) {
            sb.appendLine("MARKETPLACE REFERENCE DATA (use ONLY if user asks about a specific product or artisan):")
            sb.appendLine(ctx.allProductsSummary)
            sb.appendLine()
        }

        if (ctx.allArtisansSummary.isNotBlank()) {
            sb.appendLine("ARTISAN REFERENCE DATA (use ONLY if user asks about a specific artisan):")
            sb.appendLine(ctx.allArtisansSummary)
            sb.appendLine()
        }

        sb.appendLine()
        sb.appendLine("CRITICAL RULES:")
        sb.appendLine("1. ONLY mention specific products, artisans, or listings the user EXPLICITLY asked about.")
        sb.appendLine("2. NEVER list unrelated products, dump the full catalog, or mention items the user didn't ask for.")
        sb.appendLine("3. If the user asks 'What sarees do you have?', only mention saree products from the reference data — not toys, pottery, or woodwork.")
        sb.appendLine("4. If the user asks about one specific product, give details for ONLY that product.")
        sb.appendLine("5. Keep your reply focused. Do not volunteer extra product names 'you may also like' unless explicitly asked.")
        sb.appendLine("6. When answering, actively use the user's actual data above. For example:")
        if (ctx.isSeller) {
            sb.appendLine("   - If they ask about their shop, mention their listings, profile info, and pending inquiries.")
            sb.appendLine("   - If they ask about pricing, reference their existing listings and suggest improvements.")
            sb.appendLine("   - If they ask about buyers, reference their actual buyer inquiries and suggest responses.")
            sb.appendLine("   - If they ask about competitors, mention ONLY the specific competitor or product they asked about.")
        } else {
            sb.appendLine("   - If they ask about their cart, reference the specific items and total shown in Current cart.")
            sb.appendLine("   - If they ask about orders, reference their recent order statuses, totals, and items.")
            sb.appendLine("   - If they ask about wishlist items, mention the specific products they have saved.")
            sb.appendLine("   - If they ask about a specific product (e.g., 'Tell me about Silk Saree'), give details for ONLY that product from the reference data.")
            sb.appendLine("   - If they ask 'What products are available?', give a SHORT curated list of 3-5 items maximum, not the full catalog.")
        }
        sb.appendLine("7. Never say you don't have access to their data — you DO have it in the context above.")

        return sb.toString().trim()
    }

    fun isFallbackReply(reply: String): Boolean {
        return fallbackKeywords.any { keyword -> reply.contains(keyword, ignoreCase = true) }
    }

    private val fallbackKeywords = listOf(
        "upload a product", "Seller Dashboard", "Add Product",
        "browse artisan products", "Home screen", "Search to find",
        "My Orders", "Cash on Delivery", "UPI",
        "Shop Settings", "Seller Profile",
        "Pricing depends", "research similar products",
        "Sanchari team", "Help & Support",
        "discover handmade crafts", "upload your products"
    )

    private fun fallbackReply(query: String, ctx: UserContext = UserContext()): String {
        val namePrefix = when (ctx.language) {
            "kn" -> if (ctx.userName.isNotBlank()) "ನಮಸ್ಕಾರ ${ctx.userName}! " else "ನಮಸ್ಕಾರ! "
            "hi" -> if (ctx.userName.isNotBlank()) "नमस्ते ${ctx.userName}! " else "नमस्ते! "
            else -> if (ctx.userName.isNotBlank()) "Namaskara ${ctx.userName}! " else "Namaskara! "
        }
        return when {
            query.contains("cart") || query.contains("bag") || query.contains("checkout") -> {
                if (ctx.cartSummary.isNotBlank()) {
                    "Your cart has: ${ctx.cartSummary}. Go to Cart to review and checkout with Cash on Delivery or UPI."
                } else {
                    "Your cart is empty right now. Browse the Home screen to add beautiful artisan products!"
                }
            }
            query.contains("wishlist") || query.contains("saved") || query.contains("later") -> {
                if (ctx.wishlistSummary.isNotBlank()) {
                    "Your wishlist has: ${ctx.wishlistSummary}. Tap any item to view details and add to cart."
                } else {
                    "Your wishlist is empty. Tap the heart icon on any product to save it for later!"
                }
            }
            query.contains("order") || query.contains("track") || query.contains("delivery") || query.contains("purchase") -> {
                if (ctx.recentOrders.isNotBlank()) {
                    "Here are your recent orders: ${ctx.recentOrders}. Go to 'My Orders' for full details."
                } else {
                    "You don't have any orders yet. Browse products and add them to your cart to place your first order!"
                }
            }
            query.contains("upload") || query.contains("add product") || query.contains("sell") || query.contains("listing") -> {
                if (ctx.isSeller && ctx.sellerListings.isNotBlank()) {
                    "You currently have these listings: ${ctx.sellerListings}. To add more, go to the Seller Dashboard and tap 'Add Product'."
                } else {
                    "To upload a product, go to the Seller Dashboard and tap 'Add Product'. Fill in the product name, category, price, location, and description. You can also snap a photo directly from the app!"
                }
            }
            query.contains("inquiry") || query.contains("message") || query.contains("buyer") -> {
                if (ctx.isSeller && ctx.sellerInquiries.isNotBlank()) {
                    "You have these buyer inquiries: ${ctx.sellerInquiries}. Check the Inquiries tab to reply."
                } else {
                    "For seller inquiries, use the Inquiries tab to chat directly with buyers. Buyers can also message you from product pages."
                }
            }
            query.contains("shop") || query.contains("profile") || query.contains("my store") -> {
                if (ctx.isSeller && ctx.artisanProfile.isNotBlank()) {
                    "Your shop profile: ${ctx.artisanProfile}. Update details in Shop Settings."
                } else {
                    "Tap 'Shop Settings' in your Seller Profile or Dashboard to update your shop name, craft type, village, bio, phone number, and email."
                }
            }
            query.contains("find") || query.contains("search") || query.contains("buy") || query.contains("saree") || query.contains("pottery") || query.contains("jewelry") || query.contains("wood") || query.contains("toy") || query.contains("basket") || query.contains("product") -> {
                val matched = filterProductsByQuery(ctx.allProductsSummary, query)
                if (matched.isNotBlank()) {
                    "Here is what I found: $matched. Tap any product to view details and add to cart."
                } else {
                    "You can browse artisan products from the Home screen or use Search to find specific items like silk sarees, terracotta pots, woodwork, or jewelry. Each product shows the artisan's village and craft story."
                }
            }
            query.contains("artisan") || query.contains("seller") || query.contains("maker") || query.contains("who made") || query.contains("craftsman") -> {
                if (ctx.allArtisansSummary.isNotBlank()) {
                    "Our talented artisans on Halli Santhe include: ${ctx.allArtisansSummary}. Tap any artisan's name on a product page to learn more about their craft and village."
                } else {
                    "Halli Santhe connects you with skilled village artisans across India. Browse products and tap on an artisan's name to see their profile, craft, and village."
                }
            }
            query.contains("setting") || query.contains("account") ->
                "Tap 'Shop Settings' in your Seller Profile or Dashboard to update your shop name, craft type, village, bio, phone number, and email. This helps buyers connect with you."
            query.contains("price") || query.contains("cost") || query.contains("how much") ->
                "Pricing depends on the craft, materials, and time involved. As a seller, research similar products and consider your costs. As a buyer, every product page shows the price and any offers."
            query.contains("contact") || query.contains("call") || query.contains("help") || query.contains("support") ->
                "For human support, you can reach our Sanchari team. In the app, go to Help & Support from your profile. For seller inquiries, use the Inquiries tab to chat directly with buyers."
            query.contains("hello") || query.contains("hi") || query.contains("namaskara") || query.contains("hey") -> {
                if (ctx.isSeller) {
                    "${namePrefix}Welcome to your Seller Assistant on Halli Santhe. I can help you manage your listings, respond to buyer inquiries, set competitive pricing, and grow your artisan shop. What would you like to do?"
                } else {
                    "${namePrefix}Welcome to Halli Santhe. I can help you discover handmade crafts, track orders, or learn about Indian artisan traditions. What would you like to do?"
                }
            }
            else -> {
                if (ctx.isSeller) {
                    "${namePrefix}I'm here to help you with your Halli Santhe shop! Ask me about managing listings, responding to buyers, pricing your crafts, or shop settings. How can I assist?"
                } else {
                    "${namePrefix}I'm here to help with Halli Santhe! You can ask me about finding products, tracking orders, or learning about Indian crafts. How can I assist?"
                }
            }
        }
    }

    /**
     * Filters the product catalog summary to only return products matching the user's query.
     * Returns at most 3 matching products. If no match, returns first 3 items as a short sample.
     */
    private fun filterProductsByQuery(allProductsSummary: String, query: String): String {
        if (allProductsSummary.isBlank()) return ""
        val products = allProductsSummary.split(" | ").map { it.trim() }.filter { it.isNotBlank() }
        if (products.isEmpty()) return ""

        val queryWords = query.split(" ").map { it.lowercase() }.filter { it.length > 2 }
        val stopWords = setOf("find", "search", "buy", "about", "tell", "me", "what", "do", "you", "have", "the", "how", "much", "is", "are", "can", "i", "get", "show", "all", "some", "any", "want", "looking", "for", "and")
        val meaningfulWords = queryWords.filter { it !in stopWords }

        val matched = if (meaningfulWords.isNotEmpty()) {
            products.filter { product ->
                meaningfulWords.any { word -> product.lowercase().contains(word) }
            }
        } else {
            emptyList()
        }

        return if (matched.isNotEmpty()) {
            matched.take(3).joinToString(" | ")
        } else {
            // No direct match — return a short curated sample
            products.take(3).joinToString(" | ") + " | ...and more on the Home screen"
        }
    }

    suspend fun generateProductDescription(productName: String, category: String): Result<String> {
        return try {
            val prompt = """
                Write a short, warm product description for a handmade Indian artisan product.
                Product: $productName
                Category: $category
                Keep it under 80 words. Highlight the craft tradition and cultural value.
            """.trimIndent()

            val request = GeminiRequest(
                contents = listOf(
                    GeminiRequest.Content(
                        parts = listOf(GeminiRequest.Content.Part(text = prompt))
                    )
                )
            )

            val response = apiService.generateContent(apiKey, request)

            val text = response.candidates?.firstOrNull()
                ?.content?.parts?.firstOrNull()?.text

            if (text != null) {
                Result.success(text.trim())
            } else {
                Result.failure(Exception("Empty response from Gemini API"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun suggestCategory(productName: String): Result<String> {
        return try {
            val prompt = """
                Suggest the best category for this Indian artisan product from: Terracotta, Toys, Textiles, Spices, Basketry, Woodwork, Jewelry, Pottery.
                Product: $productName
                Return only the category name, nothing else.
            """.trimIndent()

            val request = GeminiRequest(
                contents = listOf(
                    GeminiRequest.Content(
                        parts = listOf(GeminiRequest.Content.Part(text = prompt))
                    )
                )
            )

            val response = apiService.generateContent(apiKey, request)

            val text = response.candidates?.firstOrNull()
                ?.content?.parts?.firstOrNull()?.text

            if (text != null) {
                Result.success(text.trim())
            } else {
                Result.failure(Exception("Empty response from Gemini API"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
