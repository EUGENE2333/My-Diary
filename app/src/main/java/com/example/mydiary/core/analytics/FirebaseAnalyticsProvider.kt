package com.example.mydiary.core.analytics

import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.ParametersBuilder
import javax.inject.Inject

/**
 * Implementation of [AnalyticsProvider] for Firebase Analytics.
 */
class FirebaseAnalyticsProvider @Inject constructor(
    private val firebaseAnalytics: FirebaseAnalytics,
    private val parametersBuilder: ParametersBuilder
) : AnalyticsProvider {

    override fun track(analyticEvent: AnalyticEvent, properties: Map<Property, Any>?) {
        val bundle = parametersBuilder.buildBundle(properties)

        firebaseAnalytics.logEvent(analyticEvent.transformToFirebaseEvent(), bundle)
    }

    override fun trackPage(page: Page, properties: Map<Property, Any>?) {
        val bundle = parametersBuilder.buildBundle(properties)

        firebaseAnalytics.logEvent(page.pageName, bundle)
    }

    override fun setUserDetails(userId: String) {
        firebaseAnalytics.setUserId(userId)
    }

    override fun clearUserDetails() {
        firebaseAnalytics.setUserId(null)
    }

    private fun AnalyticEvent.transformToFirebaseEvent(): String = when (this) {
        Events.ACTION_SUBSCRIPTION_PURCHASED -> FirebaseAnalytics.Event.PURCHASE
        else -> this.eventName
    }
}

fun ParametersBuilder.buildBundle(properties: Map<Property, Any>?): Bundle {
    properties?.transformToFirebaseProperty()?.forEach { (propertyName, value) ->
        when (value) {
            is String -> param(propertyName, value)
            is Int -> param(propertyName, value.toLong())
            is Long -> param(propertyName, value)
            is Double -> param(propertyName, value)
            else -> param(propertyName, value.toString())
        }
    }
    return bundle
}

private fun Map<Property, Any>.transformToFirebaseProperty(): Map<String, Any> = mapKeys { (property) ->
    when (property) {
        Properties.SUBSCRIPTION_PRICE -> FirebaseAnalytics.Param.VALUE
        Properties.SUBSCRIPTION_CURRENCY -> FirebaseAnalytics.Param.CURRENCY
        else -> property.propertyName
    }
}