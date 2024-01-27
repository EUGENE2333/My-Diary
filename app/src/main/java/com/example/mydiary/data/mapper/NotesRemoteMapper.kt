package com.example.mydiary.data.mapper

import android.util.Log
import com.example.mydiary.database.model.NotesEntity
import com.example.mydiary.network.module.NetworkNotes
import com.google.firebase.Timestamp

class NotesRemoteMapper():RemoteMapper<NetworkNotes,NotesEntity?> {
    override fun mapFromRemote(remote: NetworkNotes): NotesEntity? {
      return runCatching {
          tryMapFromRemote(netWorkNotes = remote)
      }.getOrElse {
          Log.e("NotesRemoteMapper", "mapping result $it")
          null
      }
    }

    override fun mapToRemote(local: NotesEntity?): NetworkNotes {
        return tryMapToRemote(local = local!!)
    }

    private fun tryMapFromRemote(netWorkNotes: NetworkNotes): NotesEntity {
        return with(netWorkNotes) {
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

    private fun tryMapToRemote(local: NotesEntity): NetworkNotes {
        return with(local) {
            NetworkNotes(
                userId = userId,
                title = title,
                description = description,
                timestamp = Timestamp(timestamp, 0),
                colorIndex = colorIndex,
                documentId = documentId
            )
        }
    }
}