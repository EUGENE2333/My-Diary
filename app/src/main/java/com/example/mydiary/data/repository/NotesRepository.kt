package com.example.mydiary.data.repository

import com.example.mydiary.data.model.Notes
import kotlinx.coroutines.flow.Flow

interface NotesRepository {
    fun getNotesStream():Flow<List<Notes>>
}