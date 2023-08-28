package com.example.mydiary.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mydiary.MyDiaryApplication
import com.example.mydiary.data.utils.PreferencesManager
import com.example.mydiary.domain.repository.AuthRepository


class DiaryViewModelFactory(
    private val application: MyDiaryApplication,
    private val  authRepository: AuthRepository,
    private val preferencesManager: PreferencesManager,
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DiaryViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DiaryViewModel(
                application,
                authRepository,
                preferencesManager,

                ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
