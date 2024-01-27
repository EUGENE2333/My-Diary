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
        TODO("Not yet implemented")
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

}