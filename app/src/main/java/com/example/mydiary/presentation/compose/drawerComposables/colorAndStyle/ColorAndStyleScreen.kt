package com.example.mydiary.presentation.compose.drawerComposables.colorAndStyle

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mydiary.presentation.DiaryViewModel
import com.example.mydiary.presentation.compose.mainComposables.headerFontSizeBasedOnFontTheme


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ColorAndStyle(
    viewModel: DiaryViewModel,
    navController: NavController
) {
    val selectedColorTheme = viewModel.selectedColorTheme.collectAsState()
    val selectedColor = viewModel.passwordManager.getColorTheme()
    val selectedFontTheme = viewModel.selectedFont.collectAsState()
    val selectedFont by rememberUpdatedState(viewModel.passwordManager.getFontTheme())
    val scaffoldState = rememberScaffoldState()


    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(title = {
                Text(
                    text = "Color and Font",
                    color = Color.White,
                    fontFamily = selectedFont,
                    fontSize =  headerFontSizeBasedOnFontTheme(selectedFont)
                )
            },
                backgroundColor = Color(0xFF2C2428),
                navigationIcon = {
                    IconButton(onClick = {  navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "back",tint = Color.White)
                    }
                }
            )
        },
        backgroundColor =selectedColor,
        content = {

            Column(
                modifier = Modifier.fillMaxSize()
                .padding(horizontal = 10.dp),
            ) {
                Text(
                    text = "Select color:",
                    style = MaterialTheme.typography.h4,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                     fontFamily = selectedFont
                )
                ColorThemeSelectionGrid(
                    colorThemes = viewModel.colorThemes,
                    selectedColor = selectedColorTheme
                ) { color ->
                    viewModel.setSelectedColorTheme(color)
                }
                Divider(
                    modifier = Modifier
                        .padding(10.dp)
                        .fillMaxWidth()
                        .width(10.dp),
                    color = Color.White
                )
              //  Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Select font:",
                    style = MaterialTheme.typography.h4,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    fontFamily = selectedFont
                )
                Box(
                    modifier = Modifier.padding(start= 10.dp,end = 10.dp)
                ) {


                    FontSelectionColumn(
                        fonts = viewModel.fonts,
                        selectedFont = selectedFontTheme
                    ) { font ->
                        viewModel.setSelectedFontTheme(font)

                    }
                }

                Spacer(modifier = Modifier.width(10.dp))
            }

        })
}
