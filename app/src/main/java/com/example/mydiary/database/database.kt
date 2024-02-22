package com.example.mydiary.database

/*
@Database(entities = [NotesEntity::class], version = 1)
@TypeConverters(Converters::class)
abstract class NotesDatabase : RoomDatabase() {

    abstract fun notesDao(): NotesDao


  /*  companion object {
        private var instance: NotesDatabase? = null

        @Synchronized
        fun getInstance(ctx: Context): NotesDatabase {
            if (instance == null)
                instance = Room.databaseBuilder(
                    ctx.applicationContext, NotesDatabase::class.java,
                    "note_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()

            return instance!!

        }
    } */
}
*/