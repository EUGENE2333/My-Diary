package com.example.mydiary.domain.repository

import com.example.mydiary.data.model.Notes
import com.example.mydiary.data.repository.Resources
import kotlinx.coroutines.flow.Flow

interface GetNotesUseCase {
    fun getAllNotes(): Flow<List<Notes>>

    suspend fun getSpecificNote(id: String): Resources<Notes>
}