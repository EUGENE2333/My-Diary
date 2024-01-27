package com.example.mydiary.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.mydiary.data.converters.Converters
import com.example.mydiary.database.model.NotesEntity

@Database(entities = [NotesEntity::class], version = 1)
@TypeConverters(Converters::class)
abstract class NotesDatabase : RoomDatabase() {

    abstract fun notesDao(): NotesDao

    companion object {
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
    }
}