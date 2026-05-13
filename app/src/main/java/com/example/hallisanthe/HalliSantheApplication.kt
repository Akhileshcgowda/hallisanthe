package com.example.hallisanthe

import android.app.Application
import com.example.hallisanthe.data.di.AppContainer
import com.example.hallisanthe.data.local.database.DatabaseSeeder
import com.example.hallisanthe.data.local.database.HalliSantheDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class HalliSantheApplication : Application() {

    val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    lateinit var appContainer: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
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
