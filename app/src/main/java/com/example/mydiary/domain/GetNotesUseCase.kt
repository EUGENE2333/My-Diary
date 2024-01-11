package com.example.mydiary.domain

import com.example.mydiary.data.model.Notes
import com.example.mydiary.data.repository.NotesRepository
import com.example.mydiary.data.repository.Resources
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

abstract class GetNotesUseCase(
    private val notesRepository: NotesRepository
){
    operator fun invoke(range: String): Flow<Resources<List<Notes>>> =
        notesRepository.getNotesStream(range)

    fun getSpecificNote(id: String): Flow<Resources<Notes>> =  flow {
        val data = notesRepository.getSpecificNote(id)
        emit(data)
    }
}