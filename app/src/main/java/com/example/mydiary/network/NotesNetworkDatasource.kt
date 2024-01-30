package com.example.mydiary.network

import com.example.mydiary.data.repository.Resources
import com.example.mydiary.network.module.NetworkNotes
import kotlinx.coroutines.flow.Flow

interface NotesNetworkDatasource {
    suspend fun getNotes(userId:String): Flow<Resources<List<NetworkNotes>>>
    suspend fun addNotes(userId: String,notesList:List<NetworkNotes>)
}