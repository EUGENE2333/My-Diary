package com.example.mydiary.presentation.compose.mainComposables.subscription.paywall

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.example.mydiary.R
import com.example.mydiary.ui.components.animatedBorder
import com.example.mydiary.ui.theme.LocalSpacing
import com.example.mydiary.ui.theme.bodyLarge
import com.example.mydiary.ui.theme.titleMedium
import com.google.common.collect.ImmutableList

@Composable
fun SubscriptionTitle(
    title: String,
    selected: Boolean,
    modifier: Modifier = Modifier
) {
    Text(
        modifier = modifier,
        text = title.uppercase(),
        style = if (selected) titleMedium else bodyLarge
    )
}

@Composable
fun SubscriptionPriceRow(
    price: String,
    cadence: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ){
        Text(
            text = price,
            color = MaterialTheme.colorScheme.onSecondaryContainer,
            style = titleMedium
        )
        Spacer(modifier = Modifier.width(LocalSpacing.current.small))
    }
}

@Composable
fun SubscriptionItemView(
    title: String,
    price: String,
    cadence: String,
    selected: Boolean,
    highlight: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val appliedBorder = if (highlight) {
        modifier.fillMaxWidth().animatedBorder(
            borderColors = listOf(Color.Red, Color.Green, Color.Blue, Color.Yellow) as ImmutableList<Color>,
            backgroundColor = Color.White,
            shape = MaterialTheme.shapes.large,
            borderWidth = 1.dp
        )
    } else {
        modifier.fillMaxWidth().border(
            width = 1.dp,
            color = MaterialTheme.colorScheme.secondary,
            shape = MaterialTheme.shapes.large
        )
    }
    Row(
        modifier = appliedBorder
            .background(
                color = MaterialTheme.colorScheme.primaryContainer,
                shape = MaterialTheme.shapes.large
            )
            .padding(
                horizontal = LocalSpacing.current.medium,
                vertical = LocalSpacing.current.large
            )
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(modifier = Modifier.width(LocalSpacing.current.extraSmall))
        if (selected) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_checkbox_checked),
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = Modifier.size(LocalSpacing.current.medium + LocalSpacing.current.extraSmall)
            )
        } else {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_checkbox_unchecked),
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = Modifier.size(LocalSpacing.current.medium + LocalSpacing.current.extraSmall)
            )
        }
        Spacer(modifier = Modifier.width(LocalSpacing.current.medium))

        SubscriptionTitle(title, selected)
        Spacer(
            modifier =
            Modifier
                .width(LocalSpacing.current.medium)
                .weight(1f)
        )
        SubscriptionPriceRow(price, cadence)
    }
}