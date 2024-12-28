package com.example.mydiary.core.analytics
/*
/**
 * Common interface for all analytics providers.
 * Analytics providers are responsible for tracking events and pages using the respective analytics SDKs.
 * for example, Firebase, Amplitude, etc.
 */
interface AnalyticsProvider {

    /**
     * Track an event with optional properties.
     */
    fun track(analyticEvent: AnalyticEvent, properties: Map<Property, Any>? = null)

    /**
     * Track a page with optional properties.
     */
    fun trackPage(page: Page, properties: Map<Property, Any>? = null)

    /**
     * Set the user details for the analytics provider.
     */
    fun setUserDetails(userId: String)

    /**
     * Clear the user details for the analytics provider.
     */
    fun clearUserDetails()

    fun createPageInstance(page: Page, properties: Map<String, String>? = null) {}

    fun startPage() {}

    fun endPage() {}

    fun clearPage() {}
}
*/