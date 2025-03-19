package com.ainotebook.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ainotebook.AINotebookApplication
import com.ainotebook.ui.viewmodel.ThemeViewModel
import com.ainotebook.ui.viewmodel.ThemeViewModelFactory

private val DarkColorPalette = darkColors(
    primary = Color(0xFF6200EE),
    primaryVariant = Color(0xFF3700B3),
    secondary = Color(0xFF03DAC6),
    background = Color(0xFF121212),
    surface = Color(0xFF121212),
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.White,
    onSurface = Color.White
)

private val LightColorPalette = lightColors(
    primary = Color(0xFF6200EE),
    primaryVariant = Color(0xFF3700B3),
    secondary = Color(0xFF03DAC6),
    background = Color.White,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black
)

@Composable
fun AINotebookTheme(
    content: @Composable () -> Unit
) {
    val context = LocalContext.current
    val themeViewModel: ThemeViewModel = viewModel(
        factory = ThemeViewModelFactory(
            (context.applicationContext as AINotebookApplication).themePreferences
        )
    )
    
    val isSystemTheme by themeViewModel.isSystemTheme.collectAsState(initial = true)
    val isDarkTheme by themeViewModel.isDarkTheme.collectAsState(initial = false)
    
    val colors = when {
        isSystemTheme -> if (isSystemInDarkTheme()) DarkColorPalette else LightColorPalette
        isDarkTheme -> DarkColorPalette
        else -> LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        content = content
    )
} 