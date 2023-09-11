package com.example.easynote.di

import android.content.Context
import androidx.room.Room
import com.example.easynote.data.local.EasyNoteDao
import com.example.easynote.data.local.EasyNoteDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): EasyNoteDatabase =
        Room.databaseBuilder(context, EasyNoteDatabase::class.java, "notes_db")
            .build()

    @Provides
    @Singleton
    fun provideNoteDao(database: EasyNoteDatabase): EasyNoteDao =
        database.noteDao
}