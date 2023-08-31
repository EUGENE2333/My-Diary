package com.example.mydiary.presentation.compose.drawerComposables.lockScreen

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.mydiary.presentation.DiaryViewModel
import com.example.mydiary.presentation.compose.mainComposables.headerFontSizeBasedOnFontTheme
import com.example.mydiary.presentation.compose.navigation.Screen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@OptIn(ExperimentalFoundationApi::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ForgotPassword(
    navController: NavController,
    viewModel: DiaryViewModel
) {
    var answer by remember { mutableStateOf("") }
    var isAnswerConfirmed by remember { mutableStateOf(false) }
    val savedPassword = viewModel.passwordManager.getPassword().toString()
    val selectedFont = viewModel.passwordManager.getFontTheme()
    val scaffoldState = rememberScaffoldState()
    val coroutineScope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current
    val bringIntoViewRequester = BringIntoViewRequester()



    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Security Question",
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


        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                Spacer(modifier = Modifier.height(25.dp))
                Text(text = "What is the name of your favourite movie?",style = typography.subtitle2,fontFamily = selectedFont)
                Spacer(modifier = Modifier.height(3.dp))
                TextField(
                    value =answer,
                    onValueChange = {answer = it },
                    maxLines = 1,
                    modifier = Modifier
                        .fillMaxWidth()
                        .onFocusEvent { event ->
                            if(event.isFocused) {
                                coroutineScope.launch {
                                    bringIntoViewRequester.bringIntoView()
                                }
                            } },
                    keyboardOptions = KeyboardOptions( imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions( onDone = {focusManager.clearFocus()})
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        viewModel.viewModelScope.launch(Dispatchers.IO) {
                            try {
                                val passwordManager = viewModel.passwordManager
                                if (passwordManager.verifyQuestionAnswer(answer)){
                                    isAnswerConfirmed = true
                                }else{
                                    withContext(Dispatchers.Main) {
                                        scaffoldState.snackbarHostState.showSnackbar(
                                            "Wrong answer try again!"
                                        )
                                    }
                                }

                            } catch (e: Exception) {
                                // Handle any exceptions that may occur
                                Log.e(TAG, "Error getting Security question answer: $e")
                            }
                        }
                    },
                    //  enabled = isPasswordConfirmed,
                ) {
                    Text(text = "Done",style = typography.subtitle2, fontFamily = selectedFont)
                }
                Spacer(modifier = Modifier.height(15.dp))

                 if(isAnswerConfirmed){
                     Text(
                         text = "Your password is: $savedPassword",
                         fontSize = 22.sp,
                         style = typography.body1,
                         modifier = Modifier.bringIntoViewRequester(bringIntoViewRequester),
                     )
                     Spacer(modifier = Modifier.height(15.dp))
                     Button(
                         onClick = {navController.popBackStack()},
                     modifier = Modifier.bringIntoViewRequester(bringIntoViewRequester),
                     ) {
                         Text(text = "OK",style = typography.subtitle2, fontFamily = selectedFont)
                     }
                 }

            }
        }
    )
}