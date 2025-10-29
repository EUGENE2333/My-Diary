package com.example.mydiary.presentation.compose.drawerComposables.colorAndStyle

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.mydiary.presentation.DiaryViewModel
import com.example.mydiary.presentation.compose.mainComposables.headerFontSizeBasedOnFontTheme
import com.example.mydiary.ui.theme.titleExtraLarge
import com.example.mydiary.ui.theme.titleLarge
import io.mhssn.colorpicker.ColorPicker
import io.mhssn.colorpicker.ColorPickerType
import io.mhssn.colorpicker.ext.toHex
import io.mhssn.colorpicker.ext.transparentBackground

//import com.kavi.droid.color.picker.ui.pickers.HSLAColorPicker

@OptIn(ExperimentalComposeUiApi::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ColorAndStyle(
    viewModel: DiaryViewModel = hiltViewModel(),
    navController: NavController
) {
    val selectedColorTheme = viewModel.selectedColorTheme.collectAsState()
    val selectedColor = viewModel.passwordManager.getColorTheme()
    val selectedFontTheme = viewModel.selectedFont.collectAsState()
    val selectedFont by rememberUpdatedState(viewModel.passwordManager.getFontTheme())
    val scaffoldState = rememberScaffoldState()
    var isShowColorPicker by remember {
        mutableStateOf(false)
    }

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(title = {
                Text(
                    text = "Color and Font",
                    color = Color.White,
                    fontFamily = selectedFont,
                    fontSize =  headerFontSizeBasedOnFontTheme(selectedFont)
                )
            },
                backgroundColor = Color(0xFF2C2428),
                navigationIcon = {
                    IconButton(onClick = {  navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "back",tint = Color.White)
                    }
                }
            )
        },
        backgroundColor =selectedColor,
        content = {

            Column(
                modifier = Modifier.fillMaxSize()
                .padding(horizontal = 10.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = "Select color:",
                    style = MaterialTheme.typography.h4,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                     fontFamily = selectedFont
                )
                ColorThemeSelectionGrid(
                    colorThemes = viewModel.colorThemes,
                    selectedColor = selectedColorTheme
                ) { color ->
                    viewModel.setSelectedColorTheme(color)
                }
                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = {
                        isShowColorPicker  = true
                    },
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = selectedColor.copy(alpha = 0.1f),
                        contentColor = Color.White
                    ),
                    modifier = Modifier.clip(RoundedCornerShape(12.dp)),
                ) {
                    Text(
                        text = "More Colors",
                        style = titleLarge
                    )
                }

                Divider(
                    modifier = Modifier
                        .padding(10.dp)
                        .fillMaxWidth()
                        .width(10.dp),
                    color = Color.White
                )
              //  Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Select font:",
                    style = MaterialTheme.typography.h4,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    fontFamily = selectedFont
                )
                Box(
                    modifier = Modifier.padding(start= 10.dp,end = 10.dp)
                ) {


                    FontSelectionColumn(
                        fonts = viewModel.fonts,
                        selectedFont = selectedFontTheme
                    ) { font ->
                        viewModel.setSelectedFontTheme(font)

                    }
                }

                Spacer(modifier = Modifier.width(10.dp))
            }

        })

    if(isShowColorPicker){
        ColorPickerDialog(
            show = true,
            onDismissRequest = {
                isShowColorPicker = false
            },
            onPickedColor = { color ->
                viewModel.setSelectedColorTheme(color)
                navController.popBackStack()
            }

        )
    }
}


@ExperimentalComposeUiApi
@Composable
fun ColorPickerDialog(
    show: Boolean,
    onDismissRequest: () -> Unit,
    properties: DialogProperties = DialogProperties(),
    type: ColorPickerType = ColorPickerType.Circle(),
    onPickedColor: (Color) -> Unit
) {
    var showDialog by remember(show) {
        mutableStateOf(show)
    }
    var color by remember {
        mutableStateOf(Color.White)
    }
    if (showDialog) {
        Dialog(onDismissRequest = {
            onDismissRequest()
            showDialog = false
        }, properties = properties) {
            Box(
                modifier = Modifier
                    .width(IntrinsicSize.Max)
                    .clip(RoundedCornerShape(32.dp))
                    .background(Color.Black)
            ) {
                Box(modifier = Modifier.padding(32.dp)) {
                    Column {
                        ColorPicker(type = type, onPickedColor = {
                            color = it
                        })
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(50.dp, 30.dp)
                                    .clip(RoundedCornerShape(50))
                                    .border(0.3.dp, Color.LightGray, RoundedCornerShape(50))
                                    .transparentBackground(verticalBoxesAmount = 4)
                                    .background(color)
                            )
                            Text(
                                text = buildAnnotatedString {
                                    withStyle(SpanStyle(color = Color.Gray)) {
                                        append("#")
                                    }
                                    withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                                        append(color.toHex())
                                    }
                                },
                                fontSize = 14.sp,
                                fontFamily = FontFamily.Monospace,
                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        OutlinedButton(
                            modifier = Modifier.fillMaxWidth(),
                            onClick = {
                                onPickedColor(color)
                                showDialog = false
                            },
                            shape = RoundedCornerShape(50)
                        ) {
                            Text(text = "Select")
                        }
                    }
                }
            }
        }
    }
}