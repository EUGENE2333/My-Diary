package com.example.mydiary.presentation.compose.drawerComposables.layout

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.mydiary.R
import com.example.mydiary.presentation.DiaryViewModel
import kotlinx.coroutines.launch

@Composable
 fun Layout(
    navController: NavController,
    viewModel: DiaryViewModel,
    onNavigateToNoteHomeScreen: () -> Unit,
    onNavigateToDiaryHomeScreen: () -> Unit
) {

    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()
    val selectedFontTheme = viewModel.passwordManager.getFontTheme()
    val selectedColorTheme = viewModel.passwordManager.getColorTheme()
    val isNoteFormat by viewModel.isNoteFormat.collectAsState(initial = false)
    // Note that Modifier.selectableGroup() is essential to ensure correct accessibility behavior.
    // We also set a content description for this sample, but note that a RadioButton would usually
    // be part of a higher level component, such as a raw with text, and that component would need
    // to provide an appropriate content description. See RadioGroupSample.

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Write Note",
                        color = Color.White,
                        fontFamily = selectedFontTheme,
                        fontSize = if (selectedFontTheme == FontFamily(Font(R.font.arabia))) 47.sp else 17.sp,
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
        backgroundColor = selectedColorTheme
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally

        ) {

            Text(
                text = "Select Layout Format",
                color = Color.White
            )

            Row {
                Column {
                    Card(
                        modifier = Modifier
                            .padding(10.dp)
                            .clickable {
                                scope.launch {
                                    viewModel.isNoteFormat(isNoteFormat = false)
                                    onNavigateToDiaryHomeScreen.invoke()
                                }
                            },
                        backgroundColor  = selectedColorTheme,
                        elevation = 5.dp
                        
                    ) {
                        Column(
                            modifier = Modifier.padding(5.dp),
                           horizontalAlignment = Alignment.CenterHorizontally

                                ) {


                            Image(
                                painter = painterResource(id = R.drawable.diary_format),
                                contentDescription = "Diary Format",
                                modifier = Modifier
                                    .size(130.dp)
                                    .clip(CircleShape)
                                    .align(Alignment.CenterHorizontally)
                                    .padding(top = 10.dp)

                            )
                            Spacer(modifier = Modifier.height(5.dp))

                            Text(
                                text = "Diary Format",
                                color = Color.White
                            )
                        }

                    }
                }


                Column {
                    Card(
                        modifier = Modifier
                            .padding(10.dp)
                            .clickable {
                                scope.launch {
                                    viewModel.isNoteFormat(isNoteFormat = true)
                                    onNavigateToNoteHomeScreen.invoke()
                                }
                            },
                            backgroundColor  = selectedColorTheme,

                        elevation = 5.dp
                    ) {


                        Column(
                            modifier = Modifier.padding(5.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {

                            Image(
                                painter = painterResource(id = R.drawable.note_format),
                                contentDescription = "Note Format",
                                modifier = Modifier
                                    .size(130.dp)
                                    .clip(CircleShape)
                                    .align(Alignment.CenterHorizontally)
                                    .padding(top = 10.dp)


                            )
                            Spacer(modifier = Modifier.height(3.dp))

                                Text(
                                    text = "Note Format",
                                    color = Color.White
                                )
                        }
                    }


                }
            }

            Row(
                Modifier.selectableGroup()
            ) {
                RadioButton(
                    selected =  !isNoteFormat,
                    onClick = {
                        scope.launch {
                            viewModel.isNoteFormat(isNoteFormat = false)
                            onNavigateToDiaryHomeScreen.invoke()
                        }
                              },
                    modifier = Modifier
                        .semantics { contentDescription = "Localized Description" }
                        .padding(horizontal = 55.dp)
                )
                RadioButton(
                    selected = isNoteFormat,
                    onClick = {
                        scope.launch {
                            viewModel.isNoteFormat(isNoteFormat = true)
                            onNavigateToNoteHomeScreen.invoke()
                        }
                              },
                    modifier = Modifier
                        .semantics { contentDescription = "Localized Description" }
                        .padding(horizontal = 55.dp)
                )
            }

        }
    }
}

