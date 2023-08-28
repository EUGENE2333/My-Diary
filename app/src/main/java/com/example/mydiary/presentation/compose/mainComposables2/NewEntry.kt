package com.example.mydiary.presentation.compose.mainComposables2


import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mydiary.R
import com.example.mydiary.data.utils.Utils
import com.example.mydiary.presentation.DiaryViewModel
import com.example.mydiary.presentation.compose.mainComposables2.detail.DetailUiState
import com.example.mydiary.presentation.compose.mainComposables2.detail.DetailViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun NewEntry(
    detailViewModel: DetailViewModel?,
    viewModel: DiaryViewModel,
    onNavigate: () -> Unit
){

    val detailUiState = detailViewModel?.detailUiState ?: DetailUiState()
    val isFormsNotBlank = detailUiState.note.isNotBlank() && detailUiState.title.isNotBlank()
    val selectedColor by animateColorAsState(
        targetValue = Utils.colors[detailUiState.colorIndex]
    )

    val selectedFontTheme = viewModel.passwordManager.getFontTheme()

    val icon =  Icons.Default.Check

    val scope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState()

    LaunchedEffect(key1 = Unit){
        detailViewModel?.resetState()
    }





    Scaffold(
        scaffoldState = scaffoldState,
        floatingActionButton = {
            AnimatedVisibility(visible = isFormsNotBlank){
                FloatingActionButton(
                    onClick = {
                        scope.launch (Dispatchers.IO){
                        detailViewModel?.addNote()
                        }
                    }){
                    Icon(imageVector = icon , contentDescription = null)
                }
            }
        }
    ) {padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = selectedColor)
                .padding(padding)
        ) {
            if(detailUiState.noteAddedStatus){
                scope.launch {
                    scaffoldState.snackbarHostState
                        .showSnackbar("Added Entry Successfully")
                    detailViewModel?.resetNoteAddedStatus()
                    detailViewModel?.resetState()
                    onNavigate.invoke()
                }
            }

            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                contentPadding = PaddingValues(vertical = 16.dp, horizontal = 8.dp)
            ){
                itemsIndexed(Utils.colors){colorIndex,color ->
                    ColorItem(color = color) {
                        detailViewModel?.onColorChange(colorIndex)

                    }
                }

            }
            OutlinedTextField(
                value = detailUiState.title,
                onValueChange ={
                    detailViewModel?.onTitleChange(it)
                },
                textStyle = TextStyle(
                    fontFamily = selectedFontTheme,
                    fontSize = if (selectedFontTheme == FontFamily(Font(R.font.arabia))) 47.sp else 16.sp,
                ),
                label = {Text(text = "Title")},
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)

            )

            OutlinedTextField(
                value = detailUiState.note,
                onValueChange ={detailViewModel?.onNoteChange(it)},
                label = {Text(text = "Note")},
                textStyle = TextStyle(
                    fontFamily = selectedFontTheme,
                    fontSize = if (selectedFontTheme == FontFamily(Font(R.font.arabia))) 47.sp else 16.sp,
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(8.dp)
            )
        }
    }
}


@Composable
fun ColorItem(
    color: Color,
    onClick:() -> Unit
){
    Surface(
        color = color,
        shape = CircleShape,
        modifier = Modifier
            .padding(8.dp)
            .size(36.dp)
            .clickable { onClick.invoke() },
        border = BorderStroke(2.dp,Color.Black)

    ){

    }
}
