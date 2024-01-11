package com.example.mydiary.data.repository

import com.example.mydiary.data.mapper.asExternalModel
import com.example.mydiary.data.model.Notes
import com.example.mydiary.database.NotesDao
import com.example.mydiary.database.model.NotesEntity
import com.example.mydiary.network.NotesNetworkDatasource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class NotesRepositoryImpl(
    private val notesDao: NotesDao,
    private val network: NotesNetworkDatasource,
    private val ioDispatcher: CoroutineDispatcher,
): NotesRepository{

    override fun getNotesStream(): Flow<List<Notes>> = flow {
        val notesResults = withContext(ioDispatcher){

        }
        notesDao.getNotesEntitiesAsFlow()
            .map {
                it.map(NotesEntity::asExternalModel)
            }
    }
}