package com.example.mydiary.network

import com.example.mydiary.network.module.NetworkNotes

interface NotesNetworkDatasource {
    suspend fun getNotes(range: String): List<NetworkNotes>
    suspend fun getSpecificNote(id: String): List<NetworkNotes>
}