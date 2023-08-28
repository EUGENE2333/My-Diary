

package com.example.mydiary.ui.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
class Type {


        val Typography = Typography(
            body1 = regular,
            h1 = bold,
            h2 = bold,
            h3 = bold,
            h4 = bold,
            h5 = bold,
            h6 = bold,
            subtitle1 = regular,
            subtitle2 = regular,
            overline = regular,
            button = bold,
            caption = regular
        )


    companion object {

        val regular = TextStyle(
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp
        )


        val bold = TextStyle(
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        )

        val italic = TextStyle(
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp,
            fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
        )


    }

}

