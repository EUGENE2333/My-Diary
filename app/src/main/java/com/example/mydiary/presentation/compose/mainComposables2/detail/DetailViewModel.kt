package com.example.mydiary.presentation.compose.mainComposables2.detail

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mydiary.data.model.Notes
import com.example.mydiary.data.repository.StorageRepository
import com.example.mydiary.domain.NotesUseCase
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.launch

class DetailViewModel(
    private val repository: StorageRepository = StorageRepository(),

    private val notesUsecase: NotesUseCase
): ViewModel(){


    var detailUiState by mutableStateOf(DetailUiState())
    private set

    private val hasUser: Boolean
    get() = repository.hasUser()

     val user: FirebaseUser?
    get() = repository.user()

   fun onTitleChange( title: String){
       detailUiState = detailUiState.copy(title = title)
   }
    fun onNoteChange( note: String){
        detailUiState = detailUiState.copy(note = note)
    }
    fun onColorChange( colorIndex: Int){
        detailUiState = detailUiState.copy(colorIndex = colorIndex)
    }

    fun addNote(){

        if(hasUser){
         /*   repository.addNote(
                userId = user!!.uid,
                title = detailUiState.title,
                description = detailUiState.note,
                timestamp = Timestamp.now(),
                color = detailUiState.colorIndex
            ){
                detailUiState = detailUiState.copy(noteAddedStatus = it)
            } */

            viewModelScope.launch {
                notesUsecase.addNote(
                    userId = user!!.uid,
                    title = detailUiState.title,
                    description = detailUiState.note,
                    timestamp = Timestamp.now(),
                    color = detailUiState.colorIndex
                ) {
                    detailUiState = detailUiState.copy(noteAddedStatus = it)
                }
            }
        }
    }

    fun setEditFields(note: Notes){
        detailUiState = detailUiState.copy(
            colorIndex = note.colorIndex,
            title = note.title,
            note = note.description
        )

    }

    fun getNote(noteId: String){
        repository.getNote(
            noteId = noteId,
            onError = {
                 Log.e("DetailViewModel:","${it?.message.toString()}")
            },
        ){
            detailUiState = detailUiState.copy(selectedNote = it)
            detailUiState.selectedNote?.let { it1 -> setEditFields(it1) }
        }
    }

    fun updateNote(noteId: String){
        repository.updateNote(
            title = detailUiState.title,
            note = detailUiState.note,
            color = detailUiState.colorIndex,
            noteId = noteId,
            ){
            detailUiState = detailUiState.copy(updateNoteStatus = it)
        }
    }

    fun resetNoteAddedStatus(){
        detailUiState = detailUiState.copy(noteAddedStatus = false, updateNoteStatus = false)
    }

    fun resetState(){
        detailUiState = DetailUiState()
    }
}

data class DetailUiState(
    val colorIndex: Int = 0,
    val title: String = "",
    val note: String = "",
    val noteAddedStatus:Boolean = false,
    val updateNoteStatus:Boolean = false,
    val selectedNote: Notes? = null
)