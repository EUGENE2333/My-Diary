package com.example.mydiary.presentation.compose.mainComposables.subscription.di

import com.example.mydiary.presentation.compose.mainComposables.subscription.revenuecat.RevenueCatAuthenticator
import com.example.mydiary.presentation.compose.mainComposables.subscription.revenuecat.RevenueCatController
import com.example.mydiary.presentation.compose.mainComposables.subscription.revenuecat.RevenueCatInitializer
import com.example.mydiary.presentation.compose.mainComposables.subscription.revenuecat.RevenueCatPurchases
import com.revenuecat.purchases.Purchases
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RevenueCatModule {

    @Binds
    @Singleton
    abstract fun provideRevenueCatInitializer(controller: RevenueCatController): RevenueCatInitializer

    @Binds
    @Singleton
    abstract fun provideRevenueCatAuthenticator(controller: RevenueCatController): RevenueCatAuthenticator

    @Binds
    @Singleton
    abstract fun provideRevenueCatPurchase(controller: RevenueCatController): RevenueCatPurchases

}

@Module
@InstallIn(SingletonComponent::class)
object RevenueCatProvidesModule {

    @Provides
    @Singleton
    fun providePurchases(): Purchases = Purchases.sharedInstance
}