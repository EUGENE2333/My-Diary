package com.example.mydiary.presentation.compose.drawerComposables.exportEntries

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mydiary.presentation.DiaryViewModel
import com.example.mydiary.presentation.compose.mainComposables.headerFontSizeBasedOnFontTheme
import com.example.mydiary.presentation.compose.mainComposables2.home.HomeUiState
import com.example.mydiary.presentation.compose.mainComposables2.home.HomeViewModel


@Composable
fun ExportScreen(
    navController: NavController,
    viewModel: DiaryViewModel,
    homeViewModel: HomeViewModel?,
){

   val scaffoldState  = rememberScaffoldState()
    val selectedColorTheme = viewModel.passwordManager.getColorTheme()
    val selectedFontTheme = viewModel.passwordManager.getFontTheme()
    val homeUiState = homeViewModel?.homeUiState ?: HomeUiState()
    val context = LocalContext.current

    // Handle export button click
    var exporting by remember { mutableStateOf(false) }
    var exportSuccess by remember { mutableStateOf(false) }

    // Create a sample TXT file
    val sampleTxtFile = createSampleTxtFile(context, homeViewModel)

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(title = {
                Text(
                    text = "Export",
                    color = Color.White,
                    fontSize = headerFontSizeBasedOnFontTheme(selectedFontTheme),
                    fontFamily = selectedFontTheme
                )
            },
                backgroundColor = Color(0xFF2C2428),
                navigationIcon = {
                    IconButton(onClick = {navController.popBackStack()}) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Open Drawer",tint = Color.White)
                    }
                }
            )
        },
        backgroundColor = selectedColorTheme,
    ){ paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally,

        ) {

            Text(
                text = "Export all your entries",
                color = Color.White,
                fontSize =  headerFontSizeBasedOnFontTheme(selectedFontTheme),
                fontWeight = FontWeight.Normal,
                fontFamily = selectedFontTheme,
                modifier = Modifier.padding(top = 157.dp, bottom = 30.dp)
            )

            Text(
                text = "Store/open your entries in an external application",
                color = Color.White,
                fontSize =  headerFontSizeBasedOnFontTheme(selectedFontTheme),
                fontFamily = selectedFontTheme,
                modifier = Modifier.padding(vertical = 30.dp)
            )

          Button(
              onClick = {

                    if (!exporting && !exportSuccess) {
                        exporting = true
                        exportUserNotesAsText(context, sampleTxtFile) {
                            exportSuccess = it
                            exporting = false
                        }
                    }
            }) {
                Text(
                    text = "Export",
                    color = Color.White,
                    fontSize = headerFontSizeBasedOnFontTheme(selectedFontTheme),
                    fontFamily = selectedFontTheme
                )
            }
        }

    }
}