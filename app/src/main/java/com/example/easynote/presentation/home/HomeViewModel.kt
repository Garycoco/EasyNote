package com.example.easynote.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.easynote.common.Response
import com.example.easynote.data.model.Note
import com.example.easynote.domain.use_cases.DeleteNote
import com.example.easynote.domain.use_cases.GetAllNotes
import com.example.easynote.domain.use_cases.UpdateNote
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
    private val getAllNotes: GetAllNotes,
    private val deleteNote: DeleteNote,
    private val updateNote: UpdateNote
): ViewModel() {
    private val _state: MutableStateFlow<HomeUiState> = MutableStateFlow(HomeUiState())
    val state: StateFlow<HomeUiState> = _state.asStateFlow()

    init {
        getAllNotes()
    }

    private fun getAllNotes() {
        getAllNotes.invoke()
            .onEach {
                _state.value = HomeUiState(notes = Response.Success(it))
            }
            .catch {
                _state.value = HomeUiState(notes = Response.Error(it.message))
            }
            .launchIn(viewModelScope)
    }

    fun deleteNote(id: Long) {
        viewModelScope.launch {
            deleteNote.invoke(id)
        }
    }

    fun onFavouriteChange(note: Note) {
        viewModelScope.launch {
            updateNote.invoke(note.copy(isFavourite = !note.isFavourite))
        }
    }

}
data class HomeUiState(
    val notes: Response<List<Note>> = Response.Loading,
)