package com.example.mydiary.data.repository

import com.example.mydiary.data.mapper.asExternalModel
import com.example.mydiary.data.model.Notes
import com.example.mydiary.database.NotesDao
import com.example.mydiary.database.model.NotesEntity
import com.example.mydiary.network.NotesNetworkDatasource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class NotesRepositoryImpl(
    private val notesDao: NotesDao,
    private val network: NotesNetworkDatasource
): NotesRepository{

    override fun getNotesStream(): Flow<List<Notes>> =
        notesDao.getNotesEntitiesAsFlow()
            .map {
                it.map(NotesEntity::asExternalModel)
            }
}