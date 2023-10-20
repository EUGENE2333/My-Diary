package com.example.mydiary.presentation.compose.drawerComposables.about

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mydiary.MainActivity
import com.example.mydiary.R
import com.example.mydiary.presentation.DiaryViewModel
import com.example.mydiary.presentation.compose.mainComposables.fontSizeBasedOnFontTheme
import com.example.mydiary.presentation.compose.mainComposables.headerFontSizeBasedOnFontTheme

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun About(
    navController: NavController,
    viewModel: DiaryViewModel
){
    val scaffoldState = rememberScaffoldState()
    val mainActivity = (LocalContext.current as MainActivity)
    val selectedFont = viewModel.passwordManager.getFontTheme()
    val enabled = viewModel.enabledFlow.collectAsState(initial = false).value
    val head = stringResource(R.string.about_diary_header)
    val about = stringResource(R.string.about_diary)

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "About",
                        color = Color.White,
                        fontSize = headerFontSizeBasedOnFontTheme(selectedFont),
                        fontFamily = selectedFont
                    )
                },
                backgroundColor = Color(0xFF2C2428),
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "back", tint = Color.White)
                    }
                },
                actions = {
                    if (enabled) {
                    IconButton(onClick = {
                        mainActivity.speakText(head + about)
                    }) {

                        Image(
                            painter = painterResource(id = R.drawable.outlet),
                            contentDescription = null
                        )
                    }
                    }
                }

            )
        },
        content = {
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState()).fillMaxSize().padding(top = 20.dp)
            ) {

                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(vertical = 10.dp,horizontal = 10.dp).fillMaxWidth()
                ) {
                    Text(
                        text = head,
                        style = MaterialTheme.typography.h4,
                        fontFamily = selectedFont
                    )
                }

                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(vertical = 10.dp,horizontal = 10.dp).fillMaxWidth()
                ) {
                    Text(
                        text = about,
                        style = MaterialTheme.typography.body1,
                        fontSize = fontSizeBasedOnFontTheme(selectedFont),
                        fontFamily = selectedFont
                    )
                }
            }
        })
}