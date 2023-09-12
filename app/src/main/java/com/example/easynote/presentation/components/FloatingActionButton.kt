package com.example.easynote.presentation.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeFloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun FloatingActionButton(onFloatingActionClicked: () -> Unit) {
    LargeFloatingActionButton(onClick = { onFloatingActionClicked() }) {
        Icon(imageVector = Icons.Rounded.Add, contentDescription = "Add note")
    }
}