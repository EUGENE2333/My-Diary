package com.example.mydiary.di

/*
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
*/