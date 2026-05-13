package com.example.hallisanthe.data

object SampleData {

    val categories = listOf(
        Category("all", "All", "", "category"),
        Category(
            "terracotta", "Terracotta",
            "https://lh3.googleusercontent.com/aida-public/AB6AXuCm3hDIKsAg-BHKjR7GYCQqhna3C-VU73CKEpo10GiGMZ5v3iVOCs7K0YAN46iuODDfnQVhSl0pR0G5bTSizt51w9YVKbu4UFlIte8dYsRQHtkvjrPSko4T8dsdiJa2YkPkYNdsCaxxN7-xnBwzHE_PlzoCwBC74NnoMCCIkf3k-lcCeMljzl7R1e8gCBHyBe3WO2fTn8T5PCeRF4BOMaI1u9R9T9nb7Fa2_Rew5zw1C0n3tj2ixuGSsZcadLU8LbnvbhsuMpyMACGA"
        ),
        Category(
            "toys", "Toys",
            "https://lh3.googleusercontent.com/aida-public/AB6AXuCr36YRj8c7cE-xSnP_ybg5ffoqHSXnCOryMcNJlPQyfssXRmCOgrmeNgyRZxkEkr7yK9aYHYDUYzq2YRxw8MyGlcio93Fqomiu-A2pjo_CRsf4L3lZJxPm10Et1YVbicUc5tES3bzBSgs4Ml6uXIS9ssuRUkdtVFefTVO8EzJgAdZd0Md5f1-AlkVrMwltNbo8RaBRYSMJlGWYnF_3VGkw921iVl-2opFvHC8OfR9yf_WOAGBgl7VrhRmtaj-ZZZFAcZQhDQMRatam"
        ),
        Category(
            "textiles", "Textiles",
            "https://lh3.googleusercontent.com/aida-public/AB6AXuBGDISWtUqKAZHOJYBKQirFmQbXAW-bJq-6D1j6yd8aQ5kN_L8TU2wizUT4OD3q9GXl-sOmDD7d8b-6U6SXSrb8pbXCQgVFrLW2sOijwrzterYYVxytNHtUw0zra6Y8uAA_ZmI4iEOLkCDo8ErhKsBqpanxvMCTP7ZUhPPels4iOGBYepbYpNepx-HrirSEqP7fsXSMwrvI51iZbPzBD6GTTOO9CsBjHS_1raU5gJCe_BR3osfljpz-KrX6827Zj4hVmfvumMXbgbHY"
        ),
        Category(
            "spices", "Spices",
            "https://lh3.googleusercontent.com/aida-public/AB6AXuAz2WjvcvvwzkgZJdLE9zRPosyzkssj_x1quEfvQplFbNU3pEZTi2J6NETVXymRcE5YUL82Rc9m_63QRccgv8b5lBtkcJqyDf6Fmoo_jRnYaHjVR0V3jPuQFvwRW8KslHSMTt6FutHuGoSl87I7m9DgkJ1AMpgyGCPyxPa4fad3VqIwyvPIB8ad0WkS8EOyVJdr1niTqPtdNyzPKrKAbwjbmV9BvmvthtdQghbkbrvR8RixgX6P0Qtht_86hQOrqnahb6MKr2Bzhr-R"
        ),
        Category(
            "basketry", "Basketry",
            "https://lh3.googleusercontent.com/aida-public/AB6AXuCal12C6OAo8IvM7LEv5P2lKL-eXpj1IPJOa5b631wSG5a8O5oBTxYAUkJCLu29tXULgJKjftvp4Tz1jRVSrdH3uStU19iG12PKwBUX_7KzxFSdL5D7eIhmE_QQPhC4Hu8iFJEw590AIB_bLoGwVp3NzvaIL5Gu7lQ9xHbJTK23Tn8HKvltglBR6wJ1x3ZIhA6bVKfgKdPaQ-L2gfonFa8MIeU1s8AG5ShV3Yn9ffJeMJTgpZRCQE3KpzNZs0z_6Vum80fxy7GD1itC"
        ),
        Category("jewelry", "Jewelry", "https://picsum.photos/seed/catjewelry/400/400", "category"),
        Category("paintings", "Paintings", "https://picsum.photos/seed/catpaintings/400/400", "category"),
        Category("leather", "Leather", "https://picsum.photos/seed/catleather/400/400", "category")
    )

    val products = listOf(
        Product(
            id = "p1",
            name = "Channapatna Wooden Train",
            artisan = "Ravi Kumar",
            location = "Karnataka",
            price = 450,
            imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuB-wOy7476DgerlknxStWYuXJhw2HMKJkUB6Rd2IgnDk9x-GlWtIu4QkqZO3iIJB0i97OwEiSBQoK6-rPaR_Vo4BjnXYCYDWdu6XyYQ_Ix2-GuwRBGEbFfy30P7YghBkG45cz2dWY0qysON9dtwGpoN9pnYVvI_5UD9z7BOT0Q8fg2GCd7o5uC4dCZXsdxgoxz6z1bqbUvybjmh42pI5yih-5rJv469v4zdIsZgT7FOavul1kLWIh1ENW4z3Kwni2gWqaa4TaDLvIYg",
            category = "toys",
            tags = listOf("Handcrafted"),
            description = "Vibrant hand-painted wooden toys in bright lacquer colors. Made using the 200-year-old Channapatna craft tradition.",
            heritage = "Rooted in a tradition patronized by Tipu Sultan, these toys are crafted from ivory-wood (Aale Mara) using natural vegetable dyes."
        ),
        Product(
            id = "p2",
            name = "Cane Storage Basket",
            artisan = "Northeast Artisans Co-op",
            location = "Assam",
            price = 850,
            imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuC_UMztJIxGdtmwJM8lD4J-s0qkgTN5ndNYC65krPkO90r1WjcIZztPCHMAwv7fvyge09Dvx5RUWOJIGTD5UqaJXexywCSZO_jLbnXpJwotgate79DoN-5te0FdNaqI3mnAYcpVf-keYuV6UcF1u3Fa5BUKO-06K6Unty6hy61vJHfvTXE-m3a57sZpkUtlT2aEH0Q1whkLOnuSJtNBZKSncHV_b4C_jwEBZLQQFzxFz2MwV8i_jGgrl6wgNaR3J6VGhTrJJ4iYXxqu",
            category = "basketry",
            description = "Intricately woven natural cane and bamboo basket, perfect for storage."
        ),
        Product(
            id = "p3",
            name = "Traditional Clay Matka",
            artisan = "Kumbhar Wada Guild",
            location = "Rajasthan",
            price = 320,
            imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuDX_Yugp_quNirN6eoKPWbdAW0nOBDvF8EGo-2oFvNwdoRA93I-lYV9fs3E2tEB0QYYOVPVDDDDw6Qm05gpq4sCJT1ubU3Ti55QoQT5Vl4bNraaK5OF0jB6jiytcl0eIrhcxpkt3R2-kGlcdw5iH49BtioGC_KTr8uVPRlBnHPIwyhYyPKGLs6_LAPcYVyblBM34AJjdkxVwdpRYs1NadMa3o8rUShvpa7eU8_fufmoghlCW3ah9oJ5PSyXdcHwv2luft_U2mtO4j",
            category = "terracotta",
            tags = listOf("Eco-friendly"),
            description = "Traditional handmade terracotta clay water pot with ornate carvings."
        ),
        Product(
            id = "p4",
            name = "Handloom Cotton Stole",
            artisan = "Weaver's Knot",
            location = "West Bengal",
            price = 1200,
            imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuDhQzH7aPFc-yxlR-KRE7q1FdoMPjRV0Znn_bb8PCshx-UAwqUcVzRuGKIAA5dLs85RsSln-2U5YZZuK5F4XAewDWaBKEmYm2PtopvTS9VWhc2HrKkWLx1qj-uJca7AOG83XEh0i3ujBapxG08zocoCXxjiIufC62f-ePN6WDZ145EoMLwFvYGbUt0M70-A9DQQMUsIwHB_OjGUah1Z7PhR2Jh4x0YjPzYtVXQfIW-8TE4kTV_UhcfJzuwVvwvQMC7vZXfs2M9PiBZG",
            category = "textiles",
            description = "Detailed close-up of colorful handwoven textile fabric with intricate patterns."
        ),
        Product(
            id = "p5",
            name = "Prithvi Terracotta Pot",
            artisan = "Ramesh Kumar",
            location = "Rajasthan",
            price = 850,
            originalPrice = 1100,
            imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuCRAQweZKgCr6e5RYU2Kjm4IlWh4DqxqODD46GAd-zAXQn6BZMnYy_dp_XWYmyYx_TwQ4ym97oQy4bEzTkgEa_ujjR5Rm9M6JBkcHu2lerEIhm7YmXO_8nqzEKv7qi3R7AUY4gzD2XMXP5DTPsE0Xjj5VNXgCPvCrjFxhNPxc3P1XcLUmAUV4DbqthD9WLHHYN4-PtrbxPN7jXiwr8sn-Rww7GZUz3I4XunsOvaRKU1a0UJaeiPuNWTgEVgKgngMV-aE-zCLob1mph7",
            category = "terracotta",
            tags = listOf("Best Seller"),
            isFeatured = true,
            description = "Intricately hand-carved terracotta clay water pot with floral motifs.",
            heritage = "A masterpiece of Rajasthani potter craft, each piece takes 3 days to hand-carve and fire."
        ),
        Product(
            id = "p6",
            name = "Set of 4 Kulhads",
            artisan = "Sunita Bai",
            location = "Madhya Pradesh",
            price = 320,
            imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuBaswKqcfnVW8hzXhR85kuKLVbhc0uofKGMmBkGrP6v2TlW-lrIo1BoaLRkkKCWLUy5CL38k_MbVBqZvhD27PmOl-ZpF-xumx3HX6cxtbZjJQBbJeuL8KuTBvtOjnCPWpnlX80UNz8Xk9OtDGh8bML7A-URC-Biw1PLNBFyiRgRmnDNccZUz0YVPNlt_chbRN8LsZfO9Z8GebPbyUYquBwArAYNCqXUbL6pZLycerCBFJucq3JlhnYYcM6LOTh7KNvflb_VAGoK5DPQ",
            category = "terracotta",
            description = "Small handmade terracotta tea cups arranged on a wooden board."
        ),
        Product(
            id = "p7",
            name = "Folk Art Wall Mask",
            artisan = "Gopal Das",
            location = "Odisha",
            price = 480,
            imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuCRRDxilqziN1JhlwjRia6gBmYS8Cvaw27JXt-cDvj3Qkr65HkVH9lIOjXmhWdwmdo7ZGrvLoriTjpKk7B0G5iI-hH-S4If4COw7QqKxz0m1P-WANH1nPjT-wazrtKDgMQHvDrwfLalwkCAe_Ml3th7FdwJFxYRTty-wDmfigKw9aQQe0zECz7Gm-P0WjpQ-GdJEwoYHmFrjQL_2egThCTDTUOloquWV8HE0YbEQpx5utFEzXN_H_hnp6ajGVwgX9tvjYcT4m2ta6wE",
            category = "terracotta",
            description = "Vibrant hand-painted terracotta mask with traditional Indian folk art patterns."
        ),
        Product(
            id = "p8",
            name = "Blue Ikat Cushion Cover",
            artisan = "Kavitha",
            location = "Pochampally, Telangana",
            price = 1250,
            imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuBSoMF7u0p-W-EhRQUGf8JfhjJQ6IStxb35BNQ9xRkscL9D7Y5mJAuuWdOp2UEubf2vh22xjCU4gCRzPGkUqVCJonSMM5IlpKJgY4N-lfO_60h_wU7dJVfZ2H7WfHELbRFdQ4Jo19B_tCqNdgHCHLR-89hgevQti2HgPP11E76tSdGB3Kn8F7a5pt4ahnOI82FgW2QpLCbzY0C3uzMFJuXbpyMitWb609MrJu5Basu5KH2Xt48I52j4plFzbppco7KzWo8dOZxd38Ri",
            category = "textiles",
            description = "Close-up of vibrant blue and orange Ikat hand-woven fabric."
        ),
        Product(
            id = "p9",
            name = "Classic Channapatna Stacking Train Set",
            artisan = "Gowramma & Family",
            location = "Channapatna, Karnataka",
            price = 850,
            originalPrice = 1200,
            imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuBWBw5kf9aoX5MKBbihK0zqoENE1hpA6T1bOwcUwI7CGoYQgO9zPC37sr29ZXLHsWW80VpdBUsKJQZG-2B_7bpCzJubBnDB40zTfja6NsWWlWjn5OtlgvOp-vSpsrsWb4xCv_4dDJbL7W8DKZFZ02BFw8ajis3XXEyEDmjeqZAVKEqD5Taswe9oMkc9a4ufsEZXpw1Rpk-38KOJzBNCB5EfgN-J9L4bwxlTSxJmGrGTnV7DVjCWcE8bqgjfod_SAO5y97BC27aibNvo",
            category = "toys",
            tags = listOf("Non-Toxic Colors", "Hand-turned Wood"),
            isFeatured = true,
            description = "Vibrant hand-painted wooden Channapatna toys. Safe for children of all ages.",
            heritage = "Rooted in a 200-year-old tradition patronized by Tipu Sultan. Colors achieved through natural vegetable dyes—turmeric for yellow, indigo for blue, kumkum for red—sealed with natural shellac."
        ),
        Product(
            id = "p10",
            name = "Hand-etched Diya",
            artisan = "Meena Devi",
            location = "Uttar Pradesh",
            price = 150,
            imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuCAxygNYmZHLbwbmrXq_sE4qxohfguBtFAW0p92BLxJ3KudIyux0W8vlDN6doUqXQQx0hGJ5teNXzDwpwonHGMLs2bmnsZS2g1JTX-QReULwrUnF2gPe09BUoYFPiFmXuYj9jMVTeHjG5NX8hB89af_PSVmmeGFe4Pa7qe5Z886GHlJlAMfuaTlJF40psNZ5jPL_viwGWdBBNhpZanGVVKZorFWwwTXnYmimSMFOodY7RMGef-vETvxPj56OYtS2HpS9Kq8xUbvBKSx",
            category = "terracotta",
            description = "Traditional terracotta diya lamp with hand-etched patterns."
        ),
        Product(
            id = "p11",
            name = "Kashmiri Walnut Jewelry Box",
            artisan = "Devika Sharma",
            location = "Jammu & Kashmir",
            price = 1200,
            imageUrl = "https://upload.wikimedia.org/wikipedia/commons/b/b8/Chinar_leaf_used_as_a_pattern_for_creating_various_wood_works_in_kashmir.jpg",
            category = "jewelry",
            tags = listOf("Hand-carved", "Intricate"),
            description = "Exquisite hand-carved walnut wood jewelry box with Kashmiri floral motifs. Features velvet-lined compartments.",
            heritage = "Kashmiri walnut wood carving is a centuries-old craft using wood from the Kashmir walnut tree, known for its fine grain."
        ),
        Product(
            id = "p12",
            name = "Banarasi Silk Dupatta",
            artisan = "Lakshmi Devi",
            location = "Varanasi, Uttar Pradesh",
            price = 2500,
            originalPrice = 3200,
            imageUrl = "https://upload.wikimedia.org/wikipedia/commons/f/f0/Banarasi_sari_pallu_by_ashish4.JPG",
            category = "textiles",
            tags = listOf("Brocade", "Zari Work"),
            isFeatured = true,
            description = "Luxurious handwoven Banarasi silk dupatta with gold zari border and floral buta motifs.",
            heritage = "Banarasi weaving dates back to the Mughal era, with each piece taking 15-20 days on traditional pit looms."
        ),
        Product(
            id = "p13",
            name = "Madhubani Peacock Wall Art",
            artisan = "Raju Patel",
            location = "Madhubani, Bihar",
            price = 1800,
            imageUrl = "https://upload.wikimedia.org/wikipedia/commons/8/84/Colorful_Madhubani_painting.jpg",
            category = "paintings",
            tags = listOf("Natural Dyes", "Acid-free Paper"),
            description = "Vibrant Madhubani painting depicting a majestic peacock surrounded by floral patterns.",
            heritage = "Madhubani art is a 2,500-year-old tradition from the Mithila region, originally created by women on walls during festivals."
        ),
        Product(
            id = "p14",
            name = "Kerala Coir Doormat",
            artisan = "Northeast Artisans Co-op",
            location = "Kerala",
            price = 650,
            imageUrl = "https://upload.wikimedia.org/wikipedia/commons/e/ed/Coir_mat1.jpg",
            category = "basketry",
            tags = listOf("Eco-friendly", "Durable"),
            description = "Natural coir doormat with anti-slip backing, woven from coconut husk fibers."
        ),
        Product(
            id = "p15",
            name = "Rajasthani Lac Bangles Set",
            artisan = "Devika Sharma",
            location = "Jaipur, Rajasthan",
            price = 350,
            imageUrl = "https://upload.wikimedia.org/wikipedia/commons/d/d3/Making_of_Lac_Bangles_at_Aadi_Mahotsav_in_Delhi_28.jpg",
            category = "jewelry",
            tags = listOf("Vibrant Colors", "Lightweight"),
            description = "Set of 6 hand-crafted lac bangles in ruby red, emerald green and gold mirror work."
        ),
        Product(
            id = "p16",
            name = "Warli Painted Clay Pot",
            artisan = "Sunita Bai",
            location = "Maharashtra",
            price = 550,
            imageUrl = "https://upload.wikimedia.org/wikipedia/commons/8/85/Warli_painting_in_Warli.JPG",
            category = "terracotta",
            tags = listOf("Tribal Art"),
            description = "Terracotta pot adorned with traditional Warli tribal art depicting daily village life."
        ),
        Product(
            id = "p17",
            name = "Organic Assam Tea 500g",
            artisan = "Mohan Lal",
            location = "Assam",
            price = 480,
            originalPrice = 600,
            imageUrl = "https://upload.wikimedia.org/wikipedia/commons/c/cc/Assam_black_tea.jpg",
            category = "spices",
            tags = listOf("Organic", "Single Origin"),
            description = "Premium loose-leaf Assam black tea from small-batch organic gardens in the Brahmaputra valley."
        ),
        Product(
            id = "p18",
            name = "Handcrafted Leather Jutti",
            artisan = "Karim Khan",
            location = "Punjab",
            price = 950,
            imageUrl = "https://upload.wikimedia.org/wikipedia/commons/c/c9/Punjabi_jutti_at_Dilli_Haat.jpg",
            category = "leather",
            tags = listOf("Embroidered", "Soft Sole"),
            description = "Traditional Punjabi leather jutti with intricate gold thread embroidery and cushioned insole."
        ),
        Product(
            id = "p19",
            name = "Blue Pottery Serving Bowl",
            artisan = "Ramesh Kumar",
            location = "Jaipur, Rajasthan",
            price = 720,
            imageUrl = "https://upload.wikimedia.org/wikipedia/commons/c/cc/Blue_Pottery%2C_Albert_Hall_Museum%2C_Jaipur.jpg",
            category = "terracotta",
            tags = listOf("Turquoise", "Microwave Safe"),
            description = "Jaipur blue pottery bowl with Persian-inspired floral patterns in turquoise and cobalt blue."
        ),
        Product(
            id = "p20",
            name = "Kolhapuri Chappal Pair",
            artisan = "Karim Khan",
            location = "Kolhapur, Maharashtra",
            price = 1100,
            originalPrice = 1400,
            imageUrl = "https://upload.wikimedia.org/wikipedia/commons/9/91/Kolhapuri_foot_wear_in_Hyderabad%2C_India.jpg",
            category = "leather",
            tags = listOf("Durable", "Water Resistant"),
            isFeatured = true,
            description = "Authentic Kolhapuri leather chappals with traditional braided straps and thick buffalo leather sole."
        ),
        Product(
            id = "p21",
            name = "Bamboo Spice Box",
            artisan = "Northeast Artisans Co-op",
            location = "Assam",
            price = 580,
            imageUrl = "https://upload.wikimedia.org/wikipedia/commons/a/a1/A_bamboo_basket_image_1.JPG",
            category = "basketry",
            tags = listOf("7 Compartments", "Airtight Lid"),
            description = "Handwoven bamboo masala dabba with 7 compartments and natural cork lid to keep spices fresh."
        ),
        Product(
            id = "p22",
            name = "Tribal Gond Painting",
            artisan = "Raju Patel",
            location = "Bhopal, Madhya Pradesh",
            price = 2200,
            imageUrl = "https://upload.wikimedia.org/wikipedia/commons/0/02/Gond_Painting_of_MP1.JPG",
            category = "paintings",
            tags = listOf("Dotted Patterns", "Canvas"),
            isFeatured = true,
            description = "Gond tribal painting featuring a tiger in characteristic dotted and dashed patterns on canvas.",
            heritage = "Gond painting is an ancient tribal art form from Madhya Pradesh where artists use dots and lines to create vibrant animal and nature motifs."
        )
    )

    val artisans = listOf(
        Artisan(
            id = "a1",
            name = "Gowramma & Family",
            location = "Channapatna, Karnataka",
            avatarUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuBzLKJAUFbSI2TOjXQUKXfwVf-I0_XZ8zt2mUBHNco2XQciAiYS4l3K4jeGbrJ_PxZCNWwhP4qjW9PCpVc552ydR_sECObZbBYRAsHZPpR9hZLpIS4zeFG6CSIRzMCiMh6p0JfErMOIZIXOBS1LfXh0G28wx5q2gvUfaOlgOzRsBzT1DFNDB7Lu_iHYbsPWCMR8TCaJA5d0BPW9a-zd3zuzR-MTEgosCTpxai06SjAeSrMuq_D_xttMzFpEw993Ltp6BBBGVcvXE9-3",
            bio = "Three-generation family of Channapatna toy makers. UNESCO recognized craft.",
            craft = "Channapatna Toys"
        ),
        Artisan(
            id = "a2",
            name = "Ramesh Kumar",
            location = "Jaipur, Rajasthan",
            avatarUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuB38cjAmVplsXrO3MOb3hGAinEEOcdYqqrh2eOaekNTVrpmPwIAR3a68tDQsTKH4-LNw5q33zU_AvV1iePNojxvZjGFkH14HNir1K5wi7_OT7_Grpf4wynEbuZdDtZV7NE4FtUn15QxKPclh_-TOxnVSIhLcYxKE9ejGM_sAYdY9JRfnEBGmCFvjBmQJz6WuWWw9wFv6L636yeo2bput6Diw4Lan5CGq-mxjiDVU8E0jXhiI0W8f67OLrnm2BRLVsG1j5B4zWteDCO3",
            bio = "Master terracotta potter with 25 years of experience in Rajasthani folk art.",
            craft = "Terracotta Pottery"
        ),
        Artisan(
            id = "a3",
            name = "Kumbhar Wada Guild",
            location = "Jodhpur, Rajasthan",
            avatarUrl = "",
            bio = "Traditional terracotta potters preserving Rajasthani clay art for over five generations.",
            craft = "Terracotta"
        ),
        Artisan(
            id = "a4",
            name = "Ravi Kumar",
            location = "Channapatna, Karnataka",
            avatarUrl = "",
            bio = "Third-generation Channapatna toy maker using natural vegetable dyes and ivory wood.",
            craft = "Wooden Toys"
        ),
        Artisan(
            id = "a5",
            name = "Northeast Artisans Co-op",
            location = "Guwahati, Assam",
            avatarUrl = "",
            bio = "A cooperative of cane and bamboo weavers creating sustainable eco-friendly products.",
            craft = "Basketry"
        ),
        Artisan(
            id = "a6",
            name = "Sunita Bai",
            location = "Indore, Madhya Pradesh",
            avatarUrl = "",
            bio = "Expert in traditional Madhya Pradesh terracotta craft and tribal painting.",
            craft = "Terracotta"
        ),
        Artisan(
            id = "a7",
            name = "Lakshmi Devi",
            location = "Shantiniketan, West Bengal",
            avatarUrl = "",
            bio = "Master weaver specializing in Bengal handloom, batik, and kantha textiles.",
            craft = "Handloom Textiles"
        ),
        Artisan(
            id = "a8",
            name = "Mohan Lal",
            location = "Kochi, Kerala",
            avatarUrl = "",
            bio = "Organic spice farmer and tea blender sourcing from the Western Ghats.",
            craft = "Spices & Tea"
        ),
        Artisan(
            id = "a9",
            name = "Devika Sharma",
            location = "Jaipur, Rajasthan",
            avatarUrl = "",
            bio = "Expert lac jewelry artisan creating traditional Rajasthani bangles and ornaments.",
            craft = "Lac Jewelry"
        ),
        Artisan(
            id = "a10",
            name = "Raju Patel",
            location = "Bhopal, Madhya Pradesh",
            avatarUrl = "",
            bio = "Folk artist preserving Madhubani and Gond painting traditions of Central India.",
            craft = "Folk Art"
        ),
        Artisan(
            id = "a11",
            name = "Karim Khan",
            location = "Kolhapur, Maharashtra",
            avatarUrl = "",
            bio = "Master leather craftsman making traditional Kolhapuri chappals and Punjabi juttis.",
            craft = "Leather Crafts"
        )
    )

    val cartItems = mutableListOf(
        CartItem(products[7], quantity = 1),  // Blue Ikat Cushion
        CartItem(products[2], quantity = 2),  // Clay Matka
        CartItem(products[8], quantity = 1)   // Channapatna Stacking Train
    )

    val bannerImageUrl =
        "https://lh3.googleusercontent.com/aida-public/AB6AXuATpzSLCRgJOe3Nuzkkaj6W2txVi5dir1XLu1DqVcQWLwOUsvVHK12Et-ji12R31jP5PHv6oT5sIccddrWPwzqUZbhHKi7TzF25Q59dEalhbwuKN4Cj6LiArLStqBx9mfXS7iRh_iA2edUql-wP_277BR-7TeQJBApOhzwb9E7kKsUF_FCHJtvwLmbOgAbS3vde3K1UaA4S87OXiR26uNTRKly2VPVd4uhDHQFtIIauZcHUQV2r7Djff4tW9cBlh6XfOyl6GFHbBbun"

    val orders = listOf(
        Order("ORD1001", "24 Oct 2023", "Delivered", listOf(CartItem(products[0], 1)), 450),
        Order("ORD1002", "15 Nov 2023", "Processing", listOf(CartItem(products[4], 1), CartItem(products[2], 2)), 1490)
    )

    val notifications = listOf(
        Notification("n1", "Order Delivered", "Your order ORD1001 has been delivered.", "2 hours ago"),
        Notification("n2", "Price Drop", "Items in your wishlist have a price drop!", "1 day ago", type = "promo"),
        Notification("n3", "New Collection", "Check out the new terracotta collection from Jaipur.", "3 days ago")
    )
}
