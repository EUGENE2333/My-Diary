package com.example.mydiary.presentation.compose.mainComposables.subscription

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel


@SuppressLint("RememberReturnType")
@Composable
 fun SubscriptionScreen(
    viewModel: SubscriptionViewModel = hiltViewModel(),
    onNavigate:() -> Unit
) {

}