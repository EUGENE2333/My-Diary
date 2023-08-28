package com.example.mydiary.presentation.compose.drawerComposables.lockScreen

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.mydiary.R
import com.example.mydiary.presentation.DiaryViewModel
import com.example.mydiary.presentation.compose.navigation.Screen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch



@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun LockScreen(
    navController: NavController,
    viewModel: DiaryViewModel,
    onPasswordSet: (String) -> Unit,
// onPasswordVerified: (Boolean) -> Unit
) {
    var password by remember { mutableStateOf("") }
    var passwordConfirmation by remember { mutableStateOf("") }
    var isPasswordConfirmed by remember { mutableStateOf(false) }
    var isPasswordSet by remember { mutableStateOf(false) }
    val selectedFont = viewModel.passwordManager.getFontTheme()
    val selectedColorTheme = viewModel.passwordManager.getColorTheme()
    val scaffoldState = rememberScaffoldState()
    // val context = LocalContext.current



    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                title = { Text(text = "Set Password",color = Color.White, fontFamily = selectedFont) },
                backgroundColor = Color(0xFF2C2428),
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
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
                Image(
                    painter = painterResource(id = R.drawable.lock_icon),
                    contentDescription = "Profile Image",
                    modifier = Modifier
                        .size(90.dp)
                        .clip(CircleShape)
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 10.dp)

                )
                Spacer(modifier = Modifier.height(3.dp))
                Text(
                    text = "Enter New Password",
                    style = typography.subtitle2,
                    fontFamily = selectedFont,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(3.dp))
                PasswordTextField(
                    label = "Password",
                    password = password,
                    onPasswordChanged = { password = it },
                )
                Spacer(modifier = Modifier.height(15.dp))
                Text(text = "Confirm Password",style = typography.subtitle2, fontFamily = selectedFont)
                Spacer(modifier = Modifier.height(3.dp))
                PasswordTextField(
                    label = "Confirm Password",
                    password = passwordConfirmation,
                    onPasswordChanged = {
                        passwordConfirmation = it
                        isPasswordConfirmed = it == password
                    }
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        if (isPasswordConfirmed) {
                            viewModel.viewModelScope.launch(Dispatchers.IO) {
                                try {
                                    val passwordManager = viewModel.passwordManager
                                    passwordManager.setPassword(password)
                                    onPasswordSet(password)
                                    passwordManager.lockApp()
                                    isPasswordSet = passwordManager.isPasswordSet()
                                  /*  withContext(Dispatchers.Main) {
                                        scaffoldState.snackbarHostState.showSnackbar(
                                            "Password has been set!!"
                                        )
                                     //   navController.popBackStack()
                                    } */
                                } catch (e: Exception) {
                                    // Handle any exceptions that may occur
                                    Log.e("LockScreen", "Error setting password: $e")
                                }
                            }
                        }
                        else{
                            viewModel.viewModelScope.launch(Dispatchers.Main) {
                                scaffoldState.snackbarHostState.showSnackbar(
                                    "Password Mismatch!"
                                )
                            }
                        }
                    },
                  //  enabled = isPasswordConfirmed,
                ) {
                    Text(
                        text = "Set Password",
                        style = typography.subtitle2,
                        fontFamily = selectedFont,
                        color = Color.White
                    )
                }

            }
            if (isPasswordSet) {
                AlertDialog(
                    onDismissRequest = { /* Handle dialog dismiss if needed */ navController.popBackStack() },
                    title = {
                        androidx.compose.material3.Text(
                            text = "Add Security Question",
                            fontFamily = selectedFont
                        )
                    },
                    text = {
                        androidx.compose.material3.Text(
                            text = "Password has been set successfully! \nPlease add security question" +
                                    " to be used to recover your password.",
                            fontFamily = selectedFont
                        )
                    },
                    confirmButton = {
                        androidx.compose.material3.Button(
                            onClick = {
                                // Navigate to the feedback screen
                                viewModel.viewModelScope.launch(Dispatchers.Main) {
                                    navController.navigate(Screen.SecurityQuestions.route)
                                }
                            }
                        ) {
                            androidx.compose.material3.Text(text = "Add", fontFamily = selectedFont)
                        }
                    },
                    dismissButton = {
                        androidx.compose.material3.Button(
                            onClick = { /* Handle dismiss if needed */ navController.popBackStack() }
                        ) {
                            androidx.compose.material3.Text(text = "No", fontFamily = selectedFont)
                        }
                    }
                )
            }
        }
    )
}