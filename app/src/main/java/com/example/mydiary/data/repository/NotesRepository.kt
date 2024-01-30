package com.example.mydiary.data.repository

import com.example.mydiary.data.model.Notes
import kotlinx.coroutines.flow.Flow

interface NotesRepository {
    fun getNotesStream():Flow<List<Notes>>
    suspend fun getSpecificNote(userId: String): Resources<Notes>
    suspend fun syncNotesFromNetwork(userId: String)
    suspend fun saveNotesToLocal(domainNotes: Notes)
    suspend fun saveNotesToRemote(userId:String)
    suspend fun deleteNote(note: Notes): Resources<Unit>
}