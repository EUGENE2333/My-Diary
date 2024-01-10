package com.example.mydiary.database.model

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface NotesDao {

    @Query(value = "SELECT * FROM notes")
    fun getNotesEntitiesAsFlow(): Flow<List<NotesEntity>>
    @Insert
    suspend fun insertNotes(entities: List<NotesEntity>)
    @Update
    suspend fun updateNotes(note:NotesEntity)
    @Delete
    suspend fun deleteNotes(note:NotesEntity)

}