package com.example.mydiary.presentation.compose.mainComposables.subscription.paywall

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.mydiary.R
import com.example.mydiary.ui.components.MyDiaryFilledButton
import com.example.mydiary.ui.theme.LocalSpacing
import com.example.mydiary.ui.theme.bodyLarge
import com.example.mydiary.ui.theme.bodyMedium
import com.example.mydiary.ui.theme.bodySmall
import com.example.mydiary.ui.theme.crownColor
import com.example.mydiary.ui.theme.headlineExtraLarge


@SuppressLint("RememberReturnType")
@Composable
 fun SubscriptionScreen(
    onNavigateToSuccess: () -> Unit ={},
    onRestorePurchase: () -> Unit = {},
    onPlanSelect: (PurchaseSubscriptionInput) -> Unit = {},
    viewModel: SubscriptionViewModel = hiltViewModel(),
    onNavigate:() -> Unit
){
    val uiState by viewModel.state.collectAsStateWithLifecycle()

    SubscriptionContent(
        state = uiState,
        onPlanSelect = onPlanSelect,
        onRestorePurchase = onRestorePurchase,
        onSubscriptionPurchase = onNavigateToSuccess,
        modifier = Modifier.fillMaxSize()
    )
}


@Composable
fun SubscriptionContent(
    state: PricingPlanUiState,
    onRestorePurchase: () -> Unit,
    onPlanSelect: (PurchaseSubscriptionInput) -> Unit,
    onSubscriptionPurchase: () -> Unit,
    modifier: Modifier = Modifier
){

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        Spacer(modifier = Modifier.height(LocalSpacing.current.large))
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = LocalSpacing.current.medium)
            ) {
                Spacer(modifier = Modifier.height(LocalSpacing.current.large))

                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = LocalSpacing.current.small),
                    text = stringResource(id = R.string.pricing_plan_screen_title),
                    maxLines = 2,
                    textAlign = TextAlign.Start,
                    color = MaterialTheme.colorScheme.onPrimary,
                    style = headlineExtraLarge
                )

                Text(
                    text = stringResource(id = R.string.pricing_plan_screen_subtitle),
                    textAlign = TextAlign.Start,
                    style = bodyLarge
                )

                Spacer(modifier = Modifier.height(LocalSpacing.current.extraLarge))

                Offers()

                Spacer(modifier = Modifier.height(LocalSpacing.current.extraLarge))

                state.monthlyPlanUi ?: return
                state.yearlyPlanUi ?: return

                PricingPlans(
                    monthlyPlan = state.monthlyPlanUi,
                    yearlyPlan = state.yearlyPlanUi,
                    onPlanSelect = { onPlanSelect(it) }
                )

                Spacer(modifier = Modifier.height(LocalSpacing.current.extraLarge))
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primaryContainer)
                .padding(
                    bottom = LocalSpacing.current.large,
                    start = LocalSpacing.current.medium,
                    end = LocalSpacing.current.medium
                )
        ) {
            MyDiaryFilledButton(
                onClick = {
                    val selectedPlan = if (state.yearlyPlanUi?.selected == true) {
                        state.yearlyPlanUi
                    } else {
                        state.monthlyPlanUi
                    }
                    selectedPlan?.let {
                        //   onSubscriptionPurchase(PurchaseSubscriptionInput(context, selectedPlan))
                        onSubscriptionPurchase()
                    }
                },
                text = stringResource(id = R.string.subscribe_button),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = LocalSpacing.current.small)
            )
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onRestorePurchase() },
                color = MaterialTheme.colorScheme.onPrimary,
                text = stringResource(id = R.string.restore_purchase),
                style = bodyLarge,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun Offers() {
    Column(
        modifier = Modifier.fillMaxWidth()
            .padding(horizontal = LocalSpacing.current.medium),
        verticalArrangement = Arrangement.spacedBy(LocalSpacing.current.medium)
    ) {
        PricingPlanOfferItem(
            buildAnnotatedString {
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append(stringResource(id = R.string.pricing_plan_offer_no_ads))
                }
            }
        )
        PricingPlanOfferItem(
            buildAnnotatedString {
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append(stringResource(id = R.string.pricing_plan_offer_access_fonts))
                }
            }
        )
        PricingPlanOfferItem(
            buildAnnotatedString {
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append(stringResource(id = R.string.pricing_plan_offer_priority_on_support))
                }
            }
        )
        PricingPlanOfferItem(
            buildAnnotatedString {
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append(stringResource(id = R.string.pricing_plan_offer_add_images))
                }
            }
        )
    }
}

@Composable
private fun PricingPlanOfferItem(text: AnnotatedString) {
    Row {
        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.ic_crown),
            contentDescription = null,
            tint = crownColor,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(LocalSpacing.current.medium))
        Text(
            text = text,
            textAlign = TextAlign.Start,
            color = MaterialTheme.colorScheme.onPrimary,
            style = bodyMedium
        )
    }
}

@Composable
fun PricingPlans(
    monthlyPlan: SubscriptionPlanUi,
    yearlyPlan: SubscriptionPlanUi,
    onPlanSelect: (PurchaseSubscriptionInput) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier =
        modifier
            .fillMaxWidth()
            .padding(bottom = LocalSpacing.current.medium),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        val context = LocalContext.current
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            SubscriptionItemView(
                modifier = Modifier.offset(y = 28.dp),
                title = yearlyPlan.title.toString(LocalContext.current),
                price = yearlyPlan.price.toString(LocalContext.current),
                cadence = yearlyPlan.cadence.toString(LocalContext.current),
                selected = yearlyPlan.selected,
                highlight = true,
                onClick = { onPlanSelect(PurchaseSubscriptionInput(context, yearlyPlan)) }
            )
            BestValueLabel()
        }
        Spacer(modifier = Modifier.height(LocalSpacing.current.extraMediumLarge))
        SubscriptionItemView(
            title = monthlyPlan.title.toString(LocalContext.current),
            price = monthlyPlan.price.toString(LocalContext.current),
            cadence = monthlyPlan.cadence.toString(LocalContext.current),
            selected = monthlyPlan.selected,
            highlight = false,
            onClick = { onPlanSelect(PurchaseSubscriptionInput(context, monthlyPlan)) }
        )
    }
}

@Composable
private fun BestValueLabel() {
    Box(
        modifier = Modifier
            .wrapContentWidth(Alignment.CenterHorizontally)
            .heightIn(min = LocalSpacing.current.extraLarge)
            .background(
                color = Color(0xFF4CAF50),
                shape = MaterialTheme.shapes.medium
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            modifier = Modifier
                .wrapContentWidth(Alignment.CenterHorizontally)
                .padding(horizontal = LocalSpacing.current.medium),
            text = stringResource(id = R.string.pricing_plan_best_value),
            style = bodySmall,
            color = Color.White
        )
    }
}