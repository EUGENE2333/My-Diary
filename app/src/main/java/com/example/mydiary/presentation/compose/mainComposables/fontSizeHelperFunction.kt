package com.example.mydiary.presentation.compose.mainComposables

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.example.mydiary.R

@Composable
fun fontSizeBasedOnFontTheme(selectedFontTheme: FontFamily): TextUnit =
    if (selectedFontTheme == FontFamily(Font(R.font.pizzat)) ||
        selectedFontTheme == FontFamily(Font(R.font.first_writing)) ||
        selectedFontTheme == FontFamily(Font(R.font.slimshoot)) ||
        selectedFontTheme == FontFamily(Font(R.font.twirly)) ||
        selectedFontTheme == FontFamily(Font(R.font.gnyrwn977))
    )  26.sp else  16.sp

@Composable
fun headerFontSizeBasedOnFontTheme(selectedFontTheme: FontFamily): TextUnit =
    if (selectedFontTheme == FontFamily(Font(R.font.pizzat)) ||
        selectedFontTheme == FontFamily(Font(R.font.first_writing)) ||
        selectedFontTheme == FontFamily(Font(R.font.slimshoot)) ||
        selectedFontTheme == FontFamily(Font(R.font.twirly)) ||
        selectedFontTheme == FontFamily(Font(R.font.gnyrwn977))
    )  37.sp else  25.sp
