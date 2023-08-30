

package com.example.mydiary.ui.theme

import android.app.Activity
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Typography
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import com.example.mydiary.presentation.DiaryViewModel


@Composable
fun MyDiaryTheme(
    viewModel: DiaryViewModel,
    content: @Composable () -> Unit
) {

    val selectedColorTheme  by rememberUpdatedState(viewModel.passwordManager.getColorTheme())

    val selectedFontTheme by rememberUpdatedState(viewModel.passwordManager.getFontTheme())

    val Typography = Typography(selectedFontTheme)



    val colorPalette = when (selectedColorTheme) {
        Color.Red -> RedColorPalette
        Color.Green -> GreenColorPalette
        Color.Blue -> BlueColorPalette
        Color(0xFF4B4A05) -> YellowColorPalette
        Color.Cyan -> CyanColorPalette
        Color(0xFF6200EE) -> PurpleColorPalette
        Color(0xFF2F3872) -> lightBlueColorPalette
        Color(0xFF69A325) -> lightGreenColorPalette
        Color(0xFFAB4CBB)-> thisColorPalette
        Color(0xFFD67972) -> this1ColorPalette
        Color(0xFF114D38) -> this2ColorPalette
        Color(0xFF0B353D)->this3ColorPalette

        else -> DefaultColorPalette
    }

    // Get the context
    val context = LocalContext.current
    // Check if the app is in inspection mode (preview)
    val isInspectionMode = LocalInspectionMode.current

    // Update the status bar color
    SideEffect {
        if (!isInspectionMode) {
            val statusBarColor = Color(0xFF2C2428).toArgb()            //selectedColorTheme.toArgb()
            val window = (context as? Activity)?.window
            window?.statusBarColor = statusBarColor
            window?.navigationBarColor = statusBarColor

        }
    }

    MaterialTheme(
        colors = colorPalette,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}

private val DefaultColorPalette = lightColors(
    primary = Color(0xFF6200EE),
    primaryVariant = Color(0xFF3700B3),
    secondary = Color(0xFF03DAC6),
    background = Color.White,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black
)

private val RedColorPalette = lightColors(
    primary = Color.Red,
    primaryVariant = Color(0xFF630404),
    secondary = Color.Red,
    secondaryVariant = Color(0xFF630404),
    background = Color.White,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = Color.Black,
    onSurface = Color.Black
)

private val GreenColorPalette = lightColors(
    primary = Color.Green,
    // Define other color values for the green color theme
)

private val BlueColorPalette = lightColors(
    primary = Color.Blue,
    // Define other color values for the blue color theme
)

private val YellowColorPalette = lightColors(
    primary =Color(0xFF4B4A05),
    // Define other color values for the yellow color theme
)


private val CyanColorPalette = lightColors(
    primary = Color.Cyan,
    // Define other color values for the cyan color theme
)

private val PurpleColorPalette = lightColors(
    primary = Color(0xFF6200EE),
// Define other color values for the cyan color theme
)
private val lightBlueColorPalette = lightColors(
    primary =  Color(0xFF2F3872),
// Define other color values for the cyan color theme
)
private val lightGreenColorPalette = lightColors(
    primary = Color(0xFF69A325),
// Define other color values for the cyan color theme
)
private val thisColorPalette = lightColors(
    primary =  Color(0xFFAB4CBB),
    // Define other color values for the cyan color theme
)
private val this1ColorPalette = lightColors(
    primary =  Color(0xFFD67972),
    // Define other color values for the cyan color theme
)
private val this2ColorPalette = lightColors(
    primary =    Color(0xFF114D38),
    // Define other color values for the cyan color theme
)
private val this3ColorPalette = lightColors(
    primary =   Color(0xFF0B353D),
    // Define other color values for the cyan color theme
)







/**
 * @Composable
fun MyDiaryTheme(
darkTheme: Boolean = isSystemInDarkTheme(),
content: @Composable () -> Unit
) {
val isDarkThemeEnabled = remember { mutableStateOf(darkTheme) }

val toggleTheme: () -> Unit = {
isDarkThemeEnabled.value = !isDarkThemeEnabled.value
val mode = if (isDarkThemeEnabled.value) {
AppCompatDelegate.MODE_NIGHT_YES
} else {
AppCompatDelegate.MODE_NIGHT_NO
}
AppCompatDelegate.setDefaultNightMode(mode)
}

MaterialTheme(
colors = if (isDarkThemeEnabled.value) DarkColorPalette else LightColorPalette,
typography = androidx.compose.material.Typography(),
content = {
content()
Switch(
checked = isDarkThemeEnabled.value,
onCheckedChange = { toggleTheme() },
modifier = Modifier.semantics { contentDescription = "Dark mode switch" }
)
}
)
}
 * **/













