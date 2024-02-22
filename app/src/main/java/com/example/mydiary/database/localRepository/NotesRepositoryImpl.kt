package com.example.mydiary.database.localRepository
/*
import com.example.mydiary.data.mapper.NotesDomainMapper
import com.example.mydiary.data.mapper.NotesRemoteMapper
import com.example.mydiary.data.model.Notes
import com.example.mydiary.data.repository.Resources
import com.example.mydiary.database.NotesDao
import com.example.mydiary.di.IoDispatcher
import com.example.mydiary.network.NotesNetworkDatasourceImpl
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject

class NotesRepositoryImpl @Inject constructor(
    private val notesDao: NotesDao,
    private val network: NotesNetworkDatasourceImpl,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val notesRemoteMapper: NotesRemoteMapper,
    private val notesDomainMapper: NotesDomainMapper
): NotesRepository {
    override fun user() = network.user()
   override  fun hasUser(): Boolean = network.hasUser()
   override fun getUserId(): String =network.getUserId()


    override fun getNotesStream(): Flow<Resources<List<Notes>>> =
        notesDao.getNotesEntitiesAsFlow()
            .map { entities ->
                val notesList = entities.mapNotNull { notesDomainMapper.mapToDomain(it) }
                Resources.Success(data = notesList)
            }
            .flowOn(ioDispatcher)




  /*  override suspend fun getSpecificNote(documentId: String): Resources<Notes> =
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
        } */





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
            noteEntity?.let {
                notesDao.deleteNotes(it)

            }

            Resources.Success(Unit)

        } catch (e: Exception) {
            Resources.Error(e)
        }
    }

    override suspend fun updateNote(note: Notes): Resources<Unit> = withContext(ioDispatcher) {
        try {
            val noteEntity = notesDomainMapper.mapFromDomain(note)
            noteEntity?.let {
                notesDao.updateNotes(it)

                val networkNote = notesRemoteMapper.mapToRemote(it)
                network.updateNote(
                    title = networkNote.title,
                    note = networkNote.description,
                    color = networkNote.colorIndex,
                    noteId = networkNote.documentId,
                ){}
            }

            Resources.Success(Unit)
        } catch (e: Exception) {
            Resources.Error(e)
        }
    }

   private suspend fun <T> retryIO(block:suspend () -> T): T {
        var curlDelay = 1000L
        while (true){
            try {
                return block()
            }catch (e:IOException){
                e.printStackTrace() // log the error
            }
            delay(curlDelay)
            curlDelay = (curlDelay * 2).coerceAtMost(60000L)
        }
    }
}
*/