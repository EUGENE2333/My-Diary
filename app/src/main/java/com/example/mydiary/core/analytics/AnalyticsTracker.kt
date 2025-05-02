package com.example.mydiary.core.analytics

import com.example.mydiary.core.analytics.di.AnalyticsProviders
import javax.inject.Inject

/**
 * Facade class used to track events and pages. This will delegate the tracking to all the analytics providers.
 */
interface AnalyticsTracker {
    fun track(analyticEvent: AnalyticEvent, properties: Map<Property, Any>? = null)
    fun trackPage(page: Page, properties: Map<Property, Any>? = null)
    fun setUserDetails(userId: String)
    fun clearUserDetails()
}

class AnalyticsTrackerImpl @Inject constructor(
    private val providers: AnalyticsProviders
) : AnalyticsTracker {
    override fun track(analyticEvent: AnalyticEvent, properties: Map<Property, Any>?) {
        providers.forEach { it.track(analyticEvent, properties) }
    }

    override fun trackPage(page: Page, properties: Map<Property, Any>?) {
        providers.forEach { it.trackPage(page, properties) }
    }

    override fun setUserDetails(userId: String) {
        providers.forEach { it.setUserDetails(userId) }
    }

    override fun clearUserDetails() {
        providers.forEach { it.clearUserDetails() }
    }
}