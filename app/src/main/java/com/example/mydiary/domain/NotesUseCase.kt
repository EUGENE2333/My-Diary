package com.example.mydiary.domain

import com.example.mydiary.data.model.Notes
import com.example.mydiary.data.repository.Resources
import com.example.mydiary.database.localRepository.NotesRepository
import kotlinx.coroutines.flow.Flow

class NotesUseCase(
    private val notesRepository: NotesRepository
){
    fun getAllNotes(): Flow<List<Notes>> = notesRepository.getNotesStream()

   suspend fun getSpecificNote(documentId: String): Resources<Notes> = notesRepository.getSpecificNote(documentId)
}