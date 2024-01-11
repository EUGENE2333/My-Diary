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
                        val notes = snapshot.toObjects(NetworkNotes::class.java)
                        Resources.Success(data = notes)
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

    override suspend fun getSpecificNote(id: String): List<NetworkNotes> {
        TODO("Not yet implemented")
    }
}