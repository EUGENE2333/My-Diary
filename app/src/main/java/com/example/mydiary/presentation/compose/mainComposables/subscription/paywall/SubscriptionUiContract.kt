package com.example.mydiary.presentation.compose.mainComposables.subscription.paywall

import android.content.Context
import com.example.mydiary.R
import com.example.mydiary.core.resources.TextResource
import com.example.mydiary.data.model.SubscriptionPlan
import com.example.mydiary.data.model.SubscriptionType
import com.example.mydiary.data.utils.extractCurrency
import com.example.mydiary.data.utils.removeCurrencySymbol

data class PricingPlanUiState(
    val isLoading: Boolean = false,
    val subscriptionPlans: List<SubscriptionPlan>? = null,
    val monthlyPlanUi: SubscriptionPlanUi? = null,
    val yearlyPlanUi: SubscriptionPlanUi? = null,
    val error: String? = null
)

data class SubscriptionPlanUi(
    val productId: String,
    val title: TextResource,
    val price: TextResource,
    val cadence: TextResource,
    val selected: Boolean = false
)

data class PurchaseSubscriptionInput(
    val context: Context,
    val plan: SubscriptionPlanUi
) {
    val rawPrice: String = plan.price.toString(context).removeCurrencySymbol()

    val currency: String?
        get() = plan.price.toString(context).extractCurrency()
}

fun SubscriptionPlan.toUi(selected: Boolean = false): SubscriptionPlanUi {
    val (title, price, cadence) = if (type == SubscriptionType.MONTHLY) {
        listOf(
            TextResource.from(R.string.pricing_plan_monthly),
            TextResource.from(price.formattedValue),
            TextResource.from(R.string.pricing_plan_monthly_cadence)
        )
    } else {
        listOf(
            TextResource.from(R.string.pricing_plan_yearly),
            TextResource.from(price.formattedValue),
            TextResource.from(R.string.pricing_plan_yearly_cadence)
        )
    }

    return SubscriptionPlanUi(
        productId = productId,
        title = title,
        price = price,
        cadence = cadence,
        selected = selected
    )
}

sealed interface SubscriptionUiEvent {
    data object SubscriptionPurchased : SubscriptionUiEvent
}
