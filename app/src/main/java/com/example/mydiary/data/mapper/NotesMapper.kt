package com.example.mydiary.data.mapper

/**
 * Converts the network model to the local model for persisting
 * by the local data source
 */
/*
fun NetworkNotes.asEntity() = NotesEntity(
     userId = userId,
     title = title,
     description = description,
     timestamp  = timestamp.seconds,
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
    timestamp  = Timestamp(timestamp,0),
    colorIndex = colorIndex,
    documentId  = documentId
)


/**
 * Converts the external model to the local entity model
 */
fun Notes.asEntityModel() = NotesEntity(
    userId = userId,
    title = title,
    description = description,
    timestamp = timestamp.seconds, // Access the 'time' property of Date to get the Long timestamp
    colorIndex = colorIndex,
    documentId = documentId
)
*/



