package com.example.mydiary.data.repository

import com.example.mydiary.database.NotesDao
import com.example.mydiary.network.NotesNetworkDatasource

class NotesRepository(
    private val notesDao: NotesDao,
    private val network: NotesNetworkDatasource
) {

}