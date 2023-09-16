package com.example.easynote

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.easynote.presentation.home.EasyNoteApp
import com.example.easynote.presentation.home.HomeViewModel
import com.example.easynote.ui.theme.EasyNoteTheme
import com.google.accompanist.adaptive.calculateDisplayFeatures
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModel: HomeViewModel by viewModels()
        /*viewModel.addNote(Note(
            title = "Sample Note",
            note = "Welcome to EasyNote," +
                    "This is where your notes will appear if you have any",
            createdAt = Date()
        ))*/
        setContent {
            val state by viewModel.state.collectAsStateWithLifecycle()
            val windowSize = calculateWindowSizeClass(activity = this)
            val displayFeatures = calculateDisplayFeatures(activity = this)
            EasyNoteTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    EasyNoteApp(
                        homeUiState = state,
                        onFavouriteChange = { viewModel.onFavouriteChange(it) },
                        onNoteDelete = { viewModel.deleteNote(it) },
                        windowSize = windowSize,
                        displayFeatures = displayFeatures,
                        navigateToDetails = { noteId, pane ->
                            viewModel.setOpenNote(noteId, pane)
                        },
                        closeDetailScreen = {
                            viewModel.closeDetailScreen()
                        }
                    )
                }
            }
        }
    }
}
