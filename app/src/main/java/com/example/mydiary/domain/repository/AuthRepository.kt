package com.example.mydiary.domain.repository

import com.example.mydiary.di.firebase.FirestoreDatabase
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AuthRepository @Inject constructor(){
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    val currentUser: FirebaseUser? = firebaseAuth.currentUser

    fun hasUser(): Boolean = firebaseAuth.currentUser != null

    fun getUserId(): String = firebaseAuth.currentUser?.uid.orEmpty()


    suspend fun createUser(
        email: String,
        password: String,
        onComplete: (Boolean) -> Unit
    ) = withContext(Dispatchers.IO) {
        val createUserCompleteListener = CreateUserCompleteListener(onComplete)
        Firebase.auth
            .createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(createUserCompleteListener)
            .await()
    }

    suspend fun loginUser(
        email: String,
        password: String,
        onComplete: (Boolean) -> Unit
    ) = withContext(Dispatchers.IO) {

        val loginUserCompleteLister = LoginUserCompleteListener(onComplete)
        Firebase.auth
            .signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(loginUserCompleteLister)
            .await()

    }

    /* suspend fun createUser(user: User): Result<Unit> {
        val userMap = user.toFireStoreUserData()
        return executeRequest {
            firestore
                .userCollection
                .document(user.id.value)
                .set(userMap)
                .await()
        }
    }

    suspend fun updateUser(user: User): Result<Unit> {
        val userMap = user.toFireStoreUserData()
        return executeRequest {
            firestore
                .userCollection
                .document(user.id.value)
                .update(userMap)
                .await()
        }
    }

     suspend fun getUser(): Result<User?> {
        var result: Result<User?> = Result.success(null)

            result = executeRequest {
                val uid = getUserId()
                val userDocument =
                    firestore
                        .userCollection
                        .document(uid)
                        .get()
                        .await()
                val userDataMap = userDocument.data
                userDataMap?.toUser(uid)
            }

        return result
    }

    suspend fun deleteUser(user: User){
        return executeRequest {
            firestore
                .userCollection
                .document(user.id.value)
                .delete()
                .await()
    }
} */

    class CreateUserCompleteListener(private val onComplete: (Boolean) -> Unit) :
        OnCompleteListener<AuthResult> {

        override fun onComplete(task: Task<AuthResult>) {
            if (task.isSuccessful) {
                onComplete.invoke(true)
            } else {
                onComplete.invoke(false)
            }
        }
    }

    class LoginUserCompleteListener(private val onComplete: (Boolean) -> Unit) :
        OnCompleteListener<AuthResult> {
        override fun onComplete(task: Task<AuthResult>) {
            if (task.isSuccessful) {
                onComplete.invoke(true)
            } else {
                onComplete.invoke(false)
            }
        }
    }
}