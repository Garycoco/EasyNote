package com.example.easynote.di

import android.content.Context
import androidx.room.Room
import com.example.easynote.data.local.EasyNoteDao
import com.example.easynote.data.local.EasyNoteDatabase
import com.example.easynote.domain.repository.EasyNoteRepository
import com.example.easynote.domain.use_cases.AddNote
import com.example.easynote.domain.use_cases.DeleteNote
import com.example.easynote.domain.use_cases.GetAllNotes
import com.example.easynote.domain.use_cases.GetFavourites
import com.example.easynote.domain.use_cases.GetNoteById
import com.example.easynote.domain.use_cases.UpdateNote
import com.example.easynote.domain.use_cases.UseCases
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
    @Provides
    fun provideUseCases(
        repository: EasyNoteRepository
    ) = UseCases(
        getNotes = GetAllNotes(repository),
        getFavourites = GetFavourites(repository),
        getNoteById = GetNoteById(repository),
        addNote = AddNote(repository),
        deleteNote = DeleteNote(repository),
        updateNote = UpdateNote(repository)
    )
}