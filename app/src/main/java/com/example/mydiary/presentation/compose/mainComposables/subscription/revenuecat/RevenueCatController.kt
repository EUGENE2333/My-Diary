package com.example.mydiary.presentation.compose.mainComposables.subscription.revenuecat

import android.content.Context
import com.example.mydiary.common.provider.SecretId
import com.example.mydiary.common.provider.SecretsProvider
import com.example.mydiary.data.model.Price
import com.example.mydiary.data.model.SubscriberInfo
import com.example.mydiary.data.model.SubscriptionPlan
import com.example.mydiary.data.model.SubscriptionType
import com.example.mydiary.data.model.UserId
import com.example.mydiary.data.utils.Utils.findActivity
import com.example.mydiary.di.IoDispatcher
import com.revenuecat.purchases.CustomerInfo
import com.revenuecat.purchases.LogLevel
import com.revenuecat.purchases.Package
import com.revenuecat.purchases.PurchaseParams
import com.revenuecat.purchases.Purchases
import com.revenuecat.purchases.PurchasesConfiguration
import com.revenuecat.purchases.PurchasesError
import com.revenuecat.purchases.awaitCustomerInfo
import com.revenuecat.purchases.awaitLogIn
import com.revenuecat.purchases.awaitOfferings
import com.revenuecat.purchases.awaitPurchase
import com.revenuecat.purchases.restorePurchasesWith
import dagger.Lazy
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

/**
 * Controller class that handles the RevenueCat SDK initialization, authentication, and purchase
 * and subscription operations.
 */
@Singleton
class RevenueCatController @Inject constructor(
    private val purchases: Lazy<Purchases>,
    private val secretsProvider: SecretsProvider,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : RevenueCatInitializer, RevenueCatAuthenticator, RevenueCatPurchases {

    override fun initialize(context: Context) {
        Purchases.logLevel = LogLevel.DEBUG
        Purchases.configure(
            PurchasesConfiguration.Builder(
                context = context,
                apiKey = secretsProvider.getSecretValue(SecretId.REVENUECAT_API_KEY)
            ).build()
        )
    }

    override suspend fun logIn(userId: UserId) {
        runCatching {
            purchases.get().awaitLogIn(userId.value)
        }.onFailure {
            Timber.e(it, "Failed to log in with RevenueCat")
        }
    }

    override fun logOut() {
        purchases.get().logOut()
    }

    /**
     * We only have two subscription plans: monthly and yearly.
     */
    override suspend fun purchase(params: RevenueCatPurchases.PurchaseParams): Result<Unit> =
        withContext(ioDispatcher) {
            val offerings = purchases.get().awaitOfferings()
            val revenueCatPackage: Package? = when (params.subscriptionPlan.type) {
                SubscriptionType.MONTHLY -> offerings.current?.monthly
                SubscriptionType.YEARLY -> offerings.current?.annual
            }
            revenueCatPackage
                ?: return@withContext Result.failure(Exception("Failed to get package from RevenueCat"))
            return@withContext params.context.findActivity()?.let { activity ->
                runCatching {
                    purchases.get()
                        .awaitPurchase(PurchaseParams.Builder(activity, revenueCatPackage).build())
                    Unit
                }
            } ?: Result.failure(Exception("Failed to make a purchase with RevenueCat"))
        }

    /**
     * Get the customer info from RevenueCat and map it to our [SubscriberInfo] model.
     */
    override suspend fun getSubscriberInfo(): SubscriberInfo? = withContext(ioDispatcher) {
        runCatching {
            val customerInfo = purchases.get().awaitCustomerInfo()
            SubscriberInfo(
                hasActiveSubscription = customerInfo.activeSubscriptions.isNotEmpty(),
                expirationDate = customerInfo.latestExpirationDate
            )
        }.getOrNull()
    }

    override suspend fun getSubscriptions(): Result<List<SubscriptionPlan>> =
        withContext(ioDispatcher) {
            // TODO: Remove this mock data once RevenueCat is configured
            return@withContext Result.success(
                listOf(
                    SubscriptionPlan(
                        productId = "monthly_plan",
                        type = SubscriptionType.MONTHLY,
                        price = Price(formattedValue = "$9.99", currency = "USD"),
                        identifier = ""
                    ),
                    SubscriptionPlan(
                        productId = "yearly_plan",
                        type = SubscriptionType.YEARLY,
                        price = Price(formattedValue = "$99.99", currency = "USD"),
                        identifier = ""
                    )
                )
            )

            /* Original code - uncomment when RevenueCat is set up
            runCatching {
                val offerings = purchases.get().awaitOfferings()
                listOfNotNull(
                    offerings.current?.monthly?.also { Timber.d("\n\nMonthly: $it") },
                    offerings.current?.annual?.also { Timber.d("\n\nAnnual: $it") }
                )
            }.map { offeringList ->
                offeringList.map { it.toSubscriptionPlan() }
            }
            */
        }
    /*override suspend fun getSubscriptions(): Result<List<SubscriptionPlan>> =
        withContext(ioDispatcher) {
            runCatching {
                Timber.d("Fetching offerings from RevenueCat...") // ADD THIS
                val offerings = purchases.get().awaitOfferings()
                println("🥰🥰Current offering: ${offerings.current}") // ADD THIS
                println("🥰🥰Monthly package: ${offerings.current?.monthly}") // ADD THIS
                println("🥰🥰Annual package: ${offerings.current?.annual}") // ADD THIS

                listOfNotNull(
                    offerings.current?.monthly?.also { Timber.d("\n\nMonthly: $it") },
                    offerings.current?.annual?.also { Timber.d("\n\nAnnual: $it") }
                )
            }.map { offeringList ->
                println("Mapping ${offeringList.size} offerings to SubscriptionPlan") // ADD THIS
                offeringList.map { it.toSubscriptionPlan() }
            }.onFailure {
                println(" ❌$it, Failed to get subscriptions") // ADD THIS
            }
        }*/

    override suspend fun restorePurchase(): Boolean = withContext(ioDispatcher) {
        var success = false
        purchases.get().restorePurchasesWith { customerInfo ->
            // check customerInfo to see if entitlement is now active
            if (customerInfo.entitlements.active.isNotEmpty()) {
                success = true
            }
        }
        return@withContext success
    }

    override suspend fun restorePurchasesWithSealedResult(): RestoreResult =
        suspendCoroutine { continuation ->
            purchases.get().restorePurchasesWith(
                onError = { error ->
                    continuation.resume(RestoreResult.Error(error))
                },
                onSuccess = { customerInfo ->
                    val hasActiveEntitlements = customerInfo.entitlements.active.isNotEmpty()
                    continuation.resume(
                        RestoreResult.Success(
                            customerInfo = customerInfo,
                            hasActiveEntitlements = hasActiveEntitlements
                        )
                    )
                }
            )
        }

    companion object {
        private const val TAG = "RevenueCatController"
    }
}

sealed class RestoreResult {
    data class Success(
        val customerInfo: CustomerInfo,
        val hasActiveEntitlements: Boolean
    ) : RestoreResult()
    data class Error(val error: PurchasesError) : RestoreResult()
}
