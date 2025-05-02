package com.example.mydiary.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.mydiary.R


@Composable
fun MyDiaryCircularProgressIndicator(
    modifier: Modifier = Modifier,
    spinnerColor: Color = MaterialTheme.colorScheme.onBackground,
    backgroundColor: Color = Color.Unspecified
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(backgroundColor)
    ) {
        val label = "MyDiaryCircularProgressIndicator"
        val infiniteTransition = rememberInfiniteTransition(label = label)
        val angle by infiniteTransition.animateFloat(
            initialValue = 0F,
            targetValue = 360F,
            animationSpec = infiniteRepeatable(
                animation = tween(
                    durationMillis = 1200,
                    easing = LinearEasing
                )
            ), label = label
        )

        Image(painter = painterResource(id = R.drawable.ic_spinner),
            contentDescription = "Loading...",
            modifier = Modifier
                .align(Alignment.Center)
                .wrapContentSize()
                .rotate(angle),
            colorFilter = ColorFilter.tint(spinnerColor),
            contentScale = ContentScale.Fit
        )
    }
}

@Composable
fun MyDiaryCircularProgressIndicatorDialog(
    spinnerColor: Color = MaterialTheme.colorScheme.background
) {
    Dialog(
        onDismissRequest = { },
        properties = DialogProperties(
            dismissOnBackPress = false,
            dismissOnClickOutside = false
        )
    ) {
        MyDiaryCircularProgressIndicator(spinnerColor = spinnerColor)
    }
}