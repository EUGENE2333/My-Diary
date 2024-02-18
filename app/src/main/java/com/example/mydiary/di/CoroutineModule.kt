package com.example.mydiary.di

import dagger.Module
import dagger.hilt.InstallIn
import javax.inject.Qualifier

@InstallIn
@Module
object CoroutineModule {

}

@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class DefaultDispatcher

@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class IoDispatcher

@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class MainDispatcher