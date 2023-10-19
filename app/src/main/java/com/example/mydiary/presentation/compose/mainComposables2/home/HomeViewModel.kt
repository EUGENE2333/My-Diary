package com.example.mydiary.presentation.compose.mainComposables2.home

import android.content.Context
import android.os.Environment
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mydiary.data.model.Notes
import com.example.mydiary.data.repository.Resources
import com.example.mydiary.data.repository.StorageRepository
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileWriter


class HomeViewModel (
    private val repository: StorageRepository =  StorageRepository()
): ViewModel() {
    var homeUiState by mutableStateOf(HomeUiState())
    var notesNull = mutableStateOf(false)

    val user = repository.user()
    val hasUser: Boolean
        get() = repository.hasUser()
    private val userId: String
        get() = repository.getUserId()

    fun loadNotes() {
        if (hasUser) {
            if (userId.isNotBlank()) {
                getUserNotes(userId)
            } else {
                homeUiState = homeUiState.copy(
                    notesList = Resources.Error(
                        throwable = Throwable("User is not logged in")
                    )
                )
            }
        }
    }


    private fun getUserNotes(userId: String) = viewModelScope.launch {
        repository.getUserNotes(userId).collect {
            homeUiState = homeUiState.copy(notesList = it)
        }
    }


    fun deleteNote(noteId: String) = repository.deleteNote(noteId) {
        homeUiState = homeUiState.copy(noteDeletedStatus = it)
    }

    fun signOut() = repository.signOut()

    fun deleteAccount(context: Context, navToSignUpPage: () -> Unit) =
        repository.deleteAccount(context, navToSignUpPage)


    // Function to export notes as a TXT file
    fun exportNotesToFile(notes: List<Notes>) {
        val fileName = "notes.txt"
        val notesText = buildString {
            for (note in notes) {
                append("Title: ${note.title}\nDescription: ${note.description}\n\n")
            }
        }

        val file = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), fileName)
        try {
            val fileWriter = FileWriter(file)
            fileWriter.use {
                it.write(notesText)
            }
        } catch (e: Exception) {
            // Handle any exceptions
        }
    }

}

data class HomeUiState(
    val notesList: Resources<List<Notes>>  = Resources.Loading(),
    val noteDeletedStatus: Boolean = false
)