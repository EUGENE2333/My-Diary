package com.example.mydiary.presentation.compose.drawerComposables.userProfile

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Scaffold
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.mydiary.R
import com.example.mydiary.presentation.DiaryViewModel
import com.example.mydiary.presentation.compose.mainComposables.headerFontSizeBasedOnFontTheme
import com.example.mydiary.presentation.compose.mainComposables2.home.HomeViewModel


@Composable
fun UserProfile(
    navController: NavController,
    viewModel: DiaryViewModel,
    homeViewModel: HomeViewModel?,
    navToSignUpPage: () -> Unit,
    navToLoginPage: () -> Unit
) {

    val scaffoldState = rememberScaffoldState()
    val selectedFont = viewModel.passwordManager.getFontTheme()
    val selectedColorTheme = viewModel.passwordManager.getColorTheme()
    val userEmail = homeViewModel?.user?.email.toString()
    var expanded by remember { mutableStateOf(false) }
    val context = LocalContext.current

    Scaffold(
        snackbarHost = {

        },
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(title = {
                Text(
                    text = "User Profile",
                    fontFamily = selectedFont,
                    color = Color.White,
                    fontSize =  headerFontSizeBasedOnFontTheme(selectedFont)
                )
            },
                backgroundColor = Color(0xFF2C2428),
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "back",tint = Color.White)
                    }
                },
            )
        },
        backgroundColor = selectedColorTheme,
        content = { paddingValues ->

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentSize(Alignment.TopEnd)
            ) {
                IconButton(onClick = { expanded = true }) {
                    Icon(Icons.Default.MoreVert, contentDescription = "Localized description", tint = Color.White)
                }
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("Sign out") },
                        onClick = {
                            homeViewModel?.signOut()
                            navToLoginPage.invoke()
                        },
                    )
                    DropdownMenuItem(
                        text = { Text("Delete Account") },
                        onClick = {
                            homeViewModel?.deleteAccount(context,navToSignUpPage)
                        },
                    )

                }
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Spacer(modifier = Modifier.width(255.dp))

                Image(
                    painter = painterResource(id = R.drawable.cloud_icon),
                    contentDescription = "Profile Image",
                    modifier = Modifier
                        .size(140.dp)
                        .clip(CircleShape)
                      //  .align(Alignment.CenterHorizontally)
                )

                Text(
                    text = userEmail,
                    fontFamily = FontFamily.Default,
                    fontSize = 21.sp,
                  //  modifier = Modifier.align(Alignment.CenterHorizontally),
                    color = Color.White
                )

                Text(
                    text = "Your account is active.",
                    fontFamily = FontFamily.Default,
                    fontSize = 10.sp,
                 //   modifier = Modifier.align(Alignment.CenterHorizontally),
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(23.dp))
            }
        })
}












