package com.example.mydiary.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.Timestamp

@Entity(tableName = "notes")
data class NotesEntity(
    @PrimaryKey
    val userId: Long,
    @ColumnInfo(defaultValue = "")
    val title: String,
    @ColumnInfo(defaultValue = "")
    val description: String,
    val timestamp: Timestamp = Timestamp.now(),
    val colorIndex: Int = 0,
    @ColumnInfo(defaultValue = "")
    val documentId: String
)
