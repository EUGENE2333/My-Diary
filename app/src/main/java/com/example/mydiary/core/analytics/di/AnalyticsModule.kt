package com.example.mydiary.core.analytics.di

import com.example.mydiary.core.analytics.AnalyticsProvider
import com.example.mydiary.core.analytics.AnalyticsTracker
import com.example.mydiary.core.analytics.AnalyticsTrackerImpl
import com.example.mydiary.core.analytics.FirebaseAnalyticsProvider
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ParametersBuilder
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

typealias AnalyticsProviders = List<@JvmSuppressWildcards AnalyticsProvider>

@Module
@InstallIn(SingletonComponent::class)
class AnalyticsModule {

    @Provides
    fun provideFirebaseAnalytics(): FirebaseAnalytics = Firebase.analytics

    @Provides
    fun provideFirebaseAnalyticsProvider(
        firebaseAnalytics: FirebaseAnalytics
    ): FirebaseAnalyticsProvider = FirebaseAnalyticsProvider(firebaseAnalytics, ParametersBuilder())

    @Provides
    fun provideAnalyticsProviders(
        firebaseAnalyticsProvider: FirebaseAnalyticsProvider
    ): AnalyticsProviders = listOf(firebaseAnalyticsProvider)

    @Provides
    fun provideAnalyticsTracker(
        analyticsProviders: AnalyticsProviders
    ): AnalyticsTracker = AnalyticsTrackerImpl(analyticsProviders)
}

@Module
@InstallIn(SingletonComponent::class)
interface AnalyticsModuleBinds {

    @Binds
    fun bindsAnalyticsProviders(
        firebaseAnalyticsProvider: FirebaseAnalyticsProvider
    ): AnalyticsProvider
}