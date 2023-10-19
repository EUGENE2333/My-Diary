package com.example.mydiary.presentation.compose.mainComposables

import android.annotation.SuppressLint
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.mydiary.MainActivity
import com.example.mydiary.R
import com.example.mydiary.presentation.DiaryViewModel
import com.example.mydiary.presentation.compose.mainComposables2.detail.DetailViewModel
import kotlinx.coroutines.launch


@OptIn(ExperimentalFoundationApi::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter", "SuspiciousIndentation",
    "CoroutineCreationDuringComposition"
)
@Composable
fun DiaryDetailScreen(
    navController: NavController,
    viewModel: DiaryViewModel,
    detailViewModel: DetailViewModel?,
    noteId: String,
    onNavigate: () -> Unit
) {

    val detailUiState = detailViewModel!!.detailUiState
    val scaffoldState = rememberScaffoldState()
    val selectedFontTheme = viewModel.passwordManager.getFontTheme()
    val selectedColorTheme = viewModel.passwordManager.getColorTheme()
    val mainActivity = (LocalContext.current as MainActivity)
    val enabled = viewModel.enabledFlow.collectAsState(initial = false).value
    val isFormsNotBlank = detailUiState.note.isNotBlank() && detailUiState.title.isNotBlank()
    val scope = rememberCoroutineScope()


    LaunchedEffect(key1 = Unit){
        detailViewModel.getNote(noteId)
    }



    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Diary Entry",
                        color = Color.White,
                        fontFamily = selectedFontTheme,
                        fontSize = headerFontSizeBasedOnFontTheme(selectedFontTheme)
                    )},
                backgroundColor = Color(0xFF2C2428),
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back",tint = Color.White)
                    }
                },
                actions = {
                    IconButton(onClick = {
                        scope.launch {
                        if (isFormsNotBlank) {
                            detailViewModel.updateNote(noteId)
                        } else {
                                scaffoldState.snackbarHostState.showSnackbar("Please enter some text")
                            }
                        }
                    }) {
                        Icon(Icons.Filled.Check, contentDescription ="Save" ,tint = Color.White)
                    }

                    if (enabled) {
                        IconButton(onClick = {
                            if (detailUiState.note.isNotBlank()) {
                                mainActivity.speakText(detailUiState.note)
                            }
                            else{
                                viewModel.viewModelScope.launch {
                                    scaffoldState.snackbarHostState.showSnackbar("Please enter some text")
                                }
                            }
                        }) {
                            Image(
                                painter = painterResource(id = R.drawable.outlet),
                                contentDescription = null
                            )
                        }
                    }

                },
            )
        }
    ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 1.dp)
                    .background(color = selectedColorTheme, shape = RectangleShape)
            ) {

                if(detailUiState.updateNoteStatus){
                    scope.launch {
                        scaffoldState.snackbarHostState
                            .showSnackbar("Entry Updated Successfully")
                        detailViewModel.resetNoteAddedStatus()
                        onNavigate.invoke()

                    }
                }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(5.dp)

            ) {
                Column {

                    TextField(
                        value = detailUiState.title,
                        onValueChange = { detailViewModel.onTitleChange(it) },
                        label = { Text( "Title", fontFamily = selectedFontTheme) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .background(color = Color.White),

                        textStyle = TextStyle(
                            fontFamily = selectedFontTheme,
                            fontSize = fontSizeBasedOnFontTheme(selectedFontTheme),
                            color = selectedColorTheme
                        )

                    )

                    TextField(
                        value = detailUiState.note,
                        onValueChange = { detailViewModel.onNoteChange(it) },
                        label = { Text("Content", fontFamily = selectedFontTheme) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .weight(1f)
                            .background(color = Color.White),

                        textStyle = TextStyle(
                            fontFamily = selectedFontTheme,
                            fontSize = fontSizeBasedOnFontTheme(selectedFontTheme),
                            color = selectedColorTheme
                        ),
                        maxLines = Int.MAX_VALUE

                    )
                }

            }
        }
    }
}












