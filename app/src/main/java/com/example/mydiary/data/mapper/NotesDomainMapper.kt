package com.example.mydiary.data.mapper

import com.example.mydiary.data.model.Notes
import com.example.mydiary.database.model.NotesEntity
import com.google.firebase.Timestamp

class NotesDomainMapper(): DomainMapper<NotesEntity?, Notes?>{
    override fun mapToDomain(entity: NotesEntity?): Notes? {
        return kotlin.runCatching {
            tryMapToDomain(local = entity!!)
        }.getOrElse {
            null
        }
    }

    override fun mapFromDomain(domainModel: Notes?): NotesEntity? {
        return kotlin.runCatching {
            tryMapFromDomain(domainModel = domainModel!!)
        }.getOrElse {
            null
        }
    }

    private fun tryMapToDomain(local: NotesEntity): Notes{

        return with(local){
            Notes(
                userId = userId,
                title = title,
                description = description,
                timestamp = Timestamp(timestamp,0),
                colorIndex = colorIndex,
                documentId = documentId
            )
        }
    }

    private fun tryMapFromDomain(domainModel: Notes?): NotesEntity {
        return with(domainModel!!){
            NotesEntity(
                userId = userId,
                title = title,
                description = description,
                timestamp = timestamp.seconds,
                colorIndex = colorIndex,
                documentId = documentId
            )
        }

    }

}

