package com.example.easynote.di

import com.example.easynote.data.repository.EasyNoteRepositoryImpl
import com.example.easynote.domain.repository.EasyNoteRepository
import com.example.easynote.domain.use_cases.AddNote
import com.example.easynote.domain.use_cases.DeleteNote
import com.example.easynote.domain.use_cases.GetAllNotes
import com.example.easynote.domain.use_cases.GetFavourites
import com.example.easynote.domain.use_cases.GetNoteById
import com.example.easynote.domain.use_cases.UpdateNote
import com.example.easynote.domain.use_cases.UseCases
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindRepository(repositoryImpl: EasyNoteRepositoryImpl): EasyNoteRepository
}
