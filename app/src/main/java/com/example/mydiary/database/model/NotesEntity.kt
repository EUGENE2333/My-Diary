package com.example.mydiary.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes")
data class NotesEntity(
    @PrimaryKey
    var userId: String = "",
    @ColumnInfo(defaultValue = "")
    var title: String = "",
    @ColumnInfo(defaultValue = "")
    var description: String = "",
    var timestamp: Long = 0,
    var colorIndex: Int = 0,
    @ColumnInfo(defaultValue = "")
    var documentId: String = ""
)
