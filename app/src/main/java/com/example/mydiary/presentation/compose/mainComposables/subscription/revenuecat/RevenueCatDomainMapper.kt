package com.example.mydiary.presentation.compose.mainComposables.subscription.revenuecat

import com.example.mydiary.data.model.Price
import com.example.mydiary.data.model.SubscriptionPlan
import com.example.mydiary.data.model.SubscriptionType
import com.revenuecat.purchases.Package
import com.revenuecat.purchases.PackageType

fun Package.toSubscriptionPlan(): SubscriptionPlan = SubscriptionPlan(
    identifier = this.identifier,
    productId = this.product.id,
    type = this.packageType.toSubscriptionType(),
    price = Price(
        formattedValue = this.product.price.formatted,
        currency = this.product.price.currencyCode
    )
)

fun PackageType.toSubscriptionType(): SubscriptionType = when (this) {
    PackageType.MONTHLY -> SubscriptionType.MONTHLY
    PackageType.ANNUAL -> SubscriptionType.YEARLY
    else -> throw IllegalArgumentException("Unsupported package type: $this")
}
