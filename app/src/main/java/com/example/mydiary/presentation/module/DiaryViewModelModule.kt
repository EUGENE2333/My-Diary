package com.example.mydiary.presentation.module
/**
import com.example.mydiary.MyDiaryApplication
import com.example.mydiary.data.utils.PreferencesManager
import com.example.mydiary.domain.repository.AuthRepository
import com.example.mydiary.presentation.DiaryViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)

object DiaryViewModelModule {

    @Provides
    fun provideDiaryViewModel(
        application: MyDiaryApplication,
        authRepository: AuthRepository,
        preferencesManager: PreferencesManager
    ):DiaryViewModel{
        return DiaryViewModel(
            application,
            authRepository,
            preferencesManager,
        )

    }
}
    **/