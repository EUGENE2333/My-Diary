package com.example.mydiary.presentation.compose.drawerComposables.colorAndStyle

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ColorThemeSelectionGrid(
    colorThemes: List<Color>,
    selectedColor: State<Color>,
    onColorSelected: (Color) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
        //  contentPadding = PaddingValues(8.dp),

    ){
        items(colorThemes.size) { index ->
            val color = colorThemes[index]
            ColorThemeButton(
                color = color,
                isSelected = color == selectedColor.value,
                onColorSelected = { onColorSelected(color) }
            )
        }
    }
}

@Composable
fun ColorThemeButton(
    color: Color,
    isSelected: Boolean,
    onColorSelected: () -> Unit
) {
    val buttonSize = 40.dp
    val selectedIconSize = 17.dp

    Box(
        modifier = Modifier
            .size(buttonSize)
            .clickable { onColorSelected() }
            .background(color, shape = CircleShape)
            .padding(4.dp)
            .border(
                width = if (isSelected) 2.dp else 1.dp,
                color = Color.Gray,
                shape = CircleShape
            ),
        contentAlignment = Alignment.Center
    ) {
        if (isSelected) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = "Selected",
                tint = MaterialTheme.colors.onPrimary,
                modifier = Modifier.size(selectedIconSize)
            )
        }
    }
}

