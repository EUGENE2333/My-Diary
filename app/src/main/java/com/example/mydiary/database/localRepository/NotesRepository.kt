package com.example.mydiary.database.localRepository

import com.example.mydiary.data.model.Notes
import com.example.mydiary.data.repository.Resources
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow

interface NotesRepository {
    fun user():FirebaseUser?
    fun hasUser():Boolean
    fun getUserId():String
    fun getNotesStream():Flow<List<Notes>>
    suspend fun getSpecificNote(documentId: String): Resources<Notes>
    suspend fun syncNotesFromNetwork(userId: String)
    suspend fun saveNotesToLocal(domainNotes: Notes)
    suspend fun saveNotesToRemote()
    suspend fun deleteNote(note: Notes): Resources<Unit>
    suspend fun updateNote(note: Notes): Resources<Unit>
}