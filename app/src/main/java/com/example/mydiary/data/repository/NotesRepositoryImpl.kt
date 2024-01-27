package com.example.mydiary.data.repository

import com.example.mydiary.data.mapper.NotesDomainMapper
import com.example.mydiary.data.mapper.NotesRemoteMapper
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
    private val notesRemoteMapper: NotesRemoteMapper,
    private val notesDomainMapper: NotesDomainMapper
): NotesRepository{

    override fun getNotesStream(): Flow<List<Notes>> = flow {
        notesDao.getNotesEntitiesAsFlow()
            .map { it.map(NotesEntity::asExternalModel) }
    }

    override suspend fun getSpecificNote(userId: String): Resources<Notes> = withContext(ioDispatcher){
        val localModel =  notesDao.getNoteEntityById(userId)
        Resources.Success(notesDomainMapper.mapToDomain(localModel))
    }
}