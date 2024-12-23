package com.example.mydiary.ui.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.mydiary.R

private val InterFonts = FontFamily(
    Font(R.font.inter_regular, weight = FontWeight.Normal, style = FontStyle.Normal),
    Font(R.font.inter_medium, weight = FontWeight.Medium, style = FontStyle.Normal),
    Font(R.font.inter_bold, weight = FontWeight.Bold, style = FontStyle.Normal),
    Font(R.font.inter_black, weight = FontWeight.Black, style = FontStyle.Normal)
)

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

    private val SendwavyFonts = FontFamily(
        Font(R.font.sendwavy_regular, weight = FontWeight.Normal, style = FontStyle.Normal),
        Font(R.font.sendwavy_regular, weight = FontWeight.Bold, style = FontStyle.Normal),
        Font(R.font.sendwavy_regular, weight = FontWeight.Medium, style = FontStyle.Normal)
    )

    val disclaimerMedium = TextStyle(
        fontFamily = InterFonts,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 18.sp
    )

    val displayLarge = TextStyle(
        fontFamily = SendwavyFonts,
        fontWeight = FontWeight.Bold,
        fontSize = 60.sp,
        lineHeight = 72.sp
    )
    val displayMedium = TextStyle(
        fontFamily = SendwavyFonts,
        fontWeight = FontWeight.Bold,
        fontSize = 52.sp,
        lineHeight = 64.sp
    )
    val displayMediumHeavy = TextStyle(
        fontFamily = InterFonts,
        fontWeight = FontWeight.Black,
        fontSize = 52.sp,
        lineHeight = 64.sp
    )
    val displaySmall = TextStyle(
        fontFamily = SendwavyFonts,
        fontWeight = FontWeight.Bold,
        fontSize = 40.sp,
        lineHeight = 48.sp
    )
    val headlineExtraLarge = TextStyle(
        fontFamily = InterFonts,
        fontWeight = FontWeight.Bold,
        fontSize = 28.sp,
        lineHeight = 34.sp
    )
    val headlineLarge = TextStyle(
        fontFamily = InterFonts,
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp,
        lineHeight = 30.sp
    )
    val headlineMedium = TextStyle(
        fontFamily = InterFonts,
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp,
        lineHeight = 28.sp
    )
    val headlineSmall = TextStyle(
        fontFamily = InterFonts,
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp,
        lineHeight = 24.sp
    )
    val titleExtraLarge = TextStyle(
        fontFamily = InterFonts,
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp,
        lineHeight = 30.sp
    )
    val titleLarge = TextStyle(
        fontFamily = InterFonts,
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp,
        lineHeight = 24.sp
    )
    val titleMedium = TextStyle(
        fontFamily = InterFonts,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp,
        lineHeight = 22.sp
    )
    val titleSmall = TextStyle(
        fontFamily = InterFonts,
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp,
        lineHeight = 18.sp
    )
    val bodyExtraLarge = TextStyle(
        fontFamily = InterFonts,
        fontWeight = FontWeight.Normal,
        fontSize = 18.sp,
        lineHeight = 24.sp
    )
    val bodyLarge = TextStyle(
        fontFamily = InterFonts,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 22.sp
    )
    val bodyMedium = TextStyle(
        fontFamily = InterFonts,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 18.sp
    )
    val bodySmall = TextStyle(
        fontFamily = InterFonts,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        lineHeight = 16.sp
    )
    val labelLarge = TextStyle(
        fontFamily = InterFonts,
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp,
        lineHeight = 18.sp
    )
    val labelMedium = TextStyle(
        fontFamily = InterFonts,
        fontWeight = FontWeight.Bold,
        fontSize = 12.sp,
        lineHeight = 14.sp
    )

    val labelSmall = TextStyle(
        fontFamily = InterFonts,
        fontWeight = FontWeight.Bold,
        fontSize = 10.sp,
        lineHeight = 12.sp
    )
    val Button = TextStyle(
        fontFamily = InterFonts,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp,
        lineHeight = 22.sp
    )
    val ButtonSmall = Button.copy(fontSize = 13.sp)
    val TextButtonLarge = Button.copy(fontFamily = InterFonts)
    val TextButtonMedium = TextButtonLarge.copy(
        fontSize = 14.sp,
        lineHeight = 18.sp
    )

