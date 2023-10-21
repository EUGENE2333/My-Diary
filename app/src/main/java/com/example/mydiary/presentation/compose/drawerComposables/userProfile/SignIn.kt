package com.example.mydiary.presentation.compose.drawerComposables.userProfile


import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.mydiary.presentation.DiaryViewModel
import com.example.mydiary.presentation.compose.drawerComposables.lockScreen.PasswordTextField
import com.example.mydiary.presentation.compose.navigation.Screen
import kotlinx.coroutines.launch



@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun SignInPage(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: DiaryViewModel,
    onNavigateToNoteHomeScreen:() -> Unit,
    onNavigateToDiaryHomeScreen:() -> Unit
) {
    val loginUiState = viewModel.loginUiState
    val isError = loginUiState.loginError != null
    val context = LocalContext.current
    val scaffoldState = rememberScaffoldState()
    val isNoteFormat by viewModel.isNoteFormat.collectAsState(initial = false)

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(title = {
                androidx.compose.material.Text(text = "MyDiary", color = Color.White)
            },
                backgroundColor = Color(0xFF2C2428),

            )
        },
        content = {

            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Log in to an existing Account",
                   fontSize = 20.sp,
                    fontWeight = FontWeight.Black,
                    color = MaterialTheme.colors.primary,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                if(isError){
                    Text(text = loginUiState.loginError ?: "Unknown error",color = Color.Red)
                }
                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = loginUiState.userName,
                    onValueChange = { viewModel.onUserNameChange(it) },
                    leadingIcon ={ Icons.Default.Person },
                    label = { Text("Email") },
                    isError = isError,
                    placeholder = { Text("example@gmail.com") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )
                Spacer(modifier = Modifier.height(7.dp))
                 Text(
                     text = "enter password:",
                     modifier = Modifier
                 )
                PasswordTextField(
                    password = loginUiState.password,
                    onPasswordChanged ={viewModel.onPasswordChange(it)},
                    label ="Password",

                    )
                Spacer(modifier = Modifier.height(10.dp))
                Button(
                    onClick = {
                        viewModel.viewModelScope.launch {
                        viewModel.loginUser(context)
                                if(loginUiState.isSuccessLogin) {
                                    if(isNoteFormat){
                                        onNavigateToNoteHomeScreen.invoke()
                                    }else{
                                        onNavigateToDiaryHomeScreen.invoke()
                                    }
                                }

                        }

                              },
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Text(
                        text = "Log In",
                        style = typography.subtitle2,

                        )
                }
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                  Text(
                      text = "Don't have an account?"
                  )
                    TextButton(onClick = {navController.navigate(Screen.SignUpPage.route)}) {
                         Text(
                             text = "SignUp",
                             textDecoration = TextDecoration.Underline
                         )
                        
                    }
                }
                if(loginUiState.isLoading){
                    CircularProgressIndicator()
                }

            }
        })
}
