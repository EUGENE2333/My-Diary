package com.example.mydiary.network

import com.example.mydiary.data.model.Notes
import com.example.mydiary.data.repository.Resources
import com.example.mydiary.network.module.NetworkNotes
import kotlinx.coroutines.flow.Flow

interface NotesNetworkDatasource {
    suspend fun getNotes(userId:String): Flow<Resources<List<NetworkNotes>>>
}