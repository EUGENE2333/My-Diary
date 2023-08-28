package com.example.mydiary.presentation.compose.drawerComposables.lockScreen

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.mydiary.presentation.DiaryViewModel
import com.example.mydiary.presentation.compose.navigation.Screen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun SecurityQuestions(
    navController: NavController,
    viewModel: DiaryViewModel,
    onQuestionSet: (String) -> Unit,
) {
    var answer by remember { mutableStateOf("") }
    val selectedFont = viewModel.passwordManager.getFontTheme()
    val selectedColorTheme = viewModel.passwordManager.getColorTheme()
    val scaffoldState = rememberScaffoldState()
    // val context = LocalContext.current



    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                title = { Text(text = "Security Questions",color = Color.White, fontFamily = selectedFont) },
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
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                Spacer(modifier = Modifier.height(25.dp))
                Text(
                    text = "What is the name of your favourite movie?",
                    style = typography.subtitle2,
                    fontFamily = selectedFont,
                    color =  Color.White
                )
                Spacer(modifier = Modifier.height(3.dp))
                TextField(
                    value =answer,
                    onValueChange = { answer = it },
                    maxLines = 1,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(15.dp))
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                            viewModel.viewModelScope.launch(Dispatchers.IO) {
                                try {
                                    if(answer.isNotEmpty()) {
                                        val passwordManager = viewModel.passwordManager
                                        passwordManager.setQuestionAnswer(answer)
                                        onQuestionSet(answer)
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
                    //  enabled = isPasswordConfirmed,
                ) {
                    Text(text = "Done",style = typography.subtitle2, fontFamily = selectedFont)
                }

            }
        }
    )
}