package com.example.hallisanthe.data

import android.content.Context
import android.content.res.Configuration
import java.util.Locale

object LocaleHelper {

    fun applyLocale(context: Context, languageCode: String): Context {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)

        val config = Configuration(context.resources.configuration)
        config.setLocale(locale)
        config.setLayoutDirection(locale)

        return context.createConfigurationContext(config)
    }

    fun getLocale(context: Context): Locale {
        return context.resources.configuration.locales.get(0)
            ?: context.resources.configuration.locale
            ?: Locale.getDefault()
    }
}
