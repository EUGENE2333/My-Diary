package com.example.mydiary.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.mydiary.database.model.NotesEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NotesDao {

    @Query(value = "SELECT * FROM notes")
    fun getNotesEntitiesAsFlow(): Flow<List<NotesEntity>>

   /* @Query(value = "SELECT * FROM notes where userId = :userId")
    suspend fun getNoteEntityById(userId: String): Flow<NotesEntity> */
    @Insert
    suspend fun insertNotes(entities: List<NotesEntity>)
    @Update
    suspend fun updateNotes(note:NotesEntity)
    @Delete
    suspend fun deleteNotes(note:NotesEntity)

}