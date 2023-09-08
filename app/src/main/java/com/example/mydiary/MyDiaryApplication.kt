package com.example.mydiary

import android.app.Application
import com.example.mydiary.data.utils.PasswordManager
import com.example.mydiary.data.utils.PreferencesManager
import com.example.mydiary.domain.repository.AuthRepository
import com.example.mydiary.presentation.DiaryViewModelFactory
import com.google.firebase.FirebaseApp


class MyDiaryApplication: Application() {


    val passwordManager: PasswordManager by lazy { PasswordManager(this) }

    private val authRepository: AuthRepository by lazy { AuthRepository()}
  //  private val storageRepository: StorageRepository by lazy { StorageRepository()}
    private val preferencesManager: PreferencesManager by lazy { PreferencesManager(this) }

    val diaryViewModelFactory by lazy {
        DiaryViewModelFactory(
            this,
            authRepository,
            preferencesManager,


        )
    }
/*
    val homeViewModelFactory by lazy{
        HomeViewModelFactory(
            storageRepository
        )
    }

    val detailViewModelFactory by lazy{
        DetailViewModelFactory(
            storageRepository
        )
    }

 */


}


