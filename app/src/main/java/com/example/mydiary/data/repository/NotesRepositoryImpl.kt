package com.example.mydiary.data.repository

import com.example.mydiary.data.mapper.NotesDomainMapper
import com.example.mydiary.data.mapper.NotesRemoteMapper
import com.example.mydiary.data.model.Notes
import com.example.mydiary.database.NotesDao
import com.example.mydiary.network.NotesNetworkDatasource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class NotesRepositoryImpl(
    private val notesDao: NotesDao,
    private val network: NotesNetworkDatasource,
    private val ioDispatcher: CoroutineDispatcher,
    private val notesRemoteMapper: NotesRemoteMapper,
    private val notesDomainMapper: NotesDomainMapper
): NotesRepository{

    override fun getNotesStream(): Flow<List<Notes>> = notesDao.getNotesEntitiesAsFlow()
        .map {entities ->
            entities.mapNotNull { notesDomainMapper.mapToDomain(it) }

        }
        .flowOn(ioDispatcher)

    override suspend fun getSpecificNote(userId: String): Resources<Notes> = withContext(ioDispatcher){
        val localModel =  notesDao.getNoteEntityById(userId)
        Resources.Success(notesDomainMapper.mapToDomain(localModel))
    }

    override suspend fun syncNotesFromNetwork(userId: String) {
        TODO("Not yet implemented")
    }
}