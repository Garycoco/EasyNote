package com.example.easynote.domain.use_cases

import com.example.easynote.data.model.Note
import com.example.easynote.domain.repository.EasyNoteRepository
import javax.inject.Inject

class UpdateNote @Inject constructor(private val repository: EasyNoteRepository) {
    suspend operator fun invoke(note: Note) = repository.update(note)
}