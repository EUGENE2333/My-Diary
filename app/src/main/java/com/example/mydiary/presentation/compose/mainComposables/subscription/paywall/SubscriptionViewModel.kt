package com.example.mydiary.presentation.compose.mainComposables.subscription.paywall

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mydiary.core.analytics.AnalyticsTracker
import com.example.mydiary.core.analytics.Events
import com.example.mydiary.core.analytics.Page
import com.example.mydiary.core.analytics.Properties
import com.example.mydiary.core.analytics.Property
import com.example.mydiary.data.model.SubscriptionType
import com.example.mydiary.domain.repository.AuthRepository
import com.example.mydiary.presentation.compose.mainComposables.subscription.revenuecat.RestoreResult
import com.example.mydiary.presentation.compose.mainComposables.subscription.revenuecat.RevenueCatPurchases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SubscriptionViewModel @Inject constructor(
    private val revenueCatController: RevenueCatPurchases,
    private val analyticsTracker: AnalyticsTracker,git
    private val authRepository: AuthRepository,
): ViewModel() {

    private val _state = MutableStateFlow(PricingPlanUiState())
    val state: StateFlow<PricingPlanUiState> = _state.asStateFlow()

    private val _events: Channel<SubscriptionUiEvent> = Channel(Channel.BUFFERED)
    val events: Flow<SubscriptionUiEvent> = _events.receiveAsFlow()

    init {
        analyticsTracker.trackPage(Page.SUBSCRIPTION)
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            revenueCatController.getSubscriptions().onSuccess { subscriptionPlans ->
                _state.update { uiState ->
                    uiState.copy(
                        isLoading = false,
                        subscriptionPlans = subscriptionPlans,
                        monthlyPlanUi = subscriptionPlans.find { it.type == SubscriptionType.MONTHLY }
                            ?.toUi(),
                        yearlyPlanUi = subscriptionPlans.find { it.type == SubscriptionType.YEARLY }
                            ?.toUi()
                    )
                }
            }.onFailure {
                _state.update { it.copy(isLoading = false) }
                Timber.e(it, "Error fetching subscription plans")

            }
        }
    }

    /**
     * Called when the user selects a subscription plan.
     * - Indicates the selected plan in the UI.
     */
    fun onSubscriptionPlanSelected(input: PurchaseSubscriptionInput) {
        _state.update {
            it.copy(
                monthlyPlanUi = it.monthlyPlanUi?.copy(selected = input.plan == it.monthlyPlanUi),
                yearlyPlanUi = it.yearlyPlanUi?.copy(selected = input.plan == it.yearlyPlanUi)
            )
        }
        trackTapSubscriptionEvent(input)
    }

    /**
     * Creates the purchase parameters and initiates the purchase process.
     *    - When the purchase is successful, get the user subscription info and updates the user's subscription
     *    status (date) in the database.
     */
    fun onSubscriptionPurchase(input: PurchaseSubscriptionInput) {
        viewModelScope.launch {
            val subscriptionPlan = _state.value.subscriptionPlans
                ?.find { it.productId == input.plan.productId } ?: return@launch
            val params = RevenueCatPurchases.PurchaseParams(
                context = input.context,
                subscriptionPlan = subscriptionPlan
            )
            _state.update { it.copy(isLoading = true) }
            revenueCatController.purchase(params).onSuccess {
                trackSubscriptionPurchase(input)
                if (authRepository.currentUser != null) {
                    val subscriberInfo = revenueCatController.getSubscriberInfo()
                    if (subscriberInfo?.hasActiveSubscription == true) {
                        _events.send(SubscriptionUiEvent.SubscriptionPurchased)
                        _state.update { it.copy(isLoading = false) }
                    }
                }
            }.onFailure { exception ->
                Timber.e(exception, "Failed to purchase subscription")
                _state.update {
                    it.copy(
                        error = PURCHASE_ERROR_MESSAGE
                    )
                }
            }
        }
    }

    fun onRestorePurchase() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            when (val result = revenueCatController.restorePurchasesWithSealedResult()) {
                is RestoreResult.Success -> {
                    if (result.hasActiveEntitlements) {
                            if (authRepository.currentUser != null) {
                              //  userRepository.updateUser(user.copy(subscribedAt = Date()))
                                _events.send(SubscriptionUiEvent.SubscriptionPurchased)
                                _state.update { it.copy(isLoading = false) }
                            }
                    }
                }
                is RestoreResult.Error -> {
                    Timber.e(result.error.message, "Failed to restore purchase")
                    _state.update {
                        it.copy(
                            error =RESTORE_ERROR_MESSAGE,
                            isLoading = false
                        )
                    }
                }
            }
        }
    }

    private fun trackTapSubscriptionEvent(input: PurchaseSubscriptionInput) {
        when (input.plan.productId) {
            state.value.monthlyPlanUi?.productId -> {
                analyticsTracker.track(
                    Events.TAP_MONTHLY_SUBSCRIPTION,
                    mapOf(Properties.SUBSCRIPTION_TYPE to SubscriptionType.MONTHLY)
                )
            }

            state.value.yearlyPlanUi?.productId -> {
                analyticsTracker.track(
                    Events.TAP_YEARLY_SUBSCRIPTION,
                    mapOf(Properties.SUBSCRIPTION_TYPE to SubscriptionType.YEARLY)
                )
            }
        }
    }

    private fun trackSubscriptionPurchase(input: PurchaseSubscriptionInput) {
        val subscriptionType = when (input.plan.productId) {
            state.value.monthlyPlanUi?.productId -> SubscriptionType.MONTHLY
            state.value.yearlyPlanUi?.productId -> SubscriptionType.YEARLY
            else -> null
        }
        val props = mutableMapOf<Property, Any>()
        props[Properties.SUBSCRIPTION_PRICE] = input.rawPrice
        subscriptionType?.let { props.put(Properties.SUBSCRIPTION_TYPE, it) }
        input.currency?.let { props.put(Properties.SUBSCRIPTION_CURRENCY, it) }
        analyticsTracker.track(Events.ACTION_SUBSCRIPTION_PURCHASED, props)
    }

    private inline fun <reified T : PricingPlanUiState> updateScreenUiState(function: (T) -> T) =
        _state.update { currentState ->
            (currentState as? T)?.let(function) ?: currentState
        }

    companion object {
        const val PURCHASE_ERROR_MESSAGE = "Unable to complete your purchase. This may be temporary - please check your payment details and try again. If the issue persists, email us at calorica.support@gmail.com . Our support team is here to help."
        const val RESTORE_ERROR_MESSAGE = "Unable to restore your purchase. This may be temporary - please check your internet connection and try again. If the issue persists, email us at calorica.support@gmail.com"
    }
}