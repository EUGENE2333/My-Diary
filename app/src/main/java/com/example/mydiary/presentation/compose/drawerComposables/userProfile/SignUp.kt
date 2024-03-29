package com.example.mydiary.presentation.compose.drawerComposables.userProfile

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.mydiary.presentation.DiaryViewModel
import com.example.mydiary.presentation.compose.drawerComposables.lockScreen.PasswordTextField
import com.example.mydiary.presentation.compose.navigation.Screen
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun SignUpPage(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: DiaryViewModel = hiltViewModel(),
    onNavigateToDiaryList : () -> Unit
) {
    val loginUiState = viewModel.loginUiState
    val isError = loginUiState.signUpError != null
    val context = LocalContext.current
    val scaffoldState = rememberScaffoldState()

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                title = {
                  Text(text = "My Diary", color = Color.White)
                },
                backgroundColor = Color(0xFF2C2428),
            )
        },
        content = {

            if (loginUiState.isLoading) {
                CircularProgressIndicator()
            }

            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Sign Up",
                    style = MaterialTheme.typography.h3,
                    fontWeight = FontWeight.Black,
                    color = MaterialTheme.colors.primary,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                if (isError) {
                    Text(text = loginUiState.signUpError ?: "Unknown error", color = Color.Red)
                }
                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = loginUiState.userNameSignUp,
                    onValueChange = { viewModel.onUserNameSignUpChange(it) },
                    leadingIcon = { Icons.Default.Person },
                    label = { Text("Email") },
                    isError = isError,
                    placeholder = { Text("example@gmail.com") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )
                Spacer(modifier = Modifier.height(7.dp))
                Text(text = "enter password:")
                PasswordTextField(
                    password = loginUiState.passwordSignUp,
                    onPasswordChanged = { viewModel.onPasswordSignUpChange(it) },
                    label = "Password"

                )

                Spacer(modifier = Modifier.height(7.dp))
                Text(
                    text = "confirm password:",
                    modifier = Modifier

                )
                PasswordTextField(
                    password = loginUiState.confirmPasswordSignUp,
                    onPasswordChanged = { viewModel.onConfirmPasswordSignUpChange(it) },
                    label = "Password"

                )
                Spacer(modifier = Modifier.height(10.dp))
                Button(
                    onClick = {
                        viewModel.viewModelScope.launch {
                            viewModel.createUser(context)
                            delay(1000)
                            if (loginUiState.isSuccessLogin) {
                                onNavigateToDiaryList.invoke()
                                viewModel.passwordManager.setPassword("null")
                            }
                        }
                    },
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Text(
                        text = "Sign Up",
                        style = MaterialTheme.typography.subtitle2,
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Already have an account?"
                    )
                    TextButton(onClick = { navController.navigate(Screen.SignInPage.route) }) {
                        Text(
                            text = "Log in",
                            textDecoration = TextDecoration.Underline
                        )

                    }
                }


            }
        }
    )
}
