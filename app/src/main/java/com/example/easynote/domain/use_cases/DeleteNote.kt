package com.example.easynote.domain.use_cases

import com.example.easynote.domain.repository.EasyNoteRepository
import javax.inject.Inject

class DeleteNote @Inject constructor(private val repository: EasyNoteRepository) {
    suspend operator fun invoke(id: Long) = repository.delete(id)
}