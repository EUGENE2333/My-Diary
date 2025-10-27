package com.example.mydiary

import android.app.Application
import com.example.mydiary.presentation.compose.mainComposables.subscription.revenuecat.RevenueCatController
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class MyDiaryApplication: Application() {

    @Inject
    lateinit var revenueCatController: RevenueCatController


    override fun onCreate() {
        super.onCreate()
        // In your Application class onCreate():
        revenueCatController.initialize(this)
    }
}


