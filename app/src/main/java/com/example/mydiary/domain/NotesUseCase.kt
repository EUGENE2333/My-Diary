package com.example.mydiary.domain

import com.example.mydiary.data.model.Notes
import com.example.mydiary.data.repository.Resources
import com.example.mydiary.database.localRepository.NotesRepository
import com.google.firebase.Timestamp
import kotlinx.coroutines.flow.Flow

class NotesUseCase(
    private val notesRepository: NotesRepository
){
    fun getAllNotes(): Flow<Resources<List<Notes>>> = notesRepository.getNotesStream()

   suspend fun getSpecificNote(documentId: String): Resources<Notes> = notesRepository.getSpecificNote(documentId)

    suspend fun addNote(
        userId: String,
        title: String,
        description: String,
        timestamp: Timestamp,
        color: Int = 0,
        onComplete:(Boolean) -> Unit
    ){
        try {
            val localNote = Notes(
                userId = userId,
                title = title,
                description = description,
                timestamp = timestamp,
                colorIndex = color,
            )
            notesRepository.saveNotesToLocal(localNote)

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}