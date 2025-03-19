package com.ainotebook.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes")
data class Note(
    @PrimaryKey
    val id: String,
    val title: String,
    val content: String,
    val timestamp: String,
    val type: NoteType,
    val tags: List<String> = emptyList(),
    val attachments: List<String> = emptyList()
)

enum class NoteType {
    TEXT,
    AUDIO,
    IMAGE,
    PDF,
    VIDEO
} 