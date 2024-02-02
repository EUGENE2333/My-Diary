package com.example.mydiary.network

import com.example.mydiary.data.repository.Resources
import com.example.mydiary.network.module.NetworkNotes
import com.google.firebase.Timestamp
import kotlinx.coroutines.flow.Flow

interface NotesNetworkDatasource {
    suspend fun getNotes(userId:String): Flow<Resources<List<NetworkNotes>>>
    fun addNote(
        userId: String,
        title: String,
        description: String,
        timestamp: Timestamp,
        color: Int,
        onComplete: (Boolean) -> Unit
    )
    suspend fun updateNote(
        noteId:String,
        title: String,
        description: String,
        colorIndex: Int
    ):Boolean

    suspend fun deleteNote(noteId: String): Boolean
}