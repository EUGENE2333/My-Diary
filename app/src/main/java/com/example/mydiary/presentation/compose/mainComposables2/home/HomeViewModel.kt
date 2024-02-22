package com.example.mydiary.presentation.compose.mainComposables2.home

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mydiary.data.model.Notes
import com.example.mydiary.data.repository.Resources
import com.example.mydiary.data.repository.StorageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: StorageRepository,
   // private val notesRepositoryImpl: NotesRepositoryImpl
): ViewModel() {
    var homeUiState by mutableStateOf(HomeUiState())
    var notesNull = mutableStateOf(false)

    val user =/* notesRepositoryImpl.user() */ repository.user()
    val hasUser: Boolean
        get() = /*notesRepositoryImpl.hasUser() */  repository.hasUser()
    private val userId: String
        get() = /*notesRepositoryImpl.getUserId() */    repository.getUserId()

    suspend fun loadNotes() {
        if (hasUser) {
            if (userId.isNotBlank()) {
            /*    notesRepositoryImpl.getNotesStream().collect{
                    homeUiState = homeUiState.copy(notesList = it)
                } */
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

}

data class HomeUiState(
    val notesList: Resources<List<Notes>>  = Resources.Loading(),
    val noteDeletedStatus: Boolean = false
)