package com.example.mydiary.presentation.compose.mainComposables.subscription.paywall

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.mydiary.ui.theme.LocalSpacing
import com.example.mydiary.ui.theme.bodyLarge
import com.example.mydiary.ui.theme.crownColor
import com.example.mydiary.ui.theme.titleMedium

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
fun SubscriptionPriceColumn(
    price: String,
    cadence: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
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
    // Simple border for testing
    val borderModifier = if (highlight) {
        modifier.border(
            width = 2.dp,
            color = crownColor,  // Use a simple color for testing
            shape = MaterialTheme.shapes.large
        )
    } else {
        modifier.border(
            width = 1.dp,
            color = MaterialTheme.colorScheme.onPrimary,
            shape = MaterialTheme.shapes.large
        )
    }

    // Add debug log
    Log.d("SubscriptionItemView", "Rendering: title=$title, price=$price, selected=$selected")

    Column (
        modifier = borderModifier
            .background(
                color =Color.Black, //Color(0xFFA53E97),
                shape = MaterialTheme.shapes.large)
            .padding(14.dp)
            .clickable { onClick() },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(modifier = Modifier.width(LocalSpacing.current.medium))
        SubscriptionTitle(title, selected)
        Spacer(modifier = Modifier.width(LocalSpacing.current.small))
        SubscriptionPriceColumn(price, cadence)

      /*  Text(
            text = cadence,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onPrimary
        )
        Text(
            text = title,
            style = if (selected) MaterialTheme.typography.titleMedium else MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onPrimary,
            modifier = modifier
        )

        Spacer(
            modifier = Modifier
                .height(LocalSpacing.current.medium)
                .weight(1f)
        )

        // Simplified price display
        Text(
            text = price,
            color = MaterialTheme.colorScheme.onPrimary,
            style = MaterialTheme.typography.titleMedium
        ) */
    }
}