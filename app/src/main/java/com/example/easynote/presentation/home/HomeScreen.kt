package com.example.easynote.presentation.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.easynote.common.Response
import com.example.easynote.data.model.Note
import java.util.Date

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    homeUiState: HomeUiState,
    onFavouriteChange: (note: Note) -> Unit,
    onNoteDelete: (Long) -> Unit,
    onNoteClicked: (Long) -> Unit
) {
    when(homeUiState.notes) {
        is Response.Loading -> CircularProgressIndicator()
        is Response.Success -> {
            val notes = homeUiState.notes.data
            HomeContent(
                notes = notes,
                onNoteClicked = onNoteClicked,
                onNoteDelete = onNoteDelete,
                onFavouriteChange = onFavouriteChange
            )
        }
        is Response.Error -> {

        }
    }
}

@Composable
fun HomeContent(
    notes: List<Note>,
    onNoteClicked: (Long) -> Unit,
    onNoteDelete: (Long) -> Unit,
    onFavouriteChange: (note: Note) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Fixed(2),
        contentPadding = PaddingValues(4.dp),
        modifier = modifier
    ) {
        itemsIndexed(notes) { index, note ->
            NoteCard(
                index = index,
                note = note,
                onNoteClicked = onNoteClicked,
                onNoteDelete = onNoteDelete,
                onFavouriteChange = onFavouriteChange
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteCard(
    index: Int,
    note: Note,
    onNoteClicked: (Long) -> Unit,
    onNoteDelete: (Long) -> Unit,
    onFavouriteChange: (note: Note) -> Unit,
    modifier: Modifier = Modifier
) {
    val isEvenIndex = index % 2 == 0
    val shape = when {
        isEvenIndex -> RoundedCornerShape(topStart = 50f, bottomEnd = 50f)
        else -> RoundedCornerShape(topEnd = 50f, bottomStart = 50f)
    }
    val icon = if (note.isFavourite) Icons.Rounded.Favorite
    else Icons.Rounded.FavoriteBorder

    ElevatedCard(
        onClick = { onNoteClicked(note.id) },
        modifier = modifier
            .fillMaxWidth()
            .padding(4.dp),
        shape = shape
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Text(
                text = note.title,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = modifier.height(4.dp))
            Text(
                text = note.note,
                maxLines = 4,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.bodyMedium
            )
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = modifier
                    .fillMaxWidth()
                    //.padding(vertical = 4.dp, horizontal = 2.dp)
            ) {
                IconButton(onClick = { onNoteDelete(note.id) }) {
                    Icon(imageVector = Icons.Rounded.Delete, contentDescription = "Delete note")
                }
                IconButton(onClick = { onFavouriteChange(note) }) {
                    Icon(imageVector = icon, contentDescription = "Favourite toggle")
                }
            }
        }
    }
}

/*
@Preview(showSystemUi = true)
@Composable
fun HomePrev() {
    HomeScreen(
        homeUiState = HomeUiState(notes = Response.Success(notes)),
        onFavouriteChange = {},
        onNoteDelete = {},
        onNoteClicked = {}
    )
}
val notes = listOf(
    Note(title = "Recipe", note = "Im good, I just want to greet you", createdAt = Date()),
    Note(title = "Recipe", note = "Im good, I just want to greet you", createdAt = Date(), isFavourite = true),
)*/
