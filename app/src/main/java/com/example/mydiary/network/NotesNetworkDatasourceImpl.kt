package com.example.mydiary.network

import com.example.mydiary.network.module.NetworkNotes

class NotesNetworkDatasourceImpl:NotesNetworkDatasource {
    override suspend fun getNotes(range: String): List<NetworkNotes> {
        TODO("Not yet implemented")
    }

    override suspend fun getSpecificNote(id: String): List<NetworkNotes> {
        TODO("Not yet implemented")
    }
}