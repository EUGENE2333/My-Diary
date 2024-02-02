package com.example.mydiary.database.localRepository

import com.example.mydiary.data.model.Notes
import com.example.mydiary.data.repository.Resources
import kotlinx.coroutines.flow.Flow

interface NotesRepository {
    fun getNotesStream():Flow<List<Notes>>
    suspend fun getSpecificNote(documentId: String): Resources<Notes>
    suspend fun syncNotesFromNetwork(userId: String)
    suspend fun saveNotesToLocal(domainNotes: Notes)
    suspend fun saveNotesToRemote()
    suspend fun deleteNote(note: Notes): Resources<Unit>
    suspend fun updateNote(note: Notes): Resources<Unit>
}