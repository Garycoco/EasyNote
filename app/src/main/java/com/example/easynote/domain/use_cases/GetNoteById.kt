package com.example.easynote.domain.use_cases

import com.example.easynote.data.model.Note
import com.example.easynote.domain.repository.EasyNoteRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetNoteById @Inject constructor(private val repository: EasyNoteRepository) {
    operator fun invoke(id: Long): Flow<Note> {
        return repository.getNoteById(id)
    }
}