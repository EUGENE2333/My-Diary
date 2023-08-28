package com.example.mydiary.presentation.compose.drawerComposables.lockScreen

import android.annotation.SuppressLint
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.example.mydiary.presentation.DiaryViewModel
import com.example.mydiary.presentation.compose.navigation.Screen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@RequiresApi(value = 26)
@Composable
fun LockScreenScreen(
    viewModel: DiaryViewModel,
    navController: NavHostController,
) {
    val passwordManager = viewModel.passwordManager

    // Declare a mutable state variable to hold the entered password
    var enteredPassword by remember { mutableStateOf("") }
    val scaffoldState = rememberScaffoldState()

    Scaffold(
        scaffoldState = scaffoldState,
        content = {


            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = Color.Cyan),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                PasswordTextField(
                    label = "Enter Password",
                    password = enteredPassword,
                    onPasswordChanged = { enteredPassword = it }
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        try {
                            if (passwordManager.unlockApp(enteredPassword)) {

                                navController.navigate(Screen.DiaryList.route){
                                popUpTo(route = Screen.LockScreenScreen.route) {
                                    inclusive = true
                                }
                            }


                            } else {
                                // Show error message or handle incorrect password
                                viewModel.viewModelScope.launch(Dispatchers.Main) {
                                    scaffoldState.snackbarHostState.showSnackbar(
                                        "Wrong password"
                                    )
                                }
                            }
                        } catch (e: Exception) {
                            Log.e("LockScreenScreen", "Error confirming password: $e")
                            // Show error message or handle exception
                        }
                    }
                ) {
                    Text("Unlock")
                }
                Spacer(modifier = Modifier.height(10.dp))
                Text(text = "Forgot password?",
                    style = MaterialTheme.typography.subtitle2,
                    modifier = Modifier.clickable{
                        if (passwordManager.isQuestionAnswerSet()) {
                            navController.navigate(Screen.ForgotPassword.route)
                        } else{
                            viewModel.viewModelScope.launch(Dispatchers.Main) {
                                scaffoldState.snackbarHostState.showSnackbar(
                                    "You don't have a security Question set."
                                )
                            }
                        }
                    }
                )
                Spacer(modifier = Modifier.height(10.dp))
            }
        })
}