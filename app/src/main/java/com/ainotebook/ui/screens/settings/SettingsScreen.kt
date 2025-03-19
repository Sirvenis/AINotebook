package com.ainotebook.ui.screens.settings

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.ainotebook.AINotebookApplication
import com.ainotebook.ui.viewmodel.ThemeViewModel
import com.ainotebook.ui.viewmodel.ThemeViewModelFactory

@Composable
fun SettingsScreen(navController: NavController) {
    val context = LocalContext.current
    val themeViewModel: ThemeViewModel = viewModel(
        factory = ThemeViewModelFactory(
            (context.applicationContext as AINotebookApplication).themePreferences
        )
    )
    
    val isSystemTheme by themeViewModel.isSystemTheme.collectAsState(initial = true)
    val isDarkTheme by themeViewModel.isDarkTheme.collectAsState(initial = false)
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            // Theme Settings
            Text(
                text = "Appearance",
                style = MaterialTheme.typography.h6,
                modifier = Modifier.padding(vertical = 8.dp)
            )
            
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Use System Theme")
                Switch(
                    checked = isSystemTheme,
                    onCheckedChange = { themeViewModel.setSystemTheme(it) }
                )
            }
            
            if (!isSystemTheme) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Dark Mode")
                    Switch(
                        checked = isDarkTheme,
                        onCheckedChange = { themeViewModel.setDarkTheme(it) }
                    )
                }
            }
            
            Divider()
            
            // Other settings...
            // (Previous settings content remains unchanged)
        }
    }
} 