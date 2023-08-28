package com.example.mydiary.presentation.compose.drawerComposables.colorAndStyle

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun FontSelectionColumn(
      fonts: List<FontFamily>,
      selectedFont: State<FontFamily>,
      onFontSelected:(FontFamily)-> Unit
) {
      Column {
            LazyColumn(
                  modifier = Modifier.fillMaxWidth()
            ){
                  items(fonts.size){index ->
                        val font = fonts[index]
                        FontItem(
                              font = font,
                              isSelected = font == selectedFont.value,
                              onFontSelected = { onFontSelected(font) }
                        )

                  }
            }
      }
}

@Composable
fun FontItem(
      font: FontFamily,
      isSelected: Boolean,
      onFontSelected: () -> Unit
) {
      Row(
            modifier = Modifier.clickable { onFontSelected() }
                  .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
      ) {
            Box(
                  modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp),
                  contentAlignment = Alignment.Center
            ) {
                  Column {
                        Row {
                              Text(
                                    text = "Abubakarrrrrrrrrrrrrrrrr",
                                    style = TextStyle(fontFamily = font),
                                    fontSize = 20.sp,
                                    color = Color.White,
                                    //    textAlign = TextAlign.Center,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal

                              )
                              if (isSelected) {
                                    Icon(
                                          imageVector = Icons.Default.Check,
                                          contentDescription = "Selected",
                                          tint = MaterialTheme.colors.onPrimary,
                                          modifier = Modifier.size(23.dp)
                                    )
                              }

                        }
                        Divider(
                              modifier = Modifier
                                  //  .padding(3.dp)
                                    .padding(bottom = 0.dp)
                                    .fillMaxWidth()
                                    .width(10.dp),
                              color = Color.White
                        )
                  }

            }

      }
}
