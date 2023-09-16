package com.example.easynote.domain.repository

import com.example.easynote.common.Response
import com.example.easynote.data.model.Note
import kotlinx.coroutines.flow.Flow
interface EasyNoteRepository {
    fun getAllNotes(): Flow<List<Note>>
    fun getNoteById(id: Long): Flow<Note>
    suspend fun save(note: Note)
    suspend fun update(note: Note)
    suspend fun delete(id: Long)
    fun getFavourites(): Flow<List<Note>>
}