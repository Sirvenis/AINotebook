package com.ainotebook

import android.app.Application
import com.ainotebook.data.AppDatabase
import com.ainotebook.data.preferences.ThemePreferences
import com.ainotebook.data.repository.NoteRepository

class AINotebookApplication : Application() {
    val database by lazy { AppDatabase.getDatabase(this) }
    val repository by lazy { NoteRepository(database.noteDao()) }
    val themePreferences by lazy { ThemePreferences(this) }
} 