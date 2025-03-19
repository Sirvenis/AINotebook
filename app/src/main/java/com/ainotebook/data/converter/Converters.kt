package com.ainotebook.data.converter

import androidx.room.TypeConverter
import com.ainotebook.data.model.NoteType
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {
    private val gson = Gson()
    
    @TypeConverter
    fun fromStringList(value: String?): List<String> {
        if (value == null) return emptyList()
        val listType = object : TypeToken<List<String>>() {}.type
        return gson.fromJson(value, listType)
    }
    
    @TypeConverter
    fun toStringList(list: List<String>): String {
        return gson.toJson(list)
    }
    
    @TypeConverter
    fun fromNoteType(value: NoteType): String {
        return value.name
    }
    
    @TypeConverter
    fun toNoteType(value: String): NoteType {
        return enumValueOf(value)
    }
} 