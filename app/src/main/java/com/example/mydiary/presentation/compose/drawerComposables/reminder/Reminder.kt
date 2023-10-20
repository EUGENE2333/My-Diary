package com.example.mydiary.presentation.compose.drawerComposables.reminder

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.mydiary.MainActivity
import com.example.mydiary.R
import com.example.mydiary.presentation.DiaryViewModel
import com.example.mydiary.presentation.compose.mainComposables.fontSizeBasedOnFontTheme
import com.example.mydiary.presentation.compose.mainComposables.headerFontSizeBasedOnFontTheme
import kotlinx.coroutines.launch
import java.util.*


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun Reminder(
    navController: NavController,
    viewModel: DiaryViewModel

) {
    var sliderPosition by remember { mutableStateOf(0f) }
    val mainActivity = (LocalContext.current as MainActivity)
    val selectedFont = viewModel.passwordManager.getFontTheme()
    val scaffoldState = rememberScaffoldState()

    val diaryDescription = stringResource(R.string.diary_description)

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Set Reminder",
                        color = Color.White,
                        fontSize = headerFontSizeBasedOnFontTheme(selectedFont),
                        fontFamily = selectedFont
                    )
                },
                backgroundColor = Color(0xFF2C2428),
                navigationIcon = {
                    IconButton(onClick = {navController.popBackStack()}) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "back", tint = Color.White)
                    }
                }
            )
        },
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = Color(0xFFA53E97), shape = RectangleShape)
                    .padding(top = 30.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.notifications_icon),
                    contentDescription = "Profile Image",
                    modifier = Modifier
                        .size(90.dp)
                        .clip(CircleShape)
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 10.dp)

                )
                     Row(
                         horizontalArrangement = Arrangement.Center,
                     modifier = Modifier.padding(vertical = 10.dp).fillMaxWidth()
                     ) {
                         Text(
                             text = "Set a reminder to open diary",
                             style = MaterialTheme.typography.subtitle1,
                             color = Color.White,
                             fontSize = headerFontSizeBasedOnFontTheme(selectedFont),
                             fontFamily = selectedFont
                         )
                     }

                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(vertical = 10.dp,horizontal = 10.dp).fillMaxWidth()
                ) {
                    Text(
                        text = diaryDescription,
                        style = MaterialTheme.typography.subtitle2,
                        fontSize =fontSizeBasedOnFontTheme(selectedFont),
                        color = Color.White,
                        fontFamily = selectedFont
                    )
                }
                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(vertical = 7.dp).fillMaxWidth()
                ) {
                    Text(
                        text = if(sliderPosition.toInt() == 1)
                            sliderPosition.toInt().toString() + " " + "day"
                               else
                            sliderPosition.toInt().toString() + " " + "days",
                        color = Color.White,
                        fontFamily = selectedFont,
                        fontSize = fontSizeBasedOnFontTheme(selectedFont)
                    )
                }


                Slider(
                    modifier = Modifier.semantics { contentDescription = "Localized Description" }
                        .padding(horizontal = 15.dp),
                    value = sliderPosition,
                    onValueChange = { sliderPosition = it },
                    valueRange = 0f..7f,
                  //  steps = 4,
                    colors = SliderDefaults.colors(activeTickColor = Color.White, thumbColor = Color.Red)

                )
                    Row (
                        modifier = Modifier.padding(vertical = 10.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically

                            ){
                        // Button to set the reminder and show a notification
                        Button(
                            onClick = {
                                val days = sliderPosition.toLong()
                                val title = "Reminder"
                                val message = "Come and write your experiences and ideas!"
                                mainActivity.createForegroundService(title, message, days)

                                viewModel.viewModelScope.launch {
                                    scaffoldState.snackbarHostState.showSnackbar("Reminder scheduled to " + sliderPosition.toInt().toString() + "days")
                                }
                            },
                            modifier = Modifier.padding(start= 100.dp)
                        ) {
                            Text(
                                "Set Reminder",
                                fontFamily = selectedFont,
                                fontSize = fontSizeBasedOnFontTheme(selectedFont)
                            )
                        }
                    }

            }
        }
    )
}
