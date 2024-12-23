package com.example.mydiary.core.analytics

/**
 * Represents an analytics event that can be tracked.
 */
sealed interface AnalyticEvent {
    val eventName: String
}

enum class Events(override val eventName: String) : AnalyticEvent {

    TAP_YEARLY_SUBSCRIPTION("android_tap_yearly_subscription"), // Needs properties
    TAP_MONTHLY_SUBSCRIPTION("android_tap_monthly_subscription"), // Needs properties
    ACTION_SUBSCRIPTION_PURCHASED("android_action_subscription_purchased"),
}