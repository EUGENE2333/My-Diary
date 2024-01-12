package com.example.mydiary.domain

import com.example.mydiary.data.repository.NotesRepository
import com.example.mydiary.domain.repository.GetNotesUseCase

class GetNotesUseCaseImpl(notesRepository: NotesRepository) : GetNotesUseCase(notesRepository) {

}