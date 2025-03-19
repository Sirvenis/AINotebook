package com.ainotebook.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "theme_preferences")

class ThemePreferences(private val context: Context) {
    private val isDarkThemeKey = booleanPreferencesKey("is_dark_theme")
    private val isSystemThemeKey = booleanPreferencesKey("is_system_theme")
    
    val isDarkTheme: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[isDarkThemeKey] ?: false
        }
    
    val isSystemTheme: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[isSystemThemeKey] ?: true
        }
    
    suspend fun setDarkTheme(isDark: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[isDarkThemeKey] = isDark
        }
    }
    
    suspend fun setSystemTheme(useSystem: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[isSystemThemeKey] = useSystem
        }
    }
} 