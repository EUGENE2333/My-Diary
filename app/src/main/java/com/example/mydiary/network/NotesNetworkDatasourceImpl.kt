package com.example.mydiary.network

import android.app.AlertDialog
import android.content.Context
import com.example.mydiary.data.repository.Resources
import com.example.mydiary.network.module.NetworkNotes
import com.google.firebase.Timestamp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import com.google.firebase.ktx.Firebase
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
    override fun deleteNote(noteId: String,onComplete:(Boolean) -> Unit){
        notesRef.document(noteId)
            .delete()
            .addOnCompleteListener {
                onComplete.invoke(it.isSuccessful)
            }
    }

    override fun deleteAccount(context: Context) {
        val user = Firebase.auth.currentUser

        // Check if the user is currently authenticated
        if (user != null) {
            // Create an instance of AlertDialog.Builder
            val builder = AlertDialog.Builder(context)

            // Set the dialog title, message, and button texts
            builder.setTitle("Confirm Account Deletion")
            builder.setMessage("Are you sure you want to delete your account? This action is irreversible." +
                    " All your account data will be permanently deleted.")
            builder.setPositiveButton("Delete") { _, _ ->
                // User confirmed, proceed with account deletion
                user.delete()
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            // Account deletion successful
                            // You can perform any additional actions here, such as navigating to a different screen
                         //   navToSignUpPage.invoke()

                        } else {
                            // Account deletion failed
                            // You can handle the error here or display an error message to the user
                        }
                    }
            }
            builder.setNegativeButton("Cancel") { _, _ ->
                // User canceled the deletion, do nothing or show a message
            }

            // Show the dialog
            val dialog = builder.create()
            dialog.show()
        }
    }
}