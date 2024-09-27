package com.example.imageapp.components

import androidx.compose.runtime.Composable

//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.ui.graphics.Color

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    title: String = "Images"
) {
    TopAppBar(
        title = {
            Text(text = title, color = Color.White) // Displaying the title in white
        },
    )
}
