package com.example.mydiary.presentation.compose.mainComposables.drawerContent

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Configuration
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.mydiary.R
import com.example.mydiary.presentation.DiaryViewModel
import com.example.mydiary.presentation.compose.mainComposables.fontSizeBasedOnFontTheme
import com.example.mydiary.presentation.compose.mainComposables.headerFontSizeBasedOnFontTheme
import com.example.mydiary.presentation.compose.navigation.Screen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun DrawerContent(
    navController: NavController,
    viewModel: DiaryViewModel = hiltViewModel(),

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

    val scaffoldState = rememberScaffoldState()

    val value by rememberInfiniteTransition().animateFloat(
        initialValue = 25f,
        targetValue = -25f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 600,
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Reverse
        )
    )

    Scaffold(
        scaffoldState = scaffoldState
    ) {

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
                        fontSize = headerFontSizeBasedOnFontTheme(selectedFontTheme),
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
                        if (viewModel.passwordManager.getPassword() == "null") {
                            navController.navigate(Screen.LockScreen.route)
                        } else {
                            navController.navigate(Screen.ChangePassword.route)
                        }
                    }  // call it from a background threat or hoist it
                ) {
                    // Add some other fields in the drawer
                    Row(
                        modifier = Modifier.paddingForSection()
                    ) {

                        Image(
                            painter = painterResource(id = R.drawable.lock_icon),
                            contentDescription = "Lock Image",
                            modifier = Modifier.paddingForImage()

                        )
                        Text(
                            text = if (viewModel.passwordManager.getPassword() == "null") " Add Lock" else "Change Lock",
                            fontSize = fontSizeBasedOnFontTheme(selectedFontTheme),
                            modifier = Modifier.paddingForText(),
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
                        modifier = Modifier.paddingForSection()
                    ) {

                        Image(
                            painter = painterResource(id = R.drawable.color_icon),
                            contentDescription = "Colors and style Image",
                            modifier = Modifier.paddingForImage()


                        )

                        Text(
                            text = "Colors and style",
                            fontSize = fontSizeBasedOnFontTheme(selectedFontTheme),
                            color = Color.White,
                            fontFamily = selectedFontTheme,
                            modifier = Modifier.paddingForText(),

                            )
                    }

                }

                /** REMINDER */

                Column(modifier = Modifier
                    .fillMaxWidth()
                    .clickable { navController.navigate(Screen.Reminder.route) }  // call it from a background threat or hoist it
                ) {
                    Row(
                        modifier = Modifier.paddingForSection()
                    ) {

                        Image(
                            painter = painterResource(id = R.drawable.add_alert),
                            contentDescription = "Reminder Image",
                            modifier = Modifier.paddingForImage()
                                .graphicsLayer(
                                    transformOrigin = TransformOrigin(
                                        pivotFractionX = 0.5f,
                                        pivotFractionY = 0.0f,
                                    ),
                                    rotationZ = value
                                )

                        )

                        Text(
                            text = "Reminder",
                            fontSize = fontSizeBasedOnFontTheme(selectedFontTheme),
                            color = Color.White,
                            fontFamily = selectedFontTheme,
                            modifier = Modifier.paddingForText(),

                            )
                    }

                }


                /** TEXT TO SPEECH */

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(
                            start = 20.dp,
                            bottom = 3.dp
                        )
                    ) {

                        Image(
                            painter = painterResource(id = R.drawable.voice_over),
                            contentDescription = "Text to Speech",
                            modifier = Modifier.paddingForImage()
                        )
                        Row {
                            Text(
                                text = "Text to Speech",
                                fontSize = fontSizeBasedOnFontTheme(selectedFontTheme),
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
                    Row(
                        modifier = Modifier.paddingForSection()
                    ) {

                        Image(
                            painter = painterResource(id = R.drawable.display_settings),
                            contentDescription = null,
                            modifier = Modifier.paddingForImage()

                        )
                        Text(
                            text = "Layout Format",
                            fontSize = fontSizeBasedOnFontTheme(selectedFontTheme),

                            modifier = Modifier.paddingForText(),
                            color = Color.White,
                            fontFamily = selectedFontTheme,
                        )
                    }
                }

                /**EXPORT*/

                Column(modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        viewModel.viewModelScope.launch(Dispatchers.Main) {
                            navController.navigate(Screen.Export.route)
                        }
                    }
                ) {
                    Row(
                        modifier = Modifier.paddingForSection()
                    ) {

                        Image(
                            painter = painterResource(id = R.drawable.file_open),
                            contentDescription = "Export notes",
                            modifier = Modifier.paddingForImage()

                        )

                        Text(
                            text = "Export",
                            fontSize = fontSizeBasedOnFontTheme(selectedFontTheme),
                            color = Color.White,
                            fontFamily = selectedFontTheme,
                            modifier = Modifier.paddingForText(),

                            )
                    }
                }


                /**RATE AND REVIEW */

                Column(modifier = Modifier
                    .fillMaxWidth()
                    .clickable { navController.navigate(Screen.RateAndReview.route) }  // call it from a background threat or hoist it
                ) {
                    Row(
                        modifier = Modifier.paddingForSection()
                    ) {

                        Image(
                            painter = painterResource(
                                id = R.drawable.rate_review_icon
                            ),
                            contentDescription = "Rate and review Image",
                            modifier = Modifier.paddingForImage()


                        )
                        Text(
                            text = "Rate and review",
                            fontSize = fontSizeBasedOnFontTheme(selectedFontTheme),
                            color = Color.White,
                            fontFamily = selectedFontTheme,
                            modifier = Modifier.paddingForText(),

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
                        modifier = Modifier.paddingForSection()
                    ) {

                        Image(
                            painter = painterResource(id = R.drawable.share_icon),
                            contentDescription = "share Image",
                            modifier = Modifier.paddingForImage()
                        )

                        Text(
                            text = "Recommend to a Friend",
                            fontSize = fontSizeBasedOnFontTheme(selectedFontTheme),
                            color = Color.White,
                            fontFamily = selectedFontTheme,
                            modifier = Modifier.paddingForText()
                        )
                    }


                }

                /** About */

                Column(modifier = Modifier
                    .fillMaxWidth()
                    .clickable { navController.navigate(Screen.About.route) }  // call it from a background threat or hoist it
                ) {
                    Row(
                        modifier = Modifier.paddingForSection()
                    ) {

                        Image(
                            painter = painterResource(id = R.drawable.about_icon),
                            contentDescription = "About Image",
                            modifier = Modifier.paddingForImage()
                        )

                        Text(
                            text = "About",
                            fontSize = fontSizeBasedOnFontTheme(selectedFontTheme),
                            color = Color.White,
                            fontFamily = selectedFontTheme,
                            modifier = Modifier.paddingForText()
                        )
                    }

                }
            }
        }
    }
}