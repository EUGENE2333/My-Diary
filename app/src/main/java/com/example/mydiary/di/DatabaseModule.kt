package com.example.mydiary.di

import android.content.Context
import androidx.room.Room
import com.example.mydiary.database.NotesDao
import com.example.mydiary.database.NotesDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@InstallIn
@Module
object DatabaseModule {
  @Singleton
  @Provides
  fun providesDatabase(@ApplicationContext context: Context): NotesDatabase{
    return Room.databaseBuilder(
      context.applicationContext,
      NotesDatabase::class.java,
      "Notes.db"
    ).build()
  }

  @Provides
  fun providesNotesDao(database: NotesDatabase): NotesDao = database.notesDao()
}