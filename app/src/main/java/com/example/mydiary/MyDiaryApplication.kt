package com.example.mydiary

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyDiaryApplication: Application() {

    //  val passwordManager: PasswordManager by lazy { PasswordManager(this) }

    //  private val authRepository: AuthRepository by lazy { AuthRepository()}

    //  private val preferencesManager: PreferencesManager by lazy { PreferencesManager(this) }

    //   private val storageRepository: StorageRepository by lazy {StorageRepository()}

    //  private val notesUsecase: NotesUseCase by lazy {NotesUseCase()}

    /* val diaryViewModelFactory by lazy {
        DiaryViewModelFactory(
            this,
            authRepository,
            preferencesManager,


        ) */

    /* val detailViewModelFactory by lazy {
        DetailViewModelFactory(
            storageRepository,
            notesRepository
        )
    } */
}


