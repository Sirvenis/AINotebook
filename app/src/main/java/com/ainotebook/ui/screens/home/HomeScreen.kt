package com.ainotebook.ui.screens.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.ainotebook.AINotebookApplication
import com.ainotebook.data.model.SortOption
import com.ainotebook.ui.components.NoteList
import com.ainotebook.ui.components.TagChip
import com.ainotebook.ui.navigation.Screen
import com.ainotebook.ui.viewmodel.NoteViewModel
import com.ainotebook.ui.viewmodel.NoteViewModelFactory

@Composable
fun HomeScreen(navController: NavController) {
    val context = LocalContext.current
    val noteViewModel: NoteViewModel = viewModel(
        factory = NoteViewModelFactory(
            (context.applicationContext as AINotebookApplication).repository
        )
    )
    
    val notes by noteViewModel.notes.collectAsState()
    val availableTags by noteViewModel.availableTags.collectAsState()
    val selectedTag by noteViewModel.selectedTag.collectAsState()
    val currentSortOption by noteViewModel.sortOption.collectAsState()
    var searchQuery by remember { mutableStateOf("") }
    var showSortMenu by remember { mutableStateOf(false) }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("AI Notebook") },
                navigationIcon = {
                    IconButton(onClick = { /* Open drawer */ }) {
                        Icon(Icons.Filled.Menu, contentDescription = "Menu")
                    }
                },
                actions = {
                    Box {
                        IconButton(onClick = { showSortMenu = true }) {
                            Icon(Icons.Filled.Sort, contentDescription = "Sort")
                        }
                        DropdownMenu(
                            expanded = showSortMenu,
                            onDismissRequest = { showSortMenu = false }
                        ) {
                            SortOption.values().forEach { option ->
                                DropdownMenuItem(
                                    onClick = {
                                        noteViewModel.setSortOption(option)
                                        showSortMenu = false
                                    }
                                ) {
                                    if (option == currentSortOption) {
                                        Icon(
                                            Icons.Filled.Check,
                                            contentDescription = null,
                                            modifier = Modifier.size(20.dp)
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                    } else {
                                        Spacer(modifier = Modifier.width(28.dp))
                                    }
                                    Text(option.displayName)
                                }
                            }
                        }
                    }
                    IconButton(onClick = { /* Open settings */ }) {
                        Icon(Icons.Filled.Settings, contentDescription = "Settings")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate(Screen.Note.createRoute("new")) }
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Add Note")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Search Bar
            TextField(
                value = searchQuery,
                onValueChange = { 
                    searchQuery = it
                    noteViewModel.setSearchQuery(it)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                placeholder = { Text("Search notes...") },
                leadingIcon = {
                    Icon(Icons.Filled.Search, contentDescription = "Search")
                }
            )
            
            // Tags Filter
            if (availableTags.isNotEmpty()) {
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    items(availableTags.toList()) { tag ->
                        TagChip(
                            tag = tag,
                            selected = tag == selectedTag,
                            onSelect = {
                                if (tag == selectedTag) {
                                    noteViewModel.setSelectedTag(null)
                                } else {
                                    noteViewModel.setSelectedTag(tag)
                                }
                            }
                        )
                    }
                }
            }

            // Note List
            NoteList(
                notes = notes,
                onNoteClick = { noteId ->
                    navController.navigate(Screen.Note.createRoute(noteId))
                },
                onDeleteNote = { note ->
                    noteViewModel.deleteNote(note)
                }
            )
        }
    }
} 