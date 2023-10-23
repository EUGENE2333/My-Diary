package com.example.mydiary.presentation.compose.drawerComposables.lockScreen

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.mydiary.R
import com.example.mydiary.data.utils.Utils.questions
import com.example.mydiary.presentation.DiaryViewModel
import com.example.mydiary.presentation.compose.mainComposables.headerFontSizeBasedOnFontTheme
import com.example.mydiary.presentation.compose.navigation.Screen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun SecurityQuestions(
    navController: NavController,
    viewModel: DiaryViewModel,
) {
    var answer by remember { mutableStateOf("") }
    val selectedFont = viewModel.passwordManager.getFontTheme()
    val selectedColorTheme = viewModel.passwordManager.getColorTheme()
    val scaffoldState = rememberScaffoldState()
    var expanded by remember { mutableStateOf(false) }
    var selectedQuestion by remember { mutableStateOf("") }
    var isQuestionSelected by remember { mutableStateOf(false) }



    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Security Questions",
                        color = Color.White,
                        fontFamily = selectedFont,
                        fontSize =  headerFontSizeBasedOnFontTheme(selectedFont)
                    )},
                backgroundColor = Color(0xFF2C2428),
                navigationIcon = {
                    IconButton(onClick = { navController.navigate(Screen.DiaryList.route) }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "back",tint = Color.White)
                    }
                },
            )
        },
        backgroundColor = selectedColorTheme,
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top= 96.dp,start = 10.dp, end = 10.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Spacer(modifier = Modifier.height(25.dp))
                Row(
                    modifier = Modifier.clickable {  expanded = !expanded },
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Text(
                        text = "Select a security question",
                        style = typography.subtitle2,
                        fontFamily = selectedFont,
                        fontSize = 23.sp,
                        color = Color.White
                    )
                    Image(
                        painter =if(!expanded)
                            painterResource(id = R.drawable.keyboard_arrow_down)
                        else
                            painterResource(id = R.drawable.keyboard_arrow_up),
                        contentDescription = "share Image",
                        modifier = Modifier.size(40.dp)
                    )
                }
                Spacer(modifier = Modifier.height(13.dp))

                // Dropdown to select a security question
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { selectedQuestion = "" },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    questions.forEach { question ->
                        DropdownMenuItem(onClick = {
                            selectedQuestion = question
                            isQuestionSelected = true
                            expanded = false
                        }) {
                            Text(text = question)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(15.dp))

                if(isQuestionSelected){
                    Text(
                        text = selectedQuestion,
                        style = typography.subtitle2,
                        fontFamily = selectedFont,
                        color = Color.White,
                        fontSize = 23.sp
                    )

                    Spacer(modifier = Modifier.height(15.dp))
                    TextField(
                        value =answer,
                        onValueChange = { answer = it },
                        maxLines = 1,
                        modifier = Modifier.fillMaxWidth(),
                    )
                    Spacer(modifier = Modifier.height(15.dp))

                    Button(
                        onClick = {
                            viewModel.viewModelScope.launch(Dispatchers.IO) {
                                try {
                                    if ( answer.trim().isNotEmpty()) {
                                        viewModel.passwordManager.setQuestionAnswer(selectedQuestion,answer.trim())
                                        withContext(Dispatchers.Main) {
                                            scaffoldState.snackbarHostState.showSnackbar(
                                                "Security Question has been set!!"
                                            )
                                            navController.navigate(Screen.DiaryList.route)
                                        }
                                    }else{
                                        withContext(Dispatchers.Main) {
                                            scaffoldState.snackbarHostState.showSnackbar(
                                                "No answer entered!"
                                            )
                                        }
                                    }
                                } catch (e: Exception) {
                                    // Handle any exceptions that may occur
                                    Log.e(TAG, "Error setting password: $e")
                                }
                            }
                        },
                    ) {
                        Text(text = "Done",style = typography.subtitle2, fontFamily = selectedFont)
                    }

                }

            }
        }
    )
}