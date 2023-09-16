package com.example.easynote.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.easynote.common.Response
import com.example.easynote.data.model.Note
import com.example.easynote.domain.use_cases.AddNote
import com.example.easynote.domain.use_cases.DeleteNote
import com.example.easynote.domain.use_cases.GetAllNotes
import com.example.easynote.domain.use_cases.UpdateNote
import com.example.easynote.domain.use_cases.UseCases
import com.example.easynote.util.EasyNoteContentType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val useCases: UseCases
): ViewModel() {
    private val _state: MutableStateFlow<HomeUiState> = MutableStateFlow(HomeUiState())
    val state: StateFlow<HomeUiState> = _state.asStateFlow()

    init {
        getAllNotes()
        getAllFavourites()
    }

    private fun getAllNotes() = viewModelScope.launch {
        useCases.getNotes().collect {
            _state.value = HomeUiState(
                notes = it
            )
        }
    }
    private fun getAllFavourites() = viewModelScope.launch {
        useCases.getFavourites().collect {
            _state.value = _state.value.copy(
                favourites = it
            )
        }
    }

    fun deleteNote(id: Long) {
        viewModelScope.launch {
            useCases.deleteNote(id)
        }
    }

    fun onFavouriteChange(note: Note) {
        viewModelScope.launch {
            useCases.updateNote(note.copy(isFavourite = !note.isFavourite))
        }
    }
    fun addNote(note: Note) {
        viewModelScope.launch {
            useCases.addNote(note)
        }
    }
    fun setOpenNote(noteId: Long, contentType: EasyNoteContentType) {
        val note = state.value.notes.find { it.id == noteId }
        _state.value = _state.value.copy(
            openedNote = note,
            isDetailOnlyOpen = contentType == EasyNoteContentType.SINGLE_PANE
        )
    }
    fun closeDetailScreen() {
        _state.value = _state.value.copy(
            isDetailOnlyOpen = false,
            openedNote = if (state.value.notes.isNotEmpty()) _state.value.notes.first() else null
        )
    }
}
data class HomeUiState(
    val loading: Boolean = false,
    val notes: List<Note> = emptyList(),
    val favourites: List<Note> = emptyList(),
    val openedNote: Note? = null,
    val isDetailOnlyOpen: Boolean = false
)