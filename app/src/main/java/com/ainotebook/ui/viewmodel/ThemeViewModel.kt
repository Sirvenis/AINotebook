package com.ainotebook.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.ainotebook.data.preferences.ThemePreferences
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ThemeViewModel(private val preferences: ThemePreferences) : ViewModel() {
    val isDarkTheme = preferences.isDarkTheme
    val isSystemTheme = preferences.isSystemTheme
    
    fun setDarkTheme(isDark: Boolean) {
        viewModelScope.launch {
            preferences.setDarkTheme(isDark)
        }
    }
    
    fun setSystemTheme(useSystem: Boolean) {
        viewModelScope.launch {
            preferences.setSystemTheme(useSystem)
        }
    }
}

class ThemeViewModelFactory(private val preferences: ThemePreferences) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ThemeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ThemeViewModel(preferences) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
} 