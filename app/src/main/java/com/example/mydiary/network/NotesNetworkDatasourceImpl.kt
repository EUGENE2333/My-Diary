package com.example.mydiary.network

import com.example.mydiary.data.repository.Resources
import com.example.mydiary.network.module.NetworkNotes
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


const val NOTES_COLLECTION_REF = "notes"
class NotesNetworkDatasourceImpl:NotesNetworkDatasource {


    private val firebaseFirestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    //obtain the notes reference
    private val notesRef: CollectionReference = firebaseFirestore.collection(NOTES_COLLECTION_REF)
    override suspend fun getNotes(userId:String): Flow<Resources<List<NetworkNotes>>> = callbackFlow {

        var snapshotStateListener: ListenerRegistration? = null

        try {
            snapshotStateListener = notesRef
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .whereEqualTo("userId",userId)
                .addSnapshotListener{snapshot,e ->
                    val response = if(snapshot != null){
                        //deserialize the data return so that it can be in form of JSON format
                        val networkNotes = snapshot.toObjects(NetworkNotes::class.java)
                        Resources.Success(data = networkNotes)
                    }else{
                        e?.cause?.let { Resources.Error(throwable = it) }
                    }
                    response?.let { trySend(it) }
                }

        }catch (e:Exception){

            trySend(Resources.Error(e.cause))
            e.printStackTrace()

        }

        awaitClose {
            snapshotStateListener?.remove()
        }
    }

    override suspend fun addNotes(userId: String, notesList: List<NetworkNotes>) {
        val batch = firebaseFirestore.batch()

        for (note in notesList){
            val documentId = notesRef.document().id
            val noteMap = mapOf(
                "userId" to userId,
                "title" to note.title,
                "description" to note.description,
                "timestamp" to note.timestamp,
                "colorIndex" to note.colorIndex,
                "documentId" to note.documentId
            )

            val newDocumentRef = notesRef.document(documentId)
            batch.set(newDocumentRef,noteMap)
        }
        batch.commit()
            .addOnCompleteListener {
                //Handle completion
                // add logging or additional error handling here
            }
    }

    override suspend fun updateNote(
        noteId: String,
        title: String,
        description: String,
        colorIndex: Int
    ): Boolean  = suspendCoroutine { continuation ->
        val updateData = mapOf(
            "title" to title,
            "description" to description,
            "colorIndex" to colorIndex
        )

        notesRef.document(noteId)
            .update(updateData)
            .addOnCompleteListener { task ->
                continuation.resume(task.isSuccessful)
            }
    }

    override suspend fun deleteNote(noteId: String): Boolean {
        TODO("Not yet implemented")
    }
}