package com.example.mydiary.presentation.compose.mainComposables

import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mydiary.data.model.Notes
import com.example.mydiary.data.utils.Utils
import com.example.mydiary.presentation.DiaryViewModel
import com.example.mydiary.presentation.compose.animations.bouncingClickable
import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.*


@Composable
fun DiaryEntryItem(
    notes: Notes,
    viewModel:DiaryViewModel,
    onLongClick: () -> Unit,
    onClick: () -> Unit,

) {
    val selectedColorTheme = viewModel.passwordManager.getColorTheme()
    val selectedFont = viewModel.passwordManager.getFontTheme()

    Card(
            elevation = 15.dp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(1.dp)
                .bouncingClickable (onLongClick = {onLongClick.invoke()}){ onClick.invoke() },

        backgroundColor = Utils.colors[0]
        ) {

        Row {
            Card(modifier = Modifier.padding(4.dp)) {
                Text(
                    text = formattDate(notes.timestamp),
                    style = MaterialTheme.typography.body1,
                    fontFamily = selectedFont,
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(end = 3.dp)
                        .padding(2.dp),

                )
            }

                        Column(modifier = Modifier.padding(start = 4.dp)){

                            Text(
                                text = notes.title,
                                style = MaterialTheme.typography.h6,
                                fontWeight = FontWeight.Normal,
                                color = selectedColorTheme,
                                fontFamily = selectedFont,
                                fontSize = 22.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Clip,
                            )

                            Text(
                                text = notes.description,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis,
                                style = MaterialTheme.typography.body1,
                                fontSize = fontSizeBasedOnFontTheme(selectedFont),
                                color = selectedColorTheme,
                                fontFamily = selectedFont

                            )
                        }
                    }
    }
}

private fun formattDate(timestamp: Timestamp): String{
    val simpleDateFormat = SimpleDateFormat(" MMM\n dd\nyyyy", Locale.getDefault())
    return simpleDateFormat.format(timestamp.toDate())
}






