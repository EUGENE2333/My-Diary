package com.example.mydiary.presentation.compose.mainComposables

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.mydiary.R
import com.example.mydiary.presentation.DiaryViewModel
import com.example.mydiary.presentation.compose.mainComposables2.detail.DetailUiState
import com.example.mydiary.presentation.compose.mainComposables2.detail.DetailViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter", "CoroutineCreationDuringComposition")
@Composable
fun NewDiaryEntryScreen(
    navController: NavController,
    viewModel: DiaryViewModel = hiltViewModel(),
    detailViewModel: DetailViewModel = hiltViewModel(),
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
                        fontSize = headerFontSizeBasedOnFontTheme(selectedFontTheme = selectedFontTheme)
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
                                fontSize = if(
                                    selectedFontTheme == FontFamily(Font(R.font.pizzat)) ||
                                    selectedFontTheme == FontFamily(Font(R.font.first_writing)) ||
                                    selectedFontTheme == FontFamily(Font(R.font.slimshoot)) ||
                                    selectedFontTheme == FontFamily(Font(R.font.gnyrwn977))
                                ) 26.sp else 16.sp,
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
                                fontSize = if(
                                    selectedFontTheme == FontFamily(Font(R.font.pizzat)) ||
                                    selectedFontTheme == FontFamily(Font(R.font.first_writing)) ||
                                    selectedFontTheme == FontFamily(Font(R.font.slimshoot)) ||
                                    selectedFontTheme == FontFamily(Font(R.font.gnyrwn977))
                                ) 26.sp else 16.sp,
                                color = selectedColorTheme
                            ),
                            maxLines = Int.MAX_VALUE

                        )
                    }
                }
            }
        
    }

}




































