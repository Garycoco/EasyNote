package com.example.easynote.presentation.home

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.easynote.data.model.Note
import com.example.easynote.navigation.EasyNoteBottomNavigationBar
import com.example.easynote.navigation.EasyNoteNavigationActions
import com.example.easynote.navigation.EasyNoteRoute

@Composable
fun EasyNoteApp(
    homeUiState: HomeUiState,
    onFavouriteChange: (note: Note) -> Unit,
    onNoteDelete: (id: Long) -> Unit,
    onNoteClicked: (id: Long) -> Unit
) {
    EasyNoteNavHost(
        homeUiState = homeUiState,
        onFavouriteChange = onFavouriteChange,
        onNoteDelete = onNoteDelete,
        onAddNoteClicked = {  },
        onNoteClicked = { onNoteClicked(it) }
    )
}

@Composable
fun EasyNoteNavHost(
    homeUiState: HomeUiState,
    onFavouriteChange: (note: Note) -> Unit,
    onNoteDelete: (Long) -> Unit,
    onAddNoteClicked: () -> Unit,
    onNoteClicked: (Long) -> Unit
) {
    val navController = rememberNavController()
    val navigationActions = remember(navController) {
        EasyNoteNavigationActions(navController)
    }
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val selectedDestination = navBackStackEntry?.destination?.route ?: EasyNoteRoute.home
    Scaffold(
        bottomBar = { EasyNoteBottomNavigationBar(
            selectedDestination = selectedDestination,
            navigateToTopLevelDestination = navigationActions::navigateTo
        ) }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = EasyNoteRoute.home,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(EasyNoteRoute.home) {
                HomeScreen(
                    homeUiState = homeUiState,
                    onFavouriteChange = onFavouriteChange,
                    onNoteDelete = onNoteDelete,
                    onAddNoteClicked = { onAddNoteClicked() },
                    onNoteClicked = onNoteClicked
                )
            }
            composable(EasyNoteRoute.favourites) {
                NoteDetailsScreen()
            }
        }
    }

}