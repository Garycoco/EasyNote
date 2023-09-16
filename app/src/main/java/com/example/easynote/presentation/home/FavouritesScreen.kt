package com.example.easynote.presentation.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridState
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.FavoriteBorder
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
import androidx.compose.ui.unit.dp
import com.example.easynote.data.model.Note
import com.example.easynote.util.EasyNoteContentType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavouritesScreen(
    modifier: Modifier = Modifier,
    homeUiState: HomeUiState,
    onNoteClicked: (Long) -> Unit,
    onDeleteNote: (Long) -> Unit,
    onFavouriteChange: (note: Note) -> Unit
) {
    val listState = rememberLazyListState()
    val favourites = homeUiState.notes.filter {
        it.isFavourite
    }
    Box(modifier = Modifier.fillMaxSize().padding(4.dp)) {
        LazyColumn(state = listState) {
            itemsIndexed(favourites) { index, note ->
                val icon = if (note.isFavourite) Icons.Rounded.Favorite
                else Icons.Rounded.FavoriteBorder

                ElevatedCard(
                    onClick = { onNoteClicked(note.id) },
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(4.dp)
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
                            IconButton(onClick = { onDeleteNote(note.id) }) {
                                Icon(imageVector = Icons.Rounded.Delete, contentDescription = "Delete note")
                            }
                            IconButton(onClick = { onFavouriteChange(note) }) {
                                Icon(imageVector = icon, contentDescription = "Favourite toggle")
                            }
                        }
                    }
                }
            }
        }
    }
}