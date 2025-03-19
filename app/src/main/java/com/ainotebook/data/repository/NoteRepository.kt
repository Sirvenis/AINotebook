package com.ainotebook.data.repository

import com.ainotebook.data.dao.NoteDao
import com.ainotebook.data.model.Note
import com.ainotebook.data.model.NoteType
import kotlinx.coroutines.flow.Flow
import java.util.UUID

class NoteRepository(private val noteDao: NoteDao) {
    fun getAllNotes(): Flow<List<Note>> = noteDao.getAllNotes()
    
    fun searchNotes(query: String): Flow<List<Note>> = noteDao.searchNotes(query)
    
    fun getNotesByTag(tag: String): Flow<List<Note>> = noteDao.getNotesByTag(tag)
    
    suspend fun getNoteById(noteId: String): Note? = noteDao.getNoteById(noteId)
    
    suspend fun insertNote(note: Note) = noteDao.insertNote(note)
    
    suspend fun deleteNote(note: Note) = noteDao.deleteNote(note)
    
    suspend fun createNewNote(
        title: String,
        content: String,
        type: NoteType,
        tags: List<String> = emptyList(),
        attachments: List<String> = emptyList()
    ): Note {
        val note = Note(
            id = UUID.randomUUID().toString(),
            title = title,
            content = content,
            timestamp = System.currentTimeMillis().toString(),
            type = type,
            tags = tags,
            attachments = attachments
        )
        insertNote(note)
        return note
    }
} 