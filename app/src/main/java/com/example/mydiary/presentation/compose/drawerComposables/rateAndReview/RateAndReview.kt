package com.example.mydiary.presentation.compose.drawerComposables.rateAndReview

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.mydiary.R
import com.example.mydiary.presentation.DiaryViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun RateAndReview(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: DiaryViewModel,
) {
    val scaffoldState = rememberScaffoldState()
    val selectedStars = remember { mutableStateOf(0) }
    val selected = remember { mutableStateOf(false) }
    val selectedFont = viewModel.passwordManager.getFontTheme()
    val selectedColorTheme = viewModel.passwordManager.getColorTheme()
    val context = LocalContext.current
    val packageName = context.packageName
    val scope = rememberCoroutineScope()

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Rate and Review",
                        color = Color.White,
                        fontSize = if(
                            selectedFont == FontFamily(Font(R.font.pizzat)) ||
                            selectedFont == FontFamily(Font(R.font.first_writing)) ||
                            selectedFont == FontFamily(Font(R.font.slimshoot)) ||
                            selectedFont == FontFamily(Font(R.font.twirly)) ||
                            selectedFont == FontFamily(Font(R.font.gnyrwn977))
                        ) 37.sp else 17.sp,
                        fontFamily = selectedFont
                    )
                },
                backgroundColor = Color(0xFF2C2428),
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.Filled.ArrowBack,
                            contentDescription = "back",
                            tint = Color.White
                        )
                    }
                }
            )
        },
        backgroundColor = selectedColorTheme,
        content = {
            Column(
                modifier = modifier
                    .padding(16.dp)
                    .padding(top = 20.dp)

            ) {

                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(vertical = 10.dp, horizontal = 10.dp).fillMaxWidth()
                ) {
                    Text(
                        text = "How do you feel about MyDiary?",
                        style = TextStyle(
                            fontSize = if(
                                selectedFont == FontFamily(Font(R.font.pizzat)) ||
                                selectedFont == FontFamily(Font(R.font.first_writing)) ||
                                selectedFont == FontFamily(Font(R.font.slimshoot)) ||
                                selectedFont == FontFamily(Font(R.font.twirly)) ||
                                selectedFont == FontFamily(Font(R.font.gnyrwn977))
                            ) 40.sp else 30.sp,

                            fontWeight = FontWeight.Normal,
                            fontFamily = selectedFont
                        ),
                        modifier = Modifier.padding(bottom = 18.dp),
                        color = Color.White
                    )
                }

                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(vertical = 20.dp, horizontal = 10.dp)
                        .padding(top = 40.dp)
                        .fillMaxWidth()
                ) {
                    repeat(5) { index ->


                        Icon(
                            imageVector = if (selected.value) Icons.Filled.Star else Icons.Outlined.Star,
                            contentDescription = "Star",
                            tint = if (index < selectedStars.value) Color.Yellow else Color.Gray,
                            modifier = Modifier.size(50.dp)
                                .clickable {
                                    selected.value = !selected.value
                                    selectedStars.value = index + 1
                                    if (selected.value) {
                                        if (index == 5) {
                                            val playStoreUri =
                                                Uri.parse("market://details?id=$packageName")
                                            val intent = Intent(Intent.ACTION_VIEW, playStoreUri)

                                            try {
                                                context.startActivity(intent)
                                            } catch (e: ActivityNotFoundException) {
                                                // Handle the case where Google Play Store app is not installed
                                                // Open the app page in the browser instead
                                                Log.e("RateAndReview", "NOT YET IN PLAY STORE")
                                                val browserIntent = Intent(
                                                    Intent.ACTION_VIEW,
                                                    Uri.parse("https://play.google.com/store/apps/details?id=$packageName")
                                                )
                                                context.startActivity(browserIntent)
                                                viewModel.viewModelScope.launch(Dispatchers.IO) {
                                                    scaffoldState.snackbarHostState.showSnackbar(
                                                        "Thank you for your support!"
                                                    )
                                                }
                                            }

                                        }
                                    }
                                }
                                .size(36.dp)
                                .padding(4.dp)
                        )
                    }
                }

                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(vertical = 30.dp, horizontal = 10.dp).fillMaxWidth()
                ) {
                    Text(
                        text = "It will mean a lot to us if you give us a 5 star rating in the" +
                                " Google Play Store.",
                        style = TextStyle(
                            fontSize = if(
                                selectedFont == FontFamily(Font(R.font.pizzat)) ||
                                selectedFont == FontFamily(Font(R.font.first_writing)) ||
                                selectedFont == FontFamily(Font(R.font.slimshoot)) ||
                                selectedFont == FontFamily(Font(R.font.twirly)) ||
                                selectedFont == FontFamily(Font(R.font.gnyrwn977))
                            ) 19.sp else 12.sp,

                            fontWeight = FontWeight.Normal,
                            fontFamily = selectedFont
                        ),
                        modifier = Modifier.padding(bottom = 18.dp),
                        color = Color.White
                    )
                }

                Button(
                    onClick = {
                        val playStoreUri = Uri.parse("market://details?id=$packageName")
                        val intent = Intent(Intent.ACTION_VIEW, playStoreUri)

                        try {
                            context.startActivity(intent)
                        } catch (e: ActivityNotFoundException) {
                            // Handle the case where Google Play Store app is not installed
                            // Open the app page in the browser instead
                            Log.e("RateAndReview", "NOT YET IN PLAY STORE")

                            /*
                            val browserIntent = Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse("https://play.google.com/store/apps/details?id=$packageName")
                            )
                            context.startActivity(browserIntent)
                            viewModel.viewModelScope.launch(Dispatchers.IO) {
                                scaffoldState.snackbarHostState.showSnackbar(
                                    "Password has been set!!"
                                )
                            } */
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Rate and Review",
                        fontSize = if(
                            selectedFont == FontFamily(Font(R.font.pizzat)) ||
                            selectedFont == FontFamily(Font(R.font.first_writing)) ||
                            selectedFont == FontFamily(Font(R.font.slimshoot)) ||
                            selectedFont == FontFamily(Font(R.font.twirly)) ||
                            selectedFont == FontFamily(Font(R.font.gnyrwn977))
                        ) 27.sp else 19.sp,

                        fontFamily = selectedFont
                    )
                }

                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(horizontal = 10.dp).padding(top = 40.dp)
                        .fillMaxWidth()
                ) {
                    Text(
                        text = "Thank you for your support",
                        style = TextStyle(
                            fontSize = if(
                                selectedFont == FontFamily(Font(R.font.pizzat)) ||
                                selectedFont == FontFamily(Font(R.font.first_writing)) ||
                                selectedFont == FontFamily(Font(R.font.slimshoot)) ||
                                selectedFont == FontFamily(Font(R.font.twirly)) ||
                                selectedFont == FontFamily(Font(R.font.gnyrwn977))
                            ) 19.sp else 12.sp,

                            fontWeight = FontWeight.Normal,
                            fontFamily = selectedFont
                        ),
                        color = Color.White
                    )
                }

                // Feedback dialog
                if (selected.value) {
                    if (selectedStars.value <= 4 && selectedStars.value != 0) {

                        AlertDialog(
                            onDismissRequest = { /* Handle dialog dismiss if needed */ navController.popBackStack() },
                            title = { Text(text = "Feedback", fontFamily = selectedFont) },
                            text = {
                                Text(
                                    text = "Would you like to provide feedback?",
                                    fontFamily = selectedFont
                                )
                            },
                            confirmButton = {
                                Button(
                                    onClick = {
                                        val playStoreUri =
                                            Uri.parse("market://details?id=$packageName")
                                        val intent = Intent(Intent.ACTION_VIEW, playStoreUri)
                                        try {
                                            context.startActivity(intent)
                                        } catch (e: ActivityNotFoundException) {
                                            // Handle the case where Google Play Store app is not installed
                                            // Open the app page in the browser instead
                                            Log.e("RateAndReview", "NOT YET IN PLAY STORE")
                                        }

                                    }
                                ) {
                                    Text(text = "Yes", fontFamily = selectedFont)
                                }
                            },
                            dismissButton = {
                                Button(
                                    onClick = { /* Handle dismiss if needed */ navController.popBackStack() }
                                ) {
                                    Text(text = "No", fontFamily = selectedFont)
                                }
                            }
                        )

                    } else {
                        val playStoreUri =
                            Uri.parse("market://details?id=$packageName")
                        val intent = Intent(Intent.ACTION_VIEW, playStoreUri)

                        try {
                            context.startActivity(intent)
                        } catch (e: ActivityNotFoundException) {
                            // Handle the case where Google Play Store app is not installed
                            // Open the app page in the browser instead
                            Log.e("RateAndReview", "NOT YET IN PLAY STORE")
                            val browserIntent = Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse("https://play.google.com/store/apps/details?id=$packageName")
                            )
                            context.startActivity(browserIntent)
                            // viewModel.viewModelScope.launch(Dispatchers.IO) {
                            //   scaffoldState.snackbarHostState.showSnackbar(
                            //      "Thank you for your support!"
                            //  )
                        }
                    }

                }
            }

        })
}

