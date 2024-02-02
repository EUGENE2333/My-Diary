package com.example.mydiary.database.localRepository

import com.example.mydiary.data.mapper.NotesDomainMapper
import com.example.mydiary.data.mapper.NotesRemoteMapper
import com.example.mydiary.data.model.Notes
import com.example.mydiary.data.repository.Resources
import com.example.mydiary.database.NotesDao
import com.example.mydiary.database.model.NotesEntity
import com.example.mydiary.network.NotesNetworkDatasource
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class NotesRepositoryImpl(
    private val notesDao: NotesDao,
    private val network: NotesNetworkDatasource,
    private val ioDispatcher: CoroutineDispatcher,
    private val notesRemoteMapper: NotesRemoteMapper,
    private val notesDomainMapper: NotesDomainMapper
): NotesRepository {
    fun user() = network.user()
    override fun getNotesStream(): Flow<List<Notes>> = notesDao.getNotesEntitiesAsFlow()
        .map { entities ->
            entities.mapNotNull { notesDomainMapper.mapToDomain(it) }

        }
        .flowOn(ioDispatcher)

    override suspend fun getSpecificNote(documentId: String): Resources<Notes> =
        withContext(ioDispatcher) {
            try {
                val localModel: NotesEntity? = notesDao.getNoteEntityById(documentId)

                return@withContext if (localModel != null) {
                    Resources.Success(notesDomainMapper.mapToDomain(localModel))
                } else {
                    Resources.Error(Exception("Note not found"))
                }
            } catch (e: Exception) {
                Resources.Error(e)
            }
        }





    override suspend fun syncNotesFromNetwork(userId: String) {
        network.getNotes(userId)
            .collect {
                when (it) {
                    is Resources.Success -> {
                        val networkNotesList = it.data
                        val localNotesList = networkNotesList?.mapNotNull {networkNotes ->
                            notesRemoteMapper.mapFromRemote(networkNotes)
                        }
                        if (localNotesList != null) {
                            notesDao.insertNotes(localNotesList)
                        }
                    }

                    is Resources.Error -> {

                    }

                    is Resources.Loading -> {

                    }
                }
            }
    }

    override suspend fun saveNotesToLocal(domainNotes: Notes) {
        withContext(ioDispatcher) {
          val noteEntity =   notesDomainMapper.mapFromDomain(domainNotes)
            noteEntity?.let { notesDao.insertNotes(listOf(it)) }
        }
    }

    override suspend fun saveNotesToRemote() {
        val localNotesList = notesDao.getNotesEntitiesAsFlow().firstOrNull() ?: emptyList()
        val networkNotesList = localNotesList.map { notesRemoteMapper.mapToRemote(it) }
        networkNotesList.forEach { networkNote ->
            network.addNote(
                userId = networkNote.userId,
                title = networkNote.title,
                description = networkNote.description,
                timestamp = networkNote.timestamp,
                color = networkNote.colorIndex,
                onComplete = {success ->
                    if (success)
                    {
                        // Note added successfully to remote
                    }else {
                        // Handle failure to add note to remote
                    }
                }
            )
        }
    }

    override suspend fun deleteNote(note: Notes): Resources<Unit> = withContext(ioDispatcher) {
        try {
            val noteEntity = notesDomainMapper.mapFromDomain(note)
            noteEntity?.let { notesDao.deleteNotes(it) }

            Resources.Success(Unit)

        } catch (e: Exception) {
            Resources.Error(e)
        }
    }

    override suspend fun updateNote(note: Notes): Resources<Unit> = withContext(ioDispatcher) {
        try {
            val noteEntity = notesDomainMapper.mapFromDomain(note)
            noteEntity?.let { notesDao.updateNotes(it) }

            Resources.Success(Unit)
        } catch (e: Exception) {
            Resources.Error(e)
        }
    }
}