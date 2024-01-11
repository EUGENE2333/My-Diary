package com.example.mydiary.data.mapper

import com.example.mydiary.data.model.Notes
import com.example.mydiary.database.model.NotesEntity
import com.example.mydiary.network.module.NetworkNotes

/**
 * Converts the network model to the local model for persisting
 * by the local data source
 */
fun NetworkNotes.asEntity() = NotesEntity(
     userId = userId,
     title = title,
     description = description,
     timestamp  = timestamp,
     colorIndex = colorIndex,
     documentId  = documentId
)

/**
 * Converts the local model to the external model for use
 * by layers external to the data layer
 */

fun NotesEntity.asExternalModel() = Notes(
    userId = userId,
    title = title,
    description = description,
    timestamp  = timestamp,
    colorIndex = colorIndex,
    documentId  = documentId
)

