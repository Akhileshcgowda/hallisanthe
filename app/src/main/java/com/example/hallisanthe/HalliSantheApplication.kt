package com.example.hallisanthe

import android.app.Application
import coil.Coil
import coil.ImageLoader
import com.example.hallisanthe.data.di.AppContainer
import com.example.hallisanthe.data.local.database.DatabaseSeeder
import com.example.hallisanthe.data.local.database.HalliSantheDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

class HalliSantheApplication : Application() {

    val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    lateinit var appContainer: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()

        val imageLoader = ImageLoader.Builder(this)
            .crossfade(true)
            .okHttpClient {
                OkHttpClient.Builder()
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS)
                    .addInterceptor { chain ->
                        chain.proceed(
                            chain.request().newBuilder()
                                .header("User-Agent", "HalliSanthe/1.0 (Android)")
                                .build()
                        )
                    }
                    .build()
            }
            .build()
        Coil.setImageLoader(imageLoader)

        appContainer = AppContainer(this)

        applicationScope.launch {
            val db = HalliSantheDatabase.getDatabase(applicationContext)
            DatabaseSeeder.seed(
                database = db,
                productRepository = appContainer.productRepository,
                artisanRepository = appContainer.artisanRepository,
                categoryRepository = appContainer.categoryRepository,
                enquiryRepository = appContainer.enquiryRepository
            )
        }
    }
}
