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
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.mydiary.MainActivity
import com.example.mydiary.R
import com.example.mydiary.presentation.DiaryViewModel
import kotlinx.coroutines.launch
import java.util.*


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun Reminder(
    navController: NavController,
    viewModel: DiaryViewModel

) {
    var sliderPosition by remember { mutableStateOf(0f) }
    // Maintain the selected reminder time using mutableStateOf
    val selectedReminderTime = remember { mutableStateOf(0) }
    val message = "Come and write your experiences and ideas!"
    val mainActivity = (LocalContext.current as MainActivity)
    val selectedFont = viewModel.passwordManager.getFontTheme()
  //  val message = remember { mutableStateOf("Don't forget to open your diary!") }
  //  val scheduler = AndroidAlarmScheduler(context)
  //  var alarmItem: AlarmItem? = null

    val scaffoldState = rememberScaffoldState()

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Set Reminder",
                        color = Color.White,
                        fontSize = if(selectedFont == FontFamily(Font(R.font.arabia))) 47.sp else  26.sp,
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
                             fontSize = if(selectedFont == FontFamily(Font(R.font.arabia))) 47.sp else  26.sp,
                             fontFamily = selectedFont
                         )
                     }

                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(vertical = 10.dp,horizontal = 10.dp).fillMaxWidth()
                ) {
                    Text(
                        text = "This Diary Application is a vault for your daily life experiences,"+
                                " ideas, and journal.Writing down is a therapy on it's own way! Set a"+
                                " reminder to update your diary !",
                        style = MaterialTheme.typography.subtitle2,
                        fontSize = if(selectedFont == FontFamily(Font(R.font.arabia))) 37.sp else  16.sp,
                        color = Color.White,
                        fontFamily = selectedFont
                    )
                }
                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(vertical = 7.dp).fillMaxWidth()
                ) {
                    Text(
                        text =  "weeeee" + sliderPosition.toInt().toString(),
                        color = Color.White,
                        fontFamily = selectedFont,
                        fontSize = if(selectedFont == FontFamily(Font(R.font.arabia))) 47.sp else  26.sp,
                    )
                }


                Slider(
                    modifier = Modifier.semantics { contentDescription = "Localized Description" }
                        .padding(horizontal = 15.dp),
                    value = sliderPosition,
                    onValueChange = { sliderPosition = it },
                    valueRange = 0f..130f,
                  //  steps = 4,
                    colors = SliderDefaults.colors(activeTickColor = Color.White, thumbColor = Color.Red)

                )
                    Row (
                        modifier = Modifier.padding(vertical = 10.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically

                            ){
                        // Button to set the reminder and show a notification
                        Button(
                            onClick = {
                             /*   alarmItem = AlarmItem(
                                  time = LocalDateTime.now()
                                        .plusSeconds(sliderPosition.toLong())

                                )
                                alarmItem?.let(scheduler::schedule) */
                                //     setReminder(selectedReminderTime.value, context)
                             //   val time = LocalDateTime.now().plusSeconds(sliderPosition.toLong())
                                val delayInSeconds = sliderPosition.toLong()
                                val title = "Reminder"
                                val message = "Come and write your experiences and ideas!"
                                mainActivity.createForegroundService(title, message, delayInSeconds)

                                viewModel.viewModelScope.launch {
                                    scaffoldState.snackbarHostState.showSnackbar("Reminder scheduled to " + sliderPosition.toInt().toString() + "days")
                                }
                            },
                            modifier = Modifier.padding(10.dp)
                        ) {
                            Text(
                                "Set Reminder",
                                fontFamily = selectedFont,
                                fontSize = if(selectedFont == FontFamily(Font(R.font.arabia))) 27.sp else  26.sp,
                            )
                        }
                   /*     Button(
                            onClick = {alarmItem?.let (scheduler::cancel)  }
                        ) {
                            Text("Cancel Reminder")
                        }  */
                    }

            }
        }
    )
}
