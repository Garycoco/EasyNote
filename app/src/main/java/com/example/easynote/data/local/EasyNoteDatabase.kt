package com.example.easynote.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.easynote.data.local.converter.DateConverter
import com.example.easynote.data.model.Note

@TypeConverters(value = [DateConverter::class])
@Database(entities = [Note::class], version = 1, exportSchema = false)
abstract class EasyNoteDatabase: RoomDatabase() {
    abstract val noteDao: EasyNoteDao
}