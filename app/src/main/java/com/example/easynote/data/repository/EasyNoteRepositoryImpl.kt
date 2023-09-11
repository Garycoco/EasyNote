package com.example.easynote.data.repository

import com.example.easynote.data.local.EasyNoteDao
import com.example.easynote.data.model.Note
import com.example.easynote.domain.repository.EasyNoteRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class EasyNoteRepositoryImpl @Inject constructor(private val noteDao: EasyNoteDao) : EasyNoteRepository {
    override fun getAllNotes(): Flow<List<Note>> {
        return noteDao.getAllNotes()
    }

    override fun getNoteById(id: Long): Flow<Note> {
        return noteDao.getNoteById(id)
    }

    override suspend fun save(note: Note) {
        noteDao.insertNotes(note)
    }

    override suspend fun update(note: Note) {
        noteDao.updateNote(note)
    }

    override suspend fun delete(id: Long) {
        noteDao.deleteNote(id)
    }

    override fun getFavourites(): Flow<List<Note>> {
        return noteDao.getAllFavourites()
    }
}