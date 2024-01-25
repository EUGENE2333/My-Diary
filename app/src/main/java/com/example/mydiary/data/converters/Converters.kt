package com.example.mydiary.data.converters

import androidx.room.TypeConverter
import com.google.firebase.Timestamp


class Converters {
    @TypeConverter
    fun fromTimestamp(value: Timestamp?): Long?{
        return value?.toDate()?.time
    }

    @TypeConverter
    fun toTimestamp(value: Long?): Timestamp?{
        return value?.let { Timestamp(it,0) }
    }
}