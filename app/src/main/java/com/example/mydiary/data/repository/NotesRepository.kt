package com.example.mydiary.data.repository

import com.example.mydiary.data.model.Notes
import kotlinx.coroutines.flow.Flow

interface NotesRepository {
    fun getNotesStream(range:String):Flow<Resources<List<Notes>>>
    suspend fun getSpecificNote(userId: String): Resources<Notes>
}