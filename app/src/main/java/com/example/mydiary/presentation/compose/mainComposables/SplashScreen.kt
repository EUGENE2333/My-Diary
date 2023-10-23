package com.example.mydiary.presentation.compose.mainComposables

import android.annotation.SuppressLint
import android.view.animation.OvershootInterpolator
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mydiary.R
import com.example.mydiary.presentation.DiaryViewModel
import kotlinx.coroutines.delay


@SuppressLint("RememberReturnType")
@Composable
 fun SplashScreen(
    viewModel: DiaryViewModel,
    onNavigate:() -> Unit
) {

    val selectedFontTheme = viewModel.passwordManager.getFontTheme()

    val scale = remember {
        Animatable(0f)
    }

    LaunchedEffect(key1 = true) {
        scale.animateTo(
            targetValue = 0.9f,
            animationSpec = tween(
                durationMillis = 500,
                easing = {
                    OvershootInterpolator(2f).getInterpolation(it)
                }
            )
        )
        delay(3000L)
        onNavigate.invoke()
    }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xFFCA6EE2), shape = RectangleShape)
    ) {

        Text(
            text = "My Diary",
            fontSize = if(
                selectedFontTheme == FontFamily(Font(R.font.pizzat)) ||
                selectedFontTheme == FontFamily(Font(R.font.first_writing)) ||
                selectedFontTheme == FontFamily(Font(R.font.slimshoot)) ||
                selectedFontTheme == FontFamily(Font(R.font.gnyrwn977))
            ) 58.sp else 40.sp,

            fontFamily = selectedFontTheme,
            color = Color.White,
            modifier = Modifier.padding(top  = 40.dp)

        )

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color(0xFFCA6EE2), shape = RectangleShape)
        ) {


            Image(
                painter = painterResource(id = R.drawable.logo1),
                contentDescription = "Logo",
                modifier = Modifier.scale(scale.value)
                    .padding(top = 5.dp)
            )

        }
    }
}