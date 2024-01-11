package com.example.mydiary.domain.repository

import com.example.mydiary.data.model.Notes
import com.example.mydiary.data.repository.NotesRepository
import com.example.mydiary.data.repository.Resources
import kotlinx.coroutines.flow.Flow

abstract class GetNotesUseCase(
    private val notesRepository: NotesRepository
){
    operator fun invoke(range: String): Flow<Resources<List<Notes>>> =
        notesRepository.getNotesStream(range)

}