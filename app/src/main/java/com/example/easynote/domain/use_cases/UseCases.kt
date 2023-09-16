package com.example.easynote.domain.use_cases

data class UseCases(
    val getNotes: GetAllNotes,
    val addNote: AddNote,
    val deleteNote: DeleteNote,
    val getFavourites: GetFavourites,
    val getNoteById: GetNoteById,
    val updateNote: UpdateNote
)
