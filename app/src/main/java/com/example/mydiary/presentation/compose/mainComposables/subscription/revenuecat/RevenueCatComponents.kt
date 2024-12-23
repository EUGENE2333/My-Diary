package com.example.mydiary.presentation.compose.mainComposables.subscription.revenuecat

import android.content.Context
import com.example.mydiary.data.model.SubscriberInfo
import com.example.mydiary.data.model.SubscriptionPlan
import com.example.mydiary.data.model.UserId

/**
 * RevenueCatInitializer is an interface that defines the initialization of the RevenueCat SDK.
 * The implementation of this interface should initialize the RevenueCat SDK with the provided context and API key.
 */
interface RevenueCatInitializer {

    /**
     * Should be called at the start of the application.
     */
    fun initialize(context: Context)
}


/**
 * RevenueCatAuthenticator is an interface that defines the authentication methods for RevenueCat.
 * It provides methods to log in and log out a user. The implementation of this interface should handle the
 * authentication logic using the RevenueCat SDK.
 */
interface RevenueCatAuthenticator {

    /**
     * Logs in the user with the provided user ID.
     */
    suspend fun logIn(userId: UserId)

    /**
     * Logs out the current user. This will clear the user's session and reset the RevenueCat SDK.
     */
    fun logOut()
}

interface RevenueCatPurchases {

    /**
     * Purchases the subscription plan for the user.
     * @param params The [PurchaseParams] required to make the purchase.
     */
    suspend fun purchase(params: PurchaseParams): Result<Unit>

    /**
     * Gets the subscriber information for the current user.
     */
    suspend fun getSubscriberInfo(): SubscriberInfo?

    /**
     * Gets the list of available subscription plans.
     */
    suspend fun getSubscriptions(): Result<List<SubscriptionPlan>>

    /**
     * Restores the user's purchase history.
     */
    suspend fun restorePurchase(): Boolean

    suspend fun restorePurchasesWithSealedResult(): RestoreResult

    /**
     * Represents the parameters required to make a purchase.
     * @param context The context of the activity or fragment that is making the purchase.
     * This is required to show the purchase dialog.
     * @param subscriptionPlan The [SubscriptionPlan] plan that the user wants to purchase.
     */
    data class PurchaseParams(
        val context: Context,
        val subscriptionPlan: SubscriptionPlan
    )
}