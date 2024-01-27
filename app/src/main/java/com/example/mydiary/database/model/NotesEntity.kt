package com.example.mydiary.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes")
data class NotesEntity(
    @PrimaryKey
    val userId: String = "",
    @ColumnInfo(defaultValue = "")
    val title: String = "",
    @ColumnInfo(defaultValue = "")
    val description: String = "",
    val timestamp: Long = 0,
    val colorIndex: Int = 0,
    @ColumnInfo(defaultValue = "")
    val documentId: String = ""
)
