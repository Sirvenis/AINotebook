package com.ainotebook.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.ainotebook.data.model.Note
import com.ainotebook.data.model.NoteType
import com.ainotebook.data.model.SortOption
import com.ainotebook.data.repository.NoteRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class NoteViewModel(private val repository: NoteRepository) : ViewModel() {
    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()
    
    private val _selectedTag = MutableStateFlow<String?>(null)
    val selectedTag = _selectedTag.asStateFlow()
    
    private val _availableTags = MutableStateFlow<Set<String>>(emptySet())
    val availableTags = _availableTags.asStateFlow()
    
    private val _sortOption = MutableStateFlow(SortOption.CREATED_DESC)
    val sortOption = _sortOption.asStateFlow()
    
    val notes = combine(
        searchQuery,
        selectedTag,
        sortOption
    ) { query, tag, sort ->
        val baseNotes = when {
            tag != null -> repository.getNotesByTag(tag)
            query.isNotEmpty() -> repository.searchNotes(query)
            else -> repository.getAllNotes()
        }
        
        baseNotes.map { notes ->
            when (sort) {
                SortOption.CREATED_DESC -> notes.sortedByDescending { it.createdAt }
                SortOption.CREATED_ASC -> notes.sortedBy { it.createdAt }
                SortOption.MODIFIED_DESC -> notes.sortedByDescending { it.modifiedAt }
                SortOption.MODIFIED_ASC -> notes.sortedBy { it.modifiedAt }
                SortOption.TITLE_ASC -> notes.sortedBy { it.title }
                SortOption.TITLE_DESC -> notes.sortedByDescending { it.title }
                SortOption.TAG_COUNT_DESC -> notes.sortedByDescending { it.tags.size }
                SortOption.TAG_COUNT_ASC -> notes.sortedBy { it.tags.size }
            }
        }
    }.flatMapLatest { it }
    .stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )
    
    init {
        // Update available tags whenever notes change
        viewModelScope.launch {
            notes.collect { notesList ->
                val tags = notesList.flatMap { it.tags }.toSet()
                _availableTags.value = tags
            }
        }
    }
    
    fun setSearchQuery(query: String) {
        _searchQuery.value = query
        _selectedTag.value = null // Clear tag filter when searching
    }
    
    fun setSelectedTag(tag: String?) {
        _selectedTag.value = tag
        _searchQuery.value = "" // Clear search when filtering by tag
    }
    
    fun setSortOption(option: SortOption) {
        _sortOption.value = option
    }
    
    fun createNote(
        title: String,
        content: String,
        type: NoteType = NoteType.TEXT,
        tags: List<String> = emptyList(),
        attachments: List<String> = emptyList()
    ) = viewModelScope.launch {
        repository.createNewNote(title, content, type, tags, attachments)
    }
    
    fun updateNote(note: Note) = viewModelScope.launch {
        repository.insertNote(note)
    }
    
    fun deleteNote(note: Note) {
        viewModelScope.launch {
            repository.deleteNote(note)
        }
    }
    
    suspend fun getNoteById(noteId: String): Note? {
        return repository.getNoteById(noteId)
    }
}

class NoteViewModelFactory(private val repository: NoteRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NoteViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return NoteViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
} 