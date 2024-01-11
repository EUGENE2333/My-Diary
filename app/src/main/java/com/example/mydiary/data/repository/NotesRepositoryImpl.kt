package com.example.mydiary.data.repository

import com.example.mydiary.data.mapper.asEntity
import com.example.mydiary.data.mapper.asExternalModel
import com.example.mydiary.data.model.Notes
import com.example.mydiary.database.NotesDao
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

    override fun getNotesStream(range:String): Flow<Resources<List<Notes>>> = flow {
        val notesResults = withContext(ioDispatcher){
          executeRequest {
           val localModels = network.getNotes(range).map {it.asEntity()}
              notesDao.insertNotes(localModels)
              localModels.map { it.asExternalModel() }
          }
        }
        emit(notesResults)
    }

    override suspend fun getSpecificNote(userId: String): Resources<Notes> = withContext(ioDispatcher){
        val localModel =  notesDao.getNoteEntityById(userId)
        Resources.Success(localModel.asExternalModel())
    }
}