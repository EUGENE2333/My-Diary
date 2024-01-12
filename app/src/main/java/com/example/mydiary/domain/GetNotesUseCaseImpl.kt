package com.example.mydiary.domain

import com.example.mydiary.data.model.Notes
import com.example.mydiary.data.repository.Resources
import com.example.mydiary.domain.repository.GetNotesUseCase
import kotlinx.coroutines.flow.Flow

class GetNotesUseCaseImpl: GetNotesUseCase {
    override fun getAllNotes(range: String): Flow<Resources<List<Notes>>> {
        TODO("Not yet implemented")
    }

    override suspend fun getSpecificNote(id: String): Resources<Notes> {
        TODO("Not yet implemented")
    }
}