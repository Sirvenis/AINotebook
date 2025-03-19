package com.ainotebook.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.ainotebook.ui.screens.home.HomeScreen
import com.ainotebook.ui.screens.note.NoteScreen
import com.ainotebook.ui.screens.settings.SettingsScreen

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Note : Screen("note/{noteId}") {
        fun createRoute(noteId: String) = "note/$noteId"
    }
    object Settings : Screen("settings")
}

@Composable
fun AppNavigation(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Screen.Home.route) {
        composable(Screen.Home.route) {
            HomeScreen(navController = navController)
        }
        
        composable(Screen.Note.route) { backStackEntry ->
            val noteId = backStackEntry.arguments?.getString("noteId")
            NoteScreen(
                noteId = noteId ?: "",
                navController = navController
            )
        }
        
        composable(Screen.Settings.route) {
            SettingsScreen(navController = navController)
        }
    }
} 