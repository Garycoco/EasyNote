package com.example.easynote

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.easynote.presentation.home.EasyNoteApp
import com.example.easynote.presentation.home.HomeViewModel
import com.example.easynote.ui.theme.EasyNoteTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModel: HomeViewModel by viewModels()
        setContent {
            val state by viewModel.state.collectAsStateWithLifecycle()
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
                        onNoteClicked = {  }
                    )
                }
            }
        }
    }
}
