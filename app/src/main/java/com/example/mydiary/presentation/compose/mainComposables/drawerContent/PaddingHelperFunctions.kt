package com.example.mydiary.presentation.compose.mainComposables.drawerContent


import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp



fun Modifier.paddingForSection(): Modifier = this.then(
    Modifier.padding(
        start = 20.dp,
        bottom = 8.dp
    )
)

fun Modifier.paddingForImage(): Modifier = this.then(
    Modifier.padding(
        top = 16.dp,
        start = 6.dp,
        end = 6.dp
    )
)


fun Modifier.paddingForText(): Modifier = this.then(
    Modifier.padding(
        top = 16.dp,
        start = 15.dp,
    )
)



















