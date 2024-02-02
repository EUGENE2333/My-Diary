package com.example.mydiary.network

import com.example.mydiary.data.repository.Resources
import com.example.mydiary.network.module.NetworkNotes
import com.google.firebase.Timestamp
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

    override fun addNote(
        userId: String,
        title: String,
        description: String,
        timestamp: Timestamp,
        color: Int,
        onComplete: (Boolean) -> Unit
    ) {
        val documentId = notesRef.document().id
        val note = NetworkNotes(
            userId,
            title,
            description,
            timestamp,
            colorIndex = color,
            documentId = documentId
        )
        notesRef
            .document(documentId)
            .set(note)
            .addOnCompleteListener {
                onComplete.invoke(it.isSuccessful)
            }
    }

    override fun updateNote(
        title: String,
        note: String,
        color: Int,
        noteId: String,
        onResult:(Boolean) -> Unit
    ){
        val updateData = hashMapOf<String,Any>(
            "colorIndex" to color,
            "description" to note,
            "title" to title
        )

        notesRef.document(noteId)
            .update(updateData)
            .addOnCompleteListener {
                onResult(it.isSuccessful)
            }
    }

    override suspend fun deleteNote(noteId: String): Boolean = suspendCoroutine { continuation ->
        notesRef.document(noteId)
            .delete()
            .addOnCompleteListener { task ->
                continuation.resume(task.isSuccessful)
            }
    }
}