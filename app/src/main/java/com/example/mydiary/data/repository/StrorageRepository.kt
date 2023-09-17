package com.example.mydiary.data.repository

import android.app.AlertDialog
import android.content.Context
import com.example.mydiary.data.model.Notes
import com.google.firebase.Timestamp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.*
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow




const val NOTES_COLLECTION_REF = "notes"


@Suppress("IMPLICIT_CAST_TO_ANY")
class StorageRepository (){

    private val firebaseFirestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    fun user() =  Firebase.auth.currentUser

    fun hasUser(): Boolean = Firebase.auth.currentUser != null

    fun getUserId(): String = Firebase.auth.currentUser?.uid.orEmpty()

    //obtain the notes reference
    private val notesRef: CollectionReference = firebaseFirestore.collection(NOTES_COLLECTION_REF)


    fun getUserNotes(
        userId:String
    ) : Flow<Resources<List<Notes>>> = callbackFlow {

        var snapshotStateListener:ListenerRegistration? = null

        try {
            snapshotStateListener = notesRef
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .whereEqualTo("userId",userId)
                .addSnapshotListener{snapshot,e ->
                     val response = if(snapshot != null){
                         //deserialize the data return so that it can be in form of JSON format
                        val notes = snapshot.toObjects(Notes::class.java)
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

    fun getNote(
        noteId: String,
        onError: (Throwable?) -> Unit,
        onSuccess: (Notes?) -> Unit
    ){
        notesRef
            .document(noteId)
            .get()
            .addOnSuccessListener {
                onSuccess.invoke(it?.toObject(Notes::class.java))
            }
            .addOnFailureListener {result ->
                onError.invoke(result.cause)
            }
    }
    fun addNote(
        userId: String,
        title: String,
        description: String,
        timestamp: Timestamp,
        color:Int = 0,
        onComplete:(Boolean) -> Unit
    ){
        val documentId = notesRef.document().id
        val note = Notes(
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

    fun deleteNote(noteId: String,onComplete:(Boolean) -> Unit){
        notesRef.document(noteId)
            .delete()
            .addOnCompleteListener {
                onComplete.invoke(it.isSuccessful)
            }
    }

    fun updateNote(
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

    fun signOut() = Firebase.auth.signOut()

    fun deleteAccount(context: Context, navToSignUpPage: () -> Unit) {
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
                            navToSignUpPage.invoke()

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



      //class MyFileProvider : FileProvider()

