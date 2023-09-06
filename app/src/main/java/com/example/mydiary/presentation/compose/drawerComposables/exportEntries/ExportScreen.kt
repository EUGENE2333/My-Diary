package com.example.mydiary.presentation.compose.drawerComposables.exportEntries

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.example.mydiary.presentation.DiaryViewModel
import com.example.mydiary.presentation.compose.mainComposables.headerFontSizeBasedOnFontTheme
import com.example.mydiary.presentation.compose.mainComposables2.home.HomeUiState
import com.example.mydiary.presentation.compose.mainComposables2.home.HomeViewModel
import kotlinx.coroutines.launch


@Composable
fun ExportScreen(
    navController: NavController,
    viewModel: DiaryViewModel,
    homeViewModel: HomeViewModel?,
){

   val scaffoldState  = rememberScaffoldState()
    val scope = rememberCoroutineScope()
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
    ){paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)

        ) {

            OutlinedButton(onClick = {
                scope.launch {

                    if (homeUiState.notesList.data!!.isEmpty()) {
                        // no data to export . snackbar message
                        scaffoldState.snackbarHostState.showSnackbar("No entries to export")
                    }

                    if (!exporting && !exportSuccess) {
                        exporting = true
                        exportUserNotesAsText(context, sampleTxtFile) {
                            exportSuccess = it
                            exporting = false
                        }
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
















