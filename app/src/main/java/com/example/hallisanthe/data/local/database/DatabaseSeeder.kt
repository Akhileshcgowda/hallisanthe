package com.example.hallisanthe.data.local.database

import com.example.hallisanthe.data.SampleData
import com.example.hallisanthe.data.local.entity.ArtisanEntity
import com.example.hallisanthe.data.local.entity.CategoryEntity
import com.example.hallisanthe.data.local.entity.EnquiryEntity
import com.example.hallisanthe.data.local.entity.ProductEntity
import com.example.hallisanthe.data.repository.ArtisanRepository
import com.example.hallisanthe.data.repository.CategoryRepository
import com.example.hallisanthe.data.repository.EnquiryRepository
import com.example.hallisanthe.data.repository.ProductRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.UUID

object DatabaseSeeder {

    suspend fun seed(
        database: HalliSantheDatabase,
        productRepository: ProductRepository,
        artisanRepository: ArtisanRepository,
        categoryRepository: CategoryRepository,
        enquiryRepository: EnquiryRepository
    ) = withContext(Dispatchers.IO) {
        val productDao = database.productDao()

        if (productDao.getProductCount() == 0) {
            val artisans = listOf(
                ArtisanEntity("a1", "Gowramma & Family", "Channapatna, Karnataka", "+91 98765 43210", "gowramma@hallisanthe.in", "", "Three-generation family of Channapatna toy makers. UNESCO recognized craft.", "Channapatna Toys"),
                ArtisanEntity("a2", "Ramesh Kumar", "Jaipur, Rajasthan", "+91 98765 43211", "ramesh@hallisanthe.in", "", "Master terracotta potter with 25 years of experience in Rajasthani folk art.", "Terracotta Pottery"),
                ArtisanEntity("a3", "Kumbhar Wada Guild", "Jodhpur, Rajasthan", "+91 98765 43212", "kumbhar@hallisanthe.in", "", "Traditional terracotta potters preserving Rajasthani clay art.", "Terracotta"),
                ArtisanEntity("a4", "Ravi Kumar", "Channapatna, Karnataka", "+91 98765 43213", "ravi@hallisanthe.in", "", "Third-generation Channapatna toy maker using natural vegetable dyes.", "Wooden Toys"),
                ArtisanEntity("a5", "Northeast Artisans Co-op", "Guwahati, Assam", "+91 98765 43214", "contact@neartisans.in", "", "A cooperative of cane and bamboo weavers from Assam.", "Basketry"),
                ArtisanEntity("a6", "Sunita Bai", "Indore, Madhya Pradesh", "+91 98765 43215", "sunita@hallisanthe.in", "", "Expert in traditional Madhya Pradesh terracotta craft.", "Terracotta"),
                ArtisanEntity("a7", "Lakshmi Devi", "Shantiniketan, West Bengal", "+91 98765 43216", "lakshmi@hallisanthe.in", "", "Master weaver specializing in Bengal handloom and batik textiles.", "Handloom Textiles"),
                ArtisanEntity("a8", "Mohan Lal", "Kochi, Kerala", "+91 98765 43217", "mohan@hallisanthe.in", "", "Organic spice farmer and tea blender from the Western Ghats.", "Spices & Tea"),
                ArtisanEntity("a9", "Devika Sharma", "Jaipur, Rajasthan", "+91 98765 43218", "devika@hallisanthe.in", "", "Expert lac jewelry artisan creating traditional Rajasthani bangles and ornaments.", "Lac Jewelry"),
                ArtisanEntity("a10", "Raju Patel", "Bhopal, Madhya Pradesh", "+91 98765 43219", "raju@hallisanthe.in", "", "Folk artist preserving Madhubani and Gond painting traditions.", "Folk Art"),
                ArtisanEntity("a11", "Karim Khan", "Kolhapur, Maharashtra", "+91 98765 43220", "karim@hallisanthe.in", "", "Master leather craftsman making traditional Kolhapuri chappals and juttis.", "Leather Crafts")
            )
            artisanRepository.seedArtisans(artisans)

            val artisanIdMap = artisans.associateBy { it.name }

            val categories = SampleData.categories.map { cat ->
                CategoryEntity(
                    id = cat.id,
                    name = cat.name,
                    imageUrl = cat.imageUrl,
                    iconUrl = cat.icon
                )
            }
            categoryRepository.seedCategories(categories)

            val products = SampleData.products.map { prod ->
                ProductEntity(
                    id = prod.id,
                    name = prod.name,
                    price = prod.price,
                    originalPrice = prod.originalPrice,
                    category = prod.category,
                    imageUrl = prod.imageUrl,
                    artisanId = artisanIdMap[prod.artisan]?.id ?: "a1",
                    artisanName = prod.artisan,
                    location = prod.location,
                    description = prod.description,
                    heritage = prod.heritage,
                    tags = prod.tags.joinToString(","),
                    isVerified = prod.isVerified,
                    isFeatured = prod.isFeatured
                )
            }
            productRepository.seedProducts(products)

            val enquiries = listOf(
                EnquiryEntity(
                    id = UUID.randomUUID().toString(),
                    buyerId = "b1",
                    artisanId = "a1",
                    productId = "p1",
                    buyerName = "Ananya K.",
                    productName = "Channapatna Wooden Train",
                    message = "Is the ruby red color very bright?",
                    timestamp = System.currentTimeMillis() - 7200000,
                    isUnread = true
                ),
                EnquiryEntity(
                    id = UUID.randomUUID().toString(),
                    buyerId = "b2",
                    artisanId = "a1",
                    productId = "p1",
                    buyerName = "Vikram M.",
                    productName = "Channapatna Train Set",
                    message = "Do you offer bulk discounts for 50 pieces?",
                    timestamp = System.currentTimeMillis() - 18000000,
                    isUnread = false
                ),
                EnquiryEntity(
                    id = UUID.randomUUID().toString(),
                    buyerId = "b3",
                    artisanId = "a2",
                    productId = "p2",
                    buyerName = "Sneha R.",
                    productName = "Handwoven Basket",
                    message = "When will the medium size be in stock?",
                    timestamp = System.currentTimeMillis() - 86400000,
                    isUnread = false
                )
            )
            enquiries.forEach {
                try {
                    enquiryRepository.insertEnquiry(it)
                } catch (e: Exception) {
                    // Best-effort: continue seeding even if enquiries fail to sync
                }
            }
        }
    }
}
