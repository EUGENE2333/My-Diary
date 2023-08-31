package com.example.mydiary.presentation.compose.mainComposables

import android.content.Intent
import android.content.res.Configuration
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Divider
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.mydiary.R
import com.example.mydiary.presentation.DiaryViewModel
import com.example.mydiary.presentation.compose.navigation.Screen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@Composable
fun DrawerContent(
navController: NavController,
viewModel: DiaryViewModel,
) {
    val context = LocalContext.current
    val selectedColorTheme = viewModel.passwordManager.getColorTheme()
    val selectedFontTheme = viewModel.passwordManager.getFontTheme()
    val enabled by viewModel.enabledFlow.collectAsState(initial = false)
    val isLandscape = LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE




    val shareIntent = remember {
        Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, "Check out this amazing app!")
            type = "text/plain"
        }
    }


    Column(
        modifier = if (isLandscape)
            Modifier
                .verticalScroll(rememberScrollState())
                .fillMaxSize()
        else
            Modifier.fillMaxSize()

    ) {

        /**PROFILE */

        Column(modifier = Modifier
            .fillMaxWidth()
            .clickable { navController.navigate(Screen.UserProfile.route) }  // call it from a background threat or hoist it
            .background(color = selectedColorTheme, shape = RectangleShape)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 25.dp, start = 6.dp, bottom = 5.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center

            )
            {
                // Add profile image and name
                Image(
                    painter = painterResource(id = R.drawable.profile_image),
                    contentDescription = "Profile Image",
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)

                )
                Text(
                    text = "Account",
                    fontSize = if(
                        selectedFontTheme == FontFamily(Font(R.font.pizzat)) ||
                        selectedFontTheme == FontFamily(Font(R.font.first_writing)) ||
                        selectedFontTheme == FontFamily(Font(R.font.slimshoot)) ||
                        selectedFontTheme == FontFamily(Font(R.font.twirly)) ||
                        selectedFontTheme == FontFamily(Font(R.font.gnyrwn977))
                    ) 45.sp else  26.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontFamily = selectedFontTheme,
                    modifier = Modifier
                        .padding(top = 23.dp, start = 25.dp)
                )
            }


            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp)
                    .width(10.dp),

                color = Color.White
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color(0xFF2C2428), shape = RectangleShape)
                .padding(top = 16.dp)
        ) {

            /**LOCK SCREEN */

            Column(modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    if(viewModel.passwordManager.getPassword() == "null") {
                        navController.navigate(Screen.LockScreen.route)
                    } else{
                        navController.navigate(Screen.ChangePassword.route)
                    }
                }  // call it from a background threat or hoist it
            ) {
                // Add some other fields in the drawer
                Row(
                    modifier = Modifier
                        .padding(start = 20.dp)
                        .padding(bottom = 8.dp)
                ) {

                    Image(
                        painter = painterResource(id = R.drawable.lock_icon),
                        contentDescription = "Lock Image",
                        modifier = Modifier
                            .padding(top = 16.dp)
                            .padding(horizontal = 6.dp),

                    )
                    Text(
                        text = if (viewModel.passwordManager.getPassword() == "null") " Add Lock" else "Change Lock",
                        fontSize = if(
                            selectedFontTheme == FontFamily(Font(R.font.pizzat)) ||
                            selectedFontTheme == FontFamily(Font(R.font.first_writing)) ||
                            selectedFontTheme == FontFamily(Font(R.font.slimshoot)) ||
                            selectedFontTheme == FontFamily(Font(R.font.twirly)) ||
                            selectedFontTheme == FontFamily(Font(R.font.gnyrwn977))
                        ) 26.sp else  16.sp,
                        modifier = Modifier
                                .padding(top = 16.dp)
                                .padding(start = 15.dp),
                        color = Color.White,
                        fontFamily = selectedFontTheme,
                    )
                }


            }

            /**COLOR & STYLE */

            Column(modifier = Modifier
                .fillMaxWidth()
                .clickable { navController.navigate(Screen.ColorAndStyle.route) }  // call it from a background threat or hoist it
            ) {
                Row(
                    modifier = Modifier
                        .padding(start = 20.dp)
                        .padding(bottom = 8.dp)
                ) {

                    Image(
                        painter = painterResource(id = R.drawable.color_icon),
                        contentDescription = "Colors and style Image",
                        modifier = Modifier
                            .padding(top = 16.dp)
                            .padding(horizontal = 6.dp)


                    )

                    Text(
                        text = "Colors and style",
                        fontSize = if(
                            selectedFontTheme == FontFamily(Font(R.font.pizzat)) ||
                            selectedFontTheme == FontFamily(Font(R.font.first_writing)) ||
                            selectedFontTheme == FontFamily(Font(R.font.slimshoot)) ||
                            selectedFontTheme == FontFamily(Font(R.font.twirly)) ||
                            selectedFontTheme == FontFamily(Font(R.font.gnyrwn977))
                                ) 26.sp else  16.sp,
                        color = Color.White,
                        fontFamily = selectedFontTheme,
                        modifier = Modifier
                                .padding(top = 16.dp)
                                .padding(start = 15.dp),

                    )
                }

            }

            /** REMINDER */

            Column(modifier = Modifier
                .fillMaxWidth()
                .clickable { navController.navigate(Screen.Reminder.route) }  // call it from a background threat or hoist it
            ) {
                Row(
                    modifier = Modifier
                        .padding(start = 20.dp)
                        .padding(bottom = 8.dp)
                ) {

                    Image(
                        painter = painterResource(id = R.drawable.add_alert),
                        contentDescription = "Reminder Image",
                        modifier = Modifier
                            .padding(top = 16.dp)
                            .padding(horizontal = 6.dp)

                    )

                    Text(
                        text = "Reminder",
                        fontSize = if(
                            selectedFontTheme == FontFamily(Font(R.font.pizzat)) ||
                            selectedFontTheme == FontFamily(Font(R.font.first_writing)) ||
                            selectedFontTheme == FontFamily(Font(R.font.slimshoot)) ||
                            selectedFontTheme == FontFamily(Font(R.font.twirly)) ||
                            selectedFontTheme == FontFamily(Font(R.font.gnyrwn977))
                        ) 26.sp else  16.sp,

                        color = Color.White,
                        fontFamily = selectedFontTheme,
                        modifier = Modifier
                                .padding(top = 16.dp)
                                .padding(start = 15.dp),

                    )
                }

            }


            /** TEXT TO SPEECH */

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                //   .clickable { navController.navigate(Screen.Reminder.route) }  // call it from a background threat or hoist it
            ) {
                Row(
                    modifier = Modifier
                        .padding(start = 20.dp)
                        .padding(bottom = 1.dp)
                ) {

                    Image(
                        painter = painterResource(id = R.drawable.voice_over),
                        contentDescription = "Text to Speech",
                        modifier = Modifier
                            .padding(top = 16.dp)
                            .padding(horizontal = 6.dp)
                    )
                    Row {
                        Text(
                            text = "Text to Speech",
                            fontSize = if(
                                selectedFontTheme == FontFamily(Font(R.font.pizzat)) ||
                                selectedFontTheme == FontFamily(Font(R.font.first_writing)) ||
                                selectedFontTheme == FontFamily(Font(R.font.slimshoot)) ||
                                selectedFontTheme == FontFamily(Font(R.font.twirly)) ||
                                selectedFontTheme == FontFamily(Font(R.font.gnyrwn977))
                            ) 26.sp else  16.sp,

                            color = Color.White,
                            fontFamily = selectedFontTheme,

                            modifier = Modifier
                                    .padding(top = 16.dp)
                                    .padding(start = 15.dp)
                                    .padding(end = 30.dp)

                        )
                        Switch(
                            modifier = Modifier
                                .semantics { contentDescription = "Demo" }
                                .padding(top = 4.dp),
                            checked = enabled,
                            onCheckedChange = { isChecked ->
                                viewModel.viewModelScope.launch {
                                    viewModel.setEnabled(isChecked)
                                }
                            }
                        )

                    }

                }

            }



            /**LAYOUTS */

            Column(modifier = Modifier
                .fillMaxWidth()
                .clickable { navController.navigate(Screen.Layout.route) }  // call it from a background threat or hoist it
            ) {
                // Add some other fields in the drawer
                Row(
                    modifier = Modifier
                        .padding(start = 20.dp)
                        .padding(bottom = 8.dp)
                ) {

                    Image(
                        painter = painterResource(id = R.drawable.display_settings),
                        contentDescription = null,
                        modifier = Modifier
                            .padding(top = 16.dp)
                            .padding(horizontal = 6.dp),

                    )
                    Text(
                        text = "Layout Format",
                        fontSize = if(
                            selectedFontTheme == FontFamily(Font(R.font.pizzat)) ||
                            selectedFontTheme == FontFamily(Font(R.font.first_writing)) ||
                            selectedFontTheme == FontFamily(Font(R.font.slimshoot)) ||
                            selectedFontTheme == FontFamily(Font(R.font.twirly)) ||
                            selectedFontTheme == FontFamily(Font(R.font.gnyrwn977))
                        ) 26.sp else  16.sp,

                        modifier = Modifier
                                .padding(top = 16.dp)
                                .padding(start = 15.dp),
                        color = Color.White,
                        fontFamily = selectedFontTheme,
                    )
                }


            }




                /**RATE AND REVIEW */

                Column(modifier = Modifier
                    .fillMaxWidth()
                    .clickable { navController.navigate(Screen.RateAndReview.route) }  // call it from a background threat or hoist it
                ) {
                    Row(
                        modifier = Modifier
                            .padding(start = 20.dp)
                            .padding(bottom = 8.dp)
                    ) {

                        Image(
                            painter = painterResource(
                                id = R.drawable.rate_review_icon
                            ),
                            contentDescription = "Rate and review Image",
                            modifier = Modifier
                                .padding(top = 16.dp)
                                .padding(horizontal = 6.dp)


                        )
                        Text(
                            text = "Rate and review",
                            fontSize = if(
                                selectedFontTheme == FontFamily(Font(R.font.pizzat)) ||
                                selectedFontTheme == FontFamily(Font(R.font.first_writing)) ||
                                selectedFontTheme == FontFamily(Font(R.font.slimshoot)) ||
                                selectedFontTheme == FontFamily(Font(R.font.twirly)) ||
                                selectedFontTheme == FontFamily(Font(R.font.gnyrwn977))
                            ) 26.sp else  16.sp,
                            color = Color.White,
                            fontFamily = selectedFontTheme,
                            modifier =
                                Modifier
                                    .padding(top = 16.dp)
                                    .padding(start = 15.dp),

                        )
                    }
                }

                /** RECOMMEND TO A FRIEND */

                Column(modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        viewModel.viewModelScope.launch(Dispatchers.Main) {
                            val chooserIntent = Intent.createChooser(shareIntent, "Share App")
                            context.startActivity(chooserIntent) // call it from a background threat or hoist it
                        }
                    }
                ) {
                    Row(
                        modifier = Modifier
                            .padding(start = 20.dp)
                            .padding(bottom = 8.dp)
                    ) {

                        Image(
                            painter = painterResource(id = R.drawable.share_icon),
                            contentDescription = "share Image",
                            modifier = Modifier
                                .padding(top = 16.dp)
                                .padding(horizontal = 6.dp)


                        )

                        Text(
                            text = "Recommend to a Friend",
                            fontSize = if(
                                selectedFontTheme == FontFamily(Font(R.font.pizzat)) ||
                                selectedFontTheme == FontFamily(Font(R.font.first_writing)) ||
                                selectedFontTheme == FontFamily(Font(R.font.slimshoot)) ||
                                selectedFontTheme == FontFamily(Font(R.font.twirly)) ||
                                selectedFontTheme == FontFamily(Font(R.font.gnyrwn977))
                            ) 26.sp else  16.sp,

                            color = Color.White,
                            fontFamily = selectedFontTheme,
                            modifier =
                                Modifier
                                    .padding(top = 16.dp)
                                    .padding(start = 15.dp),

                        )
                    }

                }

                /** About */

                Column(modifier = Modifier
                    .fillMaxWidth()
                    .clickable { navController.navigate(Screen.About.route) }  // call it from a background threat or hoist it
                ) {
                    Row(
                        modifier = Modifier
                            .padding(start = 20.dp)
                            .padding(bottom = 8.dp)
                    ) {

                        Image(
                            painter = painterResource(id = R.drawable.about_icon),
                            contentDescription = "About Image",
                            modifier = Modifier
                                .padding(top = 16.dp)
                                .padding(horizontal = 6.dp)


                        )

                        Text(
                            text = "About",
                            fontSize = if(
                                selectedFontTheme == FontFamily(Font(R.font.pizzat)) ||
                                selectedFontTheme == FontFamily(Font(R.font.first_writing)) ||
                                selectedFontTheme == FontFamily(Font(R.font.slimshoot)) ||
                                selectedFontTheme == FontFamily(Font(R.font.twirly)) ||
                                selectedFontTheme == FontFamily(Font(R.font.gnyrwn977))
                            ) 26.sp else  16.sp,

                            color = Color.White,
                            fontFamily = selectedFontTheme,
                            modifier =
                                Modifier
                                    .padding(top = 16.dp)
                                    .padding(start = 15.dp),

                        )
                    }

                }

        }

    }
}