package com.example.mydiary.presentation.compose.drawerComposables.exportEntries

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.mydiary.R
import com.example.mydiary.data.repository.Resources
import com.example.mydiary.presentation.DiaryViewModel
import com.example.mydiary.presentation.compose.mainComposables.headerFontSizeBasedOnFontTheme

@Composable
fun ExportScreen(
    navController: NavController,
    viewModel: DiaryViewModel,
    exportViewModel:ExportViewModel
) {
    val scaffoldState = rememberScaffoldState()
    val selectedColorTheme = viewModel.passwordManager.getColorTheme()
    val selectedFontTheme = viewModel.passwordManager.getFontTheme()
    val context = LocalContext.current
    val exportState = exportViewModel.exportUiState.collectAsState().value
    val notes = exportState.notesList.data

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
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.Filled.ArrowBack,
                            contentDescription = "Open Drawer",
                            tint = Color.White
                        )
                    }
                }
            )
        },
        backgroundColor = selectedColorTheme,
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally,

            ) {


            Image(
                painter = painterResource(id = R.drawable.export_file),
                contentDescription = "export",
                modifier = Modifier
                    .size(135.dp)
                    .clip(CircleShape)
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 50.dp)

            )


            Text(
                text = "Export all your entries",
                color = Color.White,
                fontSize = headerFontSizeBasedOnFontTheme(selectedFontTheme),
                fontWeight = FontWeight.Normal,
                fontFamily = selectedFontTheme,
                modifier = Modifier.padding(top = 70.dp, bottom = 10.dp)
            )

            Text(
                text = "Store or open your entries in an external application",
                color = Color.White,
                fontSize = headerFontSizeBasedOnFontTheme(selectedFontTheme),
                fontFamily = selectedFontTheme,
                modifier = Modifier.padding(vertical = 30.dp, horizontal = 10.dp)
            )

            when (exportState.notesList) {
                is Resources.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .fillMaxSize()
                            .wrapContentSize(align = Alignment.Center)
                    )
                }
                is Resources.Success -> {

                    if (exportState.notesList.data!!.isEmpty()) {
                        Text(
                            text = "No entries to export",
                            color = Color.Red,
                            fontFamily = selectedFontTheme
                        )
                    }

                    Text(
                        text = "Number of entries: ${notes?.size}",
                        color = Color.White,
                        fontSize = headerFontSizeBasedOnFontTheme(selectedFontTheme),
                        fontFamily = selectedFontTheme,
                        modifier = Modifier.padding(vertical = 20.dp, horizontal = 5.dp)
                    )

                    Button(
                        onClick = { exportTxTFile(notes,context) }
                    ) {
                        Text(
                            text = "Export",
                            color = Color.White,
                            fontFamily = selectedFontTheme,
                            fontSize = 17.sp
                        )
                    }

                }
                else -> {
                    Text(
                        text = exportState.notesList.throwable?.localizedMessage
                            ?: "Unknown error",
                        color = Color.Red
                    )
                }
            }
        }
    }
}
