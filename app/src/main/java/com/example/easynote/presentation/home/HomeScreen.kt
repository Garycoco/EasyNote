package com.example.easynote.presentation.home

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridState
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.itemsIndexed
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeFloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.window.layout.DisplayFeature
import com.datamate.easynote.R
import com.example.easynote.data.model.Note
import com.example.easynote.presentation.components.HomeAppBar
import com.example.easynote.util.EasyNoteContentType
import com.example.easynote.util.EasyNoteNavigationType
import com.google.accompanist.adaptive.HorizontalTwoPaneStrategy
import com.google.accompanist.adaptive.TwoPane

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    homeUiState: HomeUiState,
    contentType: EasyNoteContentType,
    onFavouriteChange: (note: Note) -> Unit,
    onNoteDelete: (Long) -> Unit,
    onAddNoteClicked: () -> Unit,
    navigationType: EasyNoteNavigationType,
    displayFeatures: List<DisplayFeature>,
    onNoteClicked: (Long, EasyNoteContentType) -> Unit,
    closeDetailScreen: () -> Unit
) {
    LaunchedEffect(key1 = contentType) {
        if (contentType == EasyNoteContentType.SINGLE_PANE && !homeUiState.isDetailOnlyOpen) {
            closeDetailScreen()
        }
    }
    val listState = rememberLazyStaggeredGridState()

    if (contentType == EasyNoteContentType.DUAL_PANE) {
        TwoPane(
            first = {
                ListContent(
                    notes = homeUiState.notes,
                    onNoteClicked = onNoteClicked,
                    onNoteDelete = onNoteDelete,
                    onFavouriteChange = onFavouriteChange,
                    lazyListState = listState
                )
            },
            second = {
                NoteDetail(
                    note = homeUiState.openedNote ?: homeUiState.notes.first(),
                    isFullScreen = false
                )
            },
            strategy = HorizontalTwoPaneStrategy(splitFraction = 0.5f, gapWidth = 16.dp),
            displayFeatures = displayFeatures
        )
    } else {
        Box(modifier = modifier.fillMaxSize()) {
            EasyNoteSinglePaneContent(
                homeUiState = homeUiState,
                lazyListState = listState,
                onNoteDelete = onNoteDelete,
                onFavouriteChange = onFavouriteChange,
                closeDetailScreen = closeDetailScreen,
                navigateToDetail = onNoteClicked
            )
            if (navigationType == EasyNoteNavigationType.BOTTOM_NAVIGATION) {
                LargeFloatingActionButton(
                    onClick = onAddNoteClicked,
                    modifier = modifier
                        .align(Alignment.BottomEnd)
                        .padding(16.dp),

                ) {
                    Icon(imageVector = Icons.Rounded.Add, contentDescription = null)
                }
            }
        }
    }
}

@Composable
fun EasyNoteSinglePaneContent(
    homeUiState: HomeUiState,
    lazyListState: LazyStaggeredGridState,
    onNoteDelete: (Long) -> Unit,
    onFavouriteChange: (note: Note) -> Unit,
    closeDetailScreen: () -> Unit,
    navigateToDetail: (Long, EasyNoteContentType) -> Unit
) {
    if (homeUiState.openedNote != null && homeUiState.isDetailOnlyOpen) {
        BackHandler {
            closeDetailScreen()
        }
        NoteDetail(note = homeUiState.openedNote, onBackPressed = closeDetailScreen)
    } else {
        ListContent(
            notes = homeUiState.notes,
            onNoteClicked = navigateToDetail,
            onNoteDelete = onNoteDelete,
            onFavouriteChange = onFavouriteChange,
            lazyListState = lazyListState
        )
    }
}

@Composable
fun ListContent(
    notes: List<Note>,
    onNoteClicked: (Long, EasyNoteContentType) -> Unit,
    onNoteDelete: (Long) -> Unit,
    onFavouriteChange: (note: Note) -> Unit,
    lazyListState: LazyStaggeredGridState,
    modifier: Modifier = Modifier
) {

    Scaffold(
        topBar = { HomeAppBar() }
    ) { paddingValues ->
        LazyVerticalStaggeredGrid(
            columns = StaggeredGridCells.Fixed(2),
            state = lazyListState,
            contentPadding = PaddingValues(4.dp),
            modifier = modifier.padding(paddingValues)
        ) {
            itemsIndexed(notes) { index, note ->
                NoteCard(
                    index = index,
                    note = note,
                    onNoteClicked = { noteId ->
                        onNoteClicked(noteId, EasyNoteContentType.SINGLE_PANE)
                    },
                    onNoteDelete = onNoteDelete,
                    onFavouriteChange = onFavouriteChange
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteDetail(
    modifier: Modifier = Modifier,
    note: Note,
    isFullScreen: Boolean = true,
    onBackPressed: () -> Unit = {}
) {

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                modifier = modifier.fillMaxWidth(),
                title = { Text(text = stringResource(id = R.string.app_name)) }, navigationIcon = {
                    IconButton(
                        onClick = { onBackPressed() }) {
                        Icon(imageVector = Icons.Rounded.ArrowBack, contentDescription = "back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {

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