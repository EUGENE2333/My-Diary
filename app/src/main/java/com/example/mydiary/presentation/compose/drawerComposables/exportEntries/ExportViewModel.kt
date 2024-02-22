package com.example.mydiary.presentation.compose.drawerComposables.exportEntries

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mydiary.data.model.Notes
import com.example.mydiary.data.repository.Resources
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExportViewModel @Inject constructor(): ViewModel() {

    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    @JvmName("getUserId1")
    fun getUserId(): String = Firebase.auth.currentUser?.uid.orEmpty()

    private val userId: String
        get() = getUserId()

    private val _exportUiState = MutableStateFlow(ExportUiState())
    val exportUiState: StateFlow<ExportUiState> = _exportUiState
    init {
        getNotes(userId)
    }
    private fun getNotes(userId: String) {
        firestore.collection("notes")
            .whereEqualTo("userId", userId)
            .get()
            .addOnSuccessListener {
                val notes = it.toObjects(Notes::class.java)
                viewModelScope.launch {
                    _exportUiState.value =
                        _exportUiState.value.copy(notesList = Resources.Success(notes))
                }
            }
            .addOnFailureListener { exception ->
                viewModelScope.launch {
                    _exportUiState.value = _exportUiState.value.copy(
                        notesList = Resources.Error(throwable = exception)
                    )
                }
            }
    }

}

data class ExportUiState(
    val notesList: Resources<List<Notes>>  = Resources.Loading()
)