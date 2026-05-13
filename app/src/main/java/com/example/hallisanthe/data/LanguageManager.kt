package com.example.hallisanthe.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "language_prefs")

object LanguageManager {

    private val LANGUAGE_KEY = stringPreferencesKey("app_language")
    private const val DEFAULT_LANGUAGE = "en"

    val supportedLanguages = listOf(
        Language("en", "English", "English"),
        Language("kn", "ಕನ್ನಡ", "Kannada"),
        Language("hi", "हिंदी", "Hindi")
    )

    fun getLanguageFlow(context: Context): Flow<String> {
        return context.dataStore.data.map { prefs ->
            prefs[LANGUAGE_KEY] ?: DEFAULT_LANGUAGE
        }
    }

    suspend fun setLanguage(context: Context, languageCode: String) {
        context.dataStore.edit { prefs ->
            prefs[LANGUAGE_KEY] = languageCode
        }
    }

    fun getDisplayName(code: String): String {
        return supportedLanguages.find { it.code == code }?.nativeName ?: code
    }
}

data class Language(
    val code: String,
    val nativeName: String,
    val englishName: String
)
