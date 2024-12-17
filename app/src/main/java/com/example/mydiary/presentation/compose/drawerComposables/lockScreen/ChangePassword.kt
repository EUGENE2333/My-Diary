package com.example.mydiary.presentation.compose.drawerComposables.lockScreen

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.mydiary.R
import com.example.mydiary.presentation.DiaryViewModel
import com.example.mydiary.presentation.compose.mainComposables.headerFontSizeBasedOnFontTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ChangePassword(
    navController: NavController,
    viewModel: DiaryViewModel = hiltViewModel(),

) {
    var password by remember { mutableStateOf("") }
    var passwordConfirmation by remember { mutableStateOf("") }
    var isPasswordConfirmed by remember { mutableStateOf(false) }
    var isPasswordSet by remember { mutableStateOf(false) }
    val selectedFont = viewModel.passwordManager.getFontTheme()
    val selectedColorTheme = viewModel.passwordManager.getColorTheme()
    val scaffoldState = rememberScaffoldState()
    var expanded by remember { mutableStateOf(false) }
    val passwordManager = viewModel.passwordManager



    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                title = { Text(
                    text = "Lock",
                    color = Color.White,
                    fontFamily = selectedFont,
                    fontSize =  headerFontSizeBasedOnFontTheme(selectedFont)
                ) },
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

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentSize(Alignment.TopEnd)
            ) {
                androidx.compose.material3.IconButton(onClick = { expanded = true }) {
                    androidx.compose.material3.Icon(
                        Icons.Default.MoreVert,
                        contentDescription = "Localized description",
                        tint = Color.White
                    )
                }
                androidx.compose.material3.DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    androidx.compose.material3.DropdownMenuItem(
                        text = { Text(text = "Remove Lock", color = Color.Black) },
                        onClick = {
                            viewModel.viewModelScope.launch(Dispatchers.IO) {
                                passwordManager.setPassword("null")
                                passwordManager.setQuestionAnswer("","")
                                withContext(Dispatchers.Main) {
                                    scaffoldState.snackbarHostState.showSnackbar(
                                        "App Lock has been removed"
                                    )
                                    navController.popBackStack()
                                }
                            }

                        },
                    )

                }
            }

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
                   color = Color.White,
                   style = MaterialTheme.typography.subtitle2,
                   fontFamily = selectedFont,

               )
                Spacer(modifier = Modifier.height(3.dp))
                PasswordTextField(
                    label = " New Password",
                    password = password,
                    onPasswordChanged = { password = it },
                )
                Spacer(modifier = Modifier.height(15.dp))

             Text(text = "Confirm Password",
                 color = Color.White,
                 style = MaterialTheme.typography.subtitle2,
                 fontFamily = selectedFont
                )

                Spacer(modifier = Modifier.height(3.dp))
                PasswordTextField(
                    label = "Confirm New Password",
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
                                    passwordManager.setPassword(password)
                                    passwordManager.lockApp()
                                    isPasswordSet = passwordManager.isPasswordSet()
                                    withContext(Dispatchers.Main) {
                                          scaffoldState.snackbarHostState.showSnackbar(
                                              " New Password has been set!!"
                                          )
                                        navController.popBackStack()
                                    }

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
                    Text(text = "Set New Password",style = MaterialTheme.typography.subtitle2, fontFamily = selectedFont)
                }

            }
        }
    )
}