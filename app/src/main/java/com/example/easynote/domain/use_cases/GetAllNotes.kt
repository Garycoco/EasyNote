package com.example.easynote.domain.use_cases

import com.example.easynote.data.model.Note
import com.example.easynote.domain.repository.EasyNoteRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllNotes @Inject constructor(private val repository: EasyNoteRepository) {
    operator fun invoke(): Flow<List<Note>> {
        return repository.getAllNotes()
    }
}