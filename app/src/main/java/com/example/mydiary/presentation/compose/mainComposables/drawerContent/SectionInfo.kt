package com.example.mydiary.presentation.compose.mainComposables.drawerContent

import com.example.mydiary.presentation.compose.navigation.Screen

data class SectionInfo(
    val route: Screen,
    val title: String,
    val iconId: Int
    )
