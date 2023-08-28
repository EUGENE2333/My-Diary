package com.example.mydiary.presentation.compose.mainComposables

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.mydiary.R
import com.example.mydiary.presentation.DiaryViewModel
import com.example.mydiary.presentation.compose.mainComposables2.detail.DetailUiState
import com.example.mydiary.presentation.compose.mainComposables2.detail.DetailViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*


@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter", "CoroutineCreationDuringComposition")
@Composable
fun NewDiaryEntryScreen(
    navController: NavController,
    viewModel: DiaryViewModel,
    detailViewModel: DetailViewModel?,
    onNavigate: () -> Unit
) {
    val detailUiState = detailViewModel?.detailUiState ?: DetailUiState()
    val selectedFontTheme = viewModel.passwordManager.getFontTheme()
    val selectedColorTheme = viewModel.passwordManager.getColorTheme()
    val isFormsNotBlank = detailUiState.note.isNotBlank() && detailUiState.title.isNotBlank()


    val scope = rememberCoroutineScope()

    val scaffoldState = rememberScaffoldState()

    LaunchedEffect(key1 = Unit){
        detailViewModel?.resetState()
    }

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Write Note",
                        color = Color.White,
                        fontFamily = selectedFontTheme,
                        fontSize = if(selectedFontTheme == FontFamily(Font(R.font.arabia))) 47.sp else  17.sp,
                    )},
                backgroundColor = Color(0xFF2C2428),
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back",tint = Color.White)
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            scope.launch(Dispatchers.IO) {
                                    if(isFormsNotBlank) {
                                    detailViewModel?.addNote()
                                }else{
                                    scaffoldState.snackbarHostState.showSnackbar("Please enter some text")
                            }

                            }
                        }
                    ) {
                        Icon(Icons.Filled.Check, contentDescription ="Save", tint = Color.White)
                    }
                }
            )
        }
    )
        {
            if(detailUiState.noteAddedStatus){
                scope.launch {
                    scaffoldState.snackbarHostState
                        .showSnackbar("Added Entry Successfully")
                    detailViewModel?.resetNoteAddedStatus()
                    onNavigate.invoke()

                }
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 1.dp)
                    .background(color = selectedColorTheme, shape = RectangleShape)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(5.dp)

                ) {
                    Column {

                        TextField(
                            value = detailUiState.title,
                            onValueChange = { detailViewModel?.onTitleChange(it) },
                            label = { Text( "Title", fontFamily = selectedFontTheme) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                                .background(color = Color.White),

                            textStyle = TextStyle(
                                fontFamily = selectedFontTheme,
                                fontSize = if (selectedFontTheme == FontFamily(Font(R.font.arabia))) 47.sp else 16.sp,
                                color = selectedColorTheme
                            )
                        )
                        TextField(
                            value = detailUiState.note,
                            onValueChange = { detailViewModel?.onNoteChange(it) },
                            label = { Text("Content", fontFamily = selectedFontTheme) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                                .weight(1f)
                                .background(color = Color.White),

                            textStyle = TextStyle(
                                fontFamily = selectedFontTheme,
                                fontSize = if (selectedFontTheme == FontFamily(Font(R.font.arabia))) 47.sp else 16.sp,
                                color = selectedColorTheme
                            ),
                            maxLines = Int.MAX_VALUE

                        )
                    }
                }
            }
        
    }

}




































