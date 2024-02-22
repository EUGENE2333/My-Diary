package com.example.mydiary.di

import android.app.Application
import android.content.Context
import com.example.mydiary.data.utils.PasswordManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideApplicationContext(application: Application): Context {
        return application.applicationContext
    }

    @Provides
    @Singleton
    fun providesPasswordManager(context: Context): PasswordManager{
        return PasswordManager(context)
    }
}
