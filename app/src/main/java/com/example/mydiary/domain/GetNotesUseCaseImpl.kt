package com.example.mydiary.domain

import com.example.mydiary.data.model.Notes
import com.example.mydiary.data.repository.Resources
import com.example.mydiary.database.localRepository.NotesRepository
import com.example.mydiary.domain.repository.GetNotesUseCase
import kotlinx.coroutines.flow.Flow

class GetNotesUseCaseImpl(  private val notesRepository: NotesRepository): GetNotesUseCase {
    override fun getAllNotes(): Flow<List<Notes>> {
     return notesRepository.getNotesStream()
    }

    override suspend fun getSpecificNote(id: String): Resources<Notes> {
       return notesRepository.getSpecificNote(id)
    }
}